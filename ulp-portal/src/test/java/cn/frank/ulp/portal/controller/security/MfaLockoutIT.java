/*
 * ulp-portal - United Login Platform
 * Copyright (c) 2022-Present Frank Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.frank.ulp.portal.controller.security;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson2.JSON;

import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.support.security.mfa.MfaBackupCodeGenerator;
import cn.frank.ulp.support.security.mfa.MfaPendingAuthenticationStore;
import cn.frank.ulp.support.security.mfa.MfaSecretCipher;
import cn.frank.ulp.support.security.mfa.MfaSecretGenerator;
import cn.frank.ulp.support.testsupport.AbstractMfaIntegrationTest;

import jakarta.servlet.http.Cookie;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Phase 5.6 端到端 IT —— 覆盖 {@link cn.frank.ulp.support.security.mfa.MfaLockoutService}
 * 在 {@code POST /api/v1/mfa/challenge} OTP 路径上的 3 条契约：
 *
 * <ol>
 *   <li>{@link #fifthInvalidOtp_returnsLockedAndRetryAfter()} —
 *       连错 5 次后第 5 次响应 423 {@code locked_out} + {@code Retry-After} 头，
 *       Redis 失败计数 = 5</li>
 *   <li>{@link #lockoutWindow_doesNotConsumePendingEntry()} —
 *       已锁状态下继续提交错码，pending 条目仍留在 Redis（spec.md 第 3 节：
 *       锁窗口允许 user 等待自然解锁后用同一 pending 重试）</li>
 *   <li>{@link #successfulOtpAtAttemptFour_clearsFailureCounter()} —
 *       前 4 次错、第 5 次提交正确 TOTP → SUCCESS，Redis 失败计数被清零</li>
 * </ol>
 *
 * <p>与 {@link PortalMfaChallengeLoginIT#fiveWrongOtps_finalAttemptReturnsLockedWithRetryAfter()}
 * 的关系：那条 IT 验证整条登录→挑战→锁定的链路是否能跑通；本 IT 是 {@code MfaLockoutService}
 * 业务契约的专属断言集，所以保留场景 1 重复验证 423/Retry-After（成本极低，但 contract
 * ownership 应该跟着 service 而不是依附在另一个 IT 的最后一个 test 里）。
 *
 * <p>事务模式 / IP 对齐 / seed 模式与 {@link PortalMfaChallengeLoginIT} 完全一致；理由见那里。
 */
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class MfaLockoutIT extends AbstractMfaIntegrationTest {

    private static final String           LOGIN_PATH     = "/api/v1/login";
    private static final String           CHALLENGE_PATH = "/api/v1/mfa/challenge";
    private static final String           PENDING_COOKIE = "ulp-mfa-pending";

    @Autowired
    private UserRepository                userRepository;

    @Autowired
    private PasswordEncoder               passwordEncoder;

    @Autowired
    private MfaSecretGenerator            mfaSecretGenerator;

    @Autowired
    private MfaSecretCipher               mfaSecretCipher;

    @Autowired
    private MfaBackupCodeGenerator        mfaBackupCodeGenerator;

    @Autowired
    private MfaPendingAuthenticationStore pendingStore;

    private String                        seededUserId;
    private String                        seededUsername;
    private String                        seededUserSecret;

    @AfterEach
    void cleanup() {
        if (seededUserId != null) {
            cleanMfaRedisKeys("user", seededUserId);
            try {
                userRepository.deleteById(seededUserId);
            } catch (RuntimeException ignored) {
                // 不阻塞下一个 test —— nanoTime 唯一 username 避免冲突
            }
            seededUserId = null;
            seededUsername = null;
            seededUserSecret = null;
        }
    }

    /**
     * 场景 1：5 次错码 → 第 5 次响应 423 locked_out + Retry-After。
     * 主断言锚：Redis FAIL 计数最终落到 5（post-increment 触发 {@code count >= threshold(5)}）。
     */
    @Test
    void fifthInvalidOtp_returnsLockedAndRetryAfter() throws Exception {
        String rawPassword = "LockoutTrip@Pwd-12345";
        seedUserWithMfa("p-mfa-lk1-" + System.nanoTime(), rawPassword);

        Cookie pending = loginAndGetPendingCookie(rawPassword);
        String sourceIp = resolveStashedSourceIp(pending.getValue());
        String wrongOtp = nudgeOtp(currentValidOtp());

        // 前 4 次：401 invalid_otp，counter 从 1 累到 4，未达阈值
        for (int i = 1; i <= 4; i++) {
            mockMvc.perform(post(CHALLENGE_PATH).with(csrf()).cookie(pending).with(req -> {
                req.setRemoteAddr(sourceIp);
                return req;
            }).contentType(MediaType.APPLICATION_JSON).content("{\"code\":\"" + wrongOtp + "\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("invalid_otp"));
        }
        assertThat(redisTemplate.opsForValue().get(failKey("user", seededUserId)))
            .as("4 次失败后 Redis 计数应为 4").isEqualTo("4");

        // 第 5 次：counter → 5 ≥ threshold → 423 locked_out + Retry-After
        mockMvc.perform(post(CHALLENGE_PATH).with(csrf()).cookie(pending).with(req -> {
            req.setRemoteAddr(sourceIp);
            return req;
        }).contentType(MediaType.APPLICATION_JSON).content("{\"code\":\"" + wrongOtp + "\"}"))
            .andExpect(status().isLocked()).andExpect(jsonPath("$.status").value("locked_out"))
            .andExpect(header().exists(HttpHeaders.RETRY_AFTER));

        assertThat(redisTemplate.opsForValue().get(failKey("user", seededUserId)))
            .as("锁定后 Redis 计数应为 5").isEqualTo("5");
    }

    /**
     * 场景 2：锁定窗口内继续提交，pending Redis 条目不被消费。
     *
     * <p>这是 spec.md Phase 3 显式约定：{@code isLockedOut} 命中走 LOCKED_OUT 分支 return，
     * 中间不会调 {@code pendingStore.delete/consume}，让 user 等自然解锁后用同一 pending 重试，
     * 避免被恶意流量"锁掉就丢 session"。源码契约见
     * {@link cn.frank.ulp.support.security.mfa.MfaChallengeService#verifyAndCommit} L189-191。
     */
    @Test
    void lockoutWindow_doesNotConsumePendingEntry() throws Exception {
        String rawPassword = "LockoutKeep@Pwd-12345";
        seedUserWithMfa("p-mfa-lk2-" + System.nanoTime(), rawPassword);

        Cookie pending = loginAndGetPendingCookie(rawPassword);
        String sourceIp = resolveStashedSourceIp(pending.getValue());
        String wrongOtp = nudgeOtp(currentValidOtp());

        // 触发锁定（5 次错码）
        for (int i = 1; i <= 5; i++) {
            mockMvc.perform(post(CHALLENGE_PATH).with(csrf()).cookie(pending).with(req -> {
                req.setRemoteAddr(sourceIp);
                return req;
            }).contentType(MediaType.APPLICATION_JSON).content("{\"code\":\"" + wrongOtp + "\"}"))
                .andExpect(status().is(i == 5 ? 423 : 401));
        }

        // 锁定后 pending Redis 条目仍在
        assertThat(redisTemplate.hasKey(pendingKey(pending.getValue())))
            .as("锁定后 pending Redis 条目应被保留（让 user 自然解锁后重试）").isTrue();

        // 锁定窗口内再提交 1 次 → 仍 423，pending 仍在
        mockMvc.perform(post(CHALLENGE_PATH).with(csrf()).cookie(pending).with(req -> {
            req.setRemoteAddr(sourceIp);
            return req;
        }).contentType(MediaType.APPLICATION_JSON).content("{\"code\":\"" + wrongOtp + "\"}"))
            .andExpect(status().isLocked()).andExpect(jsonPath("$.status").value("locked_out"));
        assertThat(redisTemplate.hasKey(pendingKey(pending.getValue())))
            .as("锁定窗口内重复提交不应消费 pending").isTrue();
    }

    /**
     * 场景 3：N 次错（N &lt; 阈值）后提交正确 OTP，{@code MfaLockoutService.clear} 被调用，
     * Redis 失败计数 key 被删除（不只是清 0，是删 key）。
     *
     * <p>这是"成功路径必须重置 brute-force 预算"的契约 —— 否则用户在阈值 -1 的攻击窗口里
     * 一次正确登录后还会被下一轮 1 次错码踢到 1 步之内，体验破坏严重。
     */
    @Test
    void successfulOtpAtAttemptFour_clearsFailureCounter() throws Exception {
        String rawPassword = "LockoutClear@Pwd-12345";
        seedUserWithMfa("p-mfa-lk3-" + System.nanoTime(), rawPassword);

        Cookie pending = loginAndGetPendingCookie(rawPassword);
        String sourceIp = resolveStashedSourceIp(pending.getValue());
        String wrongOtp = nudgeOtp(currentValidOtp());

        // 前 4 次错，counter 累到 4
        for (int i = 1; i <= 4; i++) {
            mockMvc.perform(post(CHALLENGE_PATH).with(csrf()).cookie(pending).with(req -> {
                req.setRemoteAddr(sourceIp);
                return req;
            }).contentType(MediaType.APPLICATION_JSON).content("{\"code\":\"" + wrongOtp + "\"}"))
                .andExpect(status().isUnauthorized());
        }
        assertThat(redisTemplate.opsForValue().get(failKey("user", seededUserId)))
            .as("4 次失败后 Redis 计数应为 4").isEqualTo("4");

        // 第 5 次提交"当下窗口正确 OTP" → 200 ok，clear 被调用
        String validOtp = currentValidOtp();
        mockMvc.perform(post(CHALLENGE_PATH).with(csrf()).cookie(pending).with(req -> {
            req.setRemoteAddr(sourceIp);
            return req;
        }).contentType(MediaType.APPLICATION_JSON).content("{\"code\":\"" + validOtp + "\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ok"));

        // 关键断言：FAIL key 被删除
        assertThat(redisTemplate.hasKey(failKey("user", seededUserId)))
            .as("成功后 Redis 失败计数 key 应被 clear() 删掉").isFalse();
    }

    /** 用当前 30s 窗口计算正确 TOTP —— OTP 路径 seed 用同款 SHA1 algorithm。 */
    private String currentValidOtp() {
        return computeTotp(seededUserSecret);
    }

    /** Seed 一条已绑 MFA 的 ENABLED user；与 PortalMfaChallengeLoginIT 同款 schema。 */
    private void seedUserWithMfa(String username, String rawPassword) {
        String secretBase32 = mfaSecretGenerator.generate();
        String cipher = mfaSecretCipher.encrypt(secretBase32.getBytes(StandardCharsets.UTF_8));
        List<String> plaintextCodes = mfaBackupCodeGenerator.generate();
        String backupCodesJson = JSON.toJSONString(
            plaintextCodes.stream().map(passwordEncoder::encode).collect(Collectors.toList()));

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(rawPassword));
        user.setStatus(UserStatus.ENABLED);
        user.setEmail(username + "@example.com");
        user.setFullName(username);
        user.setEmailVerified(Boolean.TRUE);
        user.setPhoneVerified(Boolean.FALSE);
        user.setNeedChangePassword(Boolean.FALSE);
        user.setDataOrigin("input");
        user.setLastUpdatePasswordTime(LocalDateTime.now());
        user.setMfaEnabled(Boolean.TRUE);
        user.setTotpSecretCipher(cipher);
        user.setBackupCodesJson(backupCodesJson);
        seededUsername = username;
        seededUserSecret = secretBase32;
        seededUserId = userRepository.saveAndFlush(user).getId();
    }

    /** 走 {@code POST /api/v1/login} 拿 {@code ulp-mfa-pending} cookie。 */
    private Cookie loginAndGetPendingCookie(String rawPassword) throws Exception {
        MvcResult result = mockMvc
            .perform(post(LOGIN_PATH).with(csrf()).param("username", seededUsername)
                .param("password", rawPassword))
            .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("mfa_required"))
            .andReturn();
        Cookie pending = result.getResponse().getCookie(PENDING_COOKIE);
        assertThat(pending).as("登录后必须下发 pending cookie").isNotNull();
        return pending;
    }

    /** 见 {@link PortalMfaChallengeLoginIT#resolveStashedSourceIp(String)}。 */
    private String resolveStashedSourceIp(String challengeId) {
        return pendingStore.peek(challengeId).map(entry -> entry.getSourceIp()).orElseThrow(
            () -> new IllegalStateException("pending entry missing for challenge " + challengeId));
    }
}
