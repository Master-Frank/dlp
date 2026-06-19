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
import cn.frank.ulp.support.security.mfa.MfaBackupCodeService;
import cn.frank.ulp.support.security.mfa.MfaPendingAuthenticationStore;
import cn.frank.ulp.support.security.mfa.MfaSecretCipher;
import cn.frank.ulp.support.security.mfa.MfaSecretGenerator;
import cn.frank.ulp.support.testsupport.AbstractMfaIntegrationTest;

import jakarta.servlet.http.Cookie;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Phase 5.5 端到端 IT —— 覆盖 backup-code 在 {@code POST /api/v1/mfa/challenge} 的三条业务路径：
 *
 * <ol>
 *   <li>{@link #ninthBackupCode_returnsWarningAtTwoOrLess()} —
 *       已消费 8 个剩 2，HTTP 提交第 9 个 → 200 ok / {@code backup_codes_remaining=1} /
 *       {@code regenerate_backup_codes_warning=true} / {@code regenerate_backup_codes_required=false}</li>
 *   <li>{@link #tenthBackupCode_returnsRequiredAtZero()} —
 *       已消费 9 个剩 1，HTTP 提交最后 1 个 → 200 ok / {@code backup_codes_remaining=0} /
 *       两个 flag 都是 true</li>
 *   <li>{@link #replayingAlreadyConsumedBackupCode_isRejected()} —
 *       HTTP 消费 code#1 成功；新登录拿新 pending，再提交同一 code#1 → 401 invalid_backup_code</li>
 * </ol>
 *
 * <p>"消费 N 个" 的预热步骤走 {@link MfaBackupCodeService#consume(String, String, String)}
 * 直调，不经 HTTP —— 一来每次 HTTP 成功会提交 Authentication + 消费 pending entry，必须重新登录；
 * 二来这里要验的是"响应 shape 随剩余数变化"而不是"业务流可重复 N 次"，把循环放在服务层更聚焦。
 * 直调路径与 HTTP 路径共用同一份 store + 同一个 {@code PasswordEncoder}，状态完全一致。
 *
 * <p>事务模式与 {@link PortalMfaChallengeLoginIT} 同款：{@code NOT_SUPPORTED} —— 登录链路里
 * {@code UserServiceImpl#findByUsernameOrPhoneOrEmail} 在独立线程跑三路查询，看不到测试事务内
 * uncommitted 的 seed。代价是 seed 真写库，{@code @AfterEach} 手动清账号 + Redis 计数器。
 *
 * <p>未挂组织：{@code OrgMfaPolicyService.isUserEnforced} 自然 false，登录走 CHALLENGE_REQUIRED
 * 而不是 SETUP_REQUIRED，与本 IT 关注的 backup-code 分支正交。
 */
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class MfaBackupCodeIT extends AbstractMfaIntegrationTest {

    private static final String           LOGIN_PATH     = "/api/v1/login";
    private static final String           CHALLENGE_PATH = "/api/v1/mfa/challenge";
    private static final String           PENDING_COOKIE = "ulp-mfa-pending";
    private static final String           USER_TYPE      = "user";

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
    private MfaBackupCodeService          mfaBackupCodeService;

    @Autowired
    private MfaPendingAuthenticationStore pendingStore;

    private String                        seededUserId;
    private String                        seededUsername;
    /** Seed 时记录的 10 个明文备份码，给 HTTP / 直调消费使用（DB 里只有编码后的）。 */
    private List<String>                  seededPlaintextBackupCodes;

    @AfterEach
    void cleanup() {
        if (seededUserId != null) {
            cleanMfaRedisKeys(USER_TYPE, seededUserId);
            try {
                userRepository.deleteById(seededUserId);
            } catch (RuntimeException ignored) {
                // 不阻塞下一个 test —— 后续 test 用 nanoTime 唯一 username 避免冲突
            }
            seededUserId = null;
            seededUsername = null;
            seededPlaintextBackupCodes = null;
        }
    }

    /**
     * 场景 1：剩余降到 ≤2 时，成功响应须带 {@code regenerate_backup_codes_warning=true}。
     *
     * <p>步骤：seed 10 → 服务层直调消耗前 8 个（剩 2）→ 登录拿 pending → HTTP 消费第 9 个 →
     * 断言 remaining=1、warning=true、required=false。
     */
    @Test
    void ninthBackupCode_returnsWarningAtTwoOrLess() throws Exception {
        String rawPassword = "Bkp9thWarn@Pwd-12345";
        seedUserWithMfaAndTenBackupCodes("p-bkp-w-" + System.nanoTime(), rawPassword);

        // 直调消耗前 8 个，DB 剩 2
        for (int i = 0; i < 8; i++) {
            MfaBackupCodeService.BackupCodeConsumption result = mfaBackupCodeService
                .consume(USER_TYPE, seededUserId, seededPlaintextBackupCodes.get(i));
            assertThat(result.consumed()).as("预热第 %d 个 backup code 应被消费", i + 1).isTrue();
        }
        assertThat(mfaBackupCodeService.remaining(USER_TYPE, seededUserId)).as("预热后 DB 应剩 2 个备份码")
            .isEqualTo(2);

        Cookie pending = loginAndGetPendingCookie(rawPassword);
        String sourceIp = resolveStashedSourceIp(pending.getValue());

        // HTTP 消费第 9 个：response remaining=1 → warning=true, required=false
        mockMvc.perform(post(CHALLENGE_PATH).with(csrf()).cookie(pending).with(req -> {
            req.setRemoteAddr(sourceIp);
            return req;
        }).contentType(MediaType.APPLICATION_JSON)
            .content("{\"backupCode\":\"" + seededPlaintextBackupCodes.get(8) + "\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ok"))
            .andExpect(jsonPath("$.result.backup_codes_remaining").value(1))
            .andExpect(jsonPath("$.result.regenerate_backup_codes_warning").value(true))
            .andExpect(jsonPath("$.result.regenerate_backup_codes_required").value(false))
            .andExpect(cookie().maxAge(PENDING_COOKIE, 0));

        // 成功路径会清掉锁计数 + 原子消费 pending key
        assertThat(redisTemplate.hasKey(pendingKey(pending.getValue())))
            .as("成功后 Redis pending key 应被消费").isFalse();
        assertThat(mfaBackupCodeService.remaining(USER_TYPE, seededUserId)).as("HTTP 消费完应剩 1 个备份码")
            .isEqualTo(1);
    }

    /**
     * 场景 2：剩余降到 0 时，成功响应须带 {@code regenerate_backup_codes_required=true}
     * （同时 warning 也 true，因为 0 ≤ 2）。
     */
    @Test
    void tenthBackupCode_returnsRequiredAtZero() throws Exception {
        String rawPassword = "Bkp10thReq@Pwd-12345";
        seedUserWithMfaAndTenBackupCodes("p-bkp-r-" + System.nanoTime(), rawPassword);

        // 直调消耗前 9 个，DB 剩 1
        for (int i = 0; i < 9; i++) {
            MfaBackupCodeService.BackupCodeConsumption result = mfaBackupCodeService
                .consume(USER_TYPE, seededUserId, seededPlaintextBackupCodes.get(i));
            assertThat(result.consumed()).as("预热第 %d 个 backup code 应被消费", i + 1).isTrue();
        }
        assertThat(mfaBackupCodeService.remaining(USER_TYPE, seededUserId)).as("预热后 DB 应剩 1 个备份码")
            .isEqualTo(1);

        Cookie pending = loginAndGetPendingCookie(rawPassword);
        String sourceIp = resolveStashedSourceIp(pending.getValue());

        // HTTP 消费第 10 个：response remaining=0 → 两个 flag 都是 true
        mockMvc.perform(post(CHALLENGE_PATH).with(csrf()).cookie(pending).with(req -> {
            req.setRemoteAddr(sourceIp);
            return req;
        }).contentType(MediaType.APPLICATION_JSON)
            .content("{\"backupCode\":\"" + seededPlaintextBackupCodes.get(9) + "\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ok"))
            .andExpect(jsonPath("$.result.backup_codes_remaining").value(0))
            .andExpect(jsonPath("$.result.regenerate_backup_codes_warning").value(true))
            .andExpect(jsonPath("$.result.regenerate_backup_codes_required").value(true))
            .andExpect(cookie().maxAge(PENDING_COOKIE, 0));

        assertThat(mfaBackupCodeService.remaining(USER_TYPE, seededUserId)).as("HTTP 消费完应剩 0 个备份码")
            .isZero();
    }

    /**
     * 场景 3：同一备份码不能用第二次。第一次 HTTP 提交成功，再用同样的码会被当作不匹配 →
     * 401 invalid_backup_code，且计入失败计数（但不达阈值，本测试不验锁）。
     *
     * <p>注意：第一次成功会清掉同一 subject 的锁定计数器，所以失败计数从 0 重新计。
     */
    @Test
    void replayingAlreadyConsumedBackupCode_isRejected() throws Exception {
        String rawPassword = "BkpReplay@Pwd-12345";
        seedUserWithMfaAndTenBackupCodes("p-bkp-rp-" + System.nanoTime(), rawPassword);

        // 第一次：登录 → 用 code#0 通过挑战
        Cookie firstPending = loginAndGetPendingCookie(rawPassword);
        String firstSourceIp = resolveStashedSourceIp(firstPending.getValue());
        mockMvc.perform(post(CHALLENGE_PATH).with(csrf()).cookie(firstPending).with(req -> {
            req.setRemoteAddr(firstSourceIp);
            return req;
        }).contentType(MediaType.APPLICATION_JSON)
            .content("{\"backupCode\":\"" + seededPlaintextBackupCodes.get(0) + "\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ok"));

        assertThat(mfaBackupCodeService.remaining(USER_TYPE, seededUserId)).as("首次消费完应剩 9 个备份码")
            .isEqualTo(9);

        // 第二次：重新登录拿新 pending（旧 pending 已被原子消费），用同一 code#0 → 401 invalid_backup_code
        Cookie secondPending = loginAndGetPendingCookie(rawPassword);
        String secondSourceIp = resolveStashedSourceIp(secondPending.getValue());
        mockMvc.perform(post(CHALLENGE_PATH).with(csrf()).cookie(secondPending).with(req -> {
            req.setRemoteAddr(secondSourceIp);
            return req;
        }).contentType(MediaType.APPLICATION_JSON)
            .content("{\"backupCode\":\"" + seededPlaintextBackupCodes.get(0) + "\"}"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value("invalid_backup_code"));

        // DB 没有进一步变化（失败路径不写库）
        assertThat(mfaBackupCodeService.remaining(USER_TYPE, seededUserId)).as("替换性消费失败后 DB 剩余数不变")
            .isEqualTo(9);
        // Pending entry 因 IP 校验过 + 失败码不达阈值未被锁，仍留在 Redis（spec.md：失败不消费 pending）
        assertThat(redisTemplate.hasKey(pendingKey(secondPending.getValue())))
            .as("失败路径不应消费 pending entry").isTrue();
    }

    /**
     * Seed 一条已绑 MFA 的 ENABLED user，{@code mfa_enabled=true} + 10 个 backup code，
     * 明文列表存到 {@link #seededPlaintextBackupCodes} 字段，密文 + JSON 持久化到 DB。
     * 复用 {@link PortalMfaChallengeLoginIT#seedUserWithMfa} 的 schema 字段，差异只在
     * 把明文 backup 列表暴露给测试。
     */
    private void seedUserWithMfaAndTenBackupCodes(String username, String rawPassword) {
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
        seededPlaintextBackupCodes = plaintextCodes;
        seededUserId = userRepository.saveAndFlush(user).getId();
        assertThat(seededPlaintextBackupCodes).as("生成器应固定输出 10 个 backup code").hasSize(10);
    }

    /**
     * 走 {@code POST /api/v1/login} 拿 {@code ulp-mfa-pending} cookie。已绑 MFA 的 user 一定
     * 走 CHALLENGE_REQUIRED 分支，断言 {@code status=mfa_required}，返回 cookie。
     */
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

    /**
     * 见 {@link PortalMfaChallengeLoginIT#resolveStashedSourceIp(String)}：MockMvc 默认请求
     * remoteAddr=127.0.0.1 与 stash 时的真实 IP 大概率不同 /24，需要对齐。
     */
    private String resolveStashedSourceIp(String challengeId) {
        return pendingStore.peek(challengeId).map(entry -> entry.getSourceIp()).orElseThrow(
            () -> new IllegalStateException("pending entry missing for challenge " + challengeId));
    }
}
