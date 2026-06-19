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
import com.alibaba.fastjson2.JSONObject;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Phase 4.5 端到端 IT —— 走真实 {@code POST /api/v1/login} 链路验证 Portal MFA 挑战门（仅自愿场景，
 * 组织强制路径在 {@code PortalOrgMfaEnforcementIT} 单独覆盖）。
 *
 * <ol>
 *   <li>未绑定 MFA 且无组织强制的 user 登录直接放行（无 mfa_required，无 pending cookie）</li>
 *   <li>已绑定 MFA 的 user 登录返回 {@code mfa_required} + 写 {@code ulp-mfa-pending} cookie</li>
 *   <li>提交正确 TOTP 后 {@code /api/v1/mfa/challenge} 返回 200 {@code status=ok}，pending cookie 被清除</li>
 *   <li>连错 5 次：前 4 次 401 {@code invalid_otp}，第 5 次 423 {@code locked_out} + {@code Retry-After}</li>
 * </ol>
 *
 * <p><b>事务模式：</b> {@code @Transactional(propagation = NOT_SUPPORTED)} —— 与 console 同款理由：
 * {@code UserServiceImpl#findByUsernameOrPhoneOrEmail} 用 {@code CompletableFuture.supplyAsync} 在独立
 * 线程做三路查询，异步线程脱离测试事务上下文，看不到测试事务内 uncommitted 的 seed INSERT。
 * 代价是 seed 真写库，需 {@code @AfterEach} 手动清账号 + Redis 计数器。
 *
 * <p><b>密码 seed：</b> 用 {@code {bcrypt}}+BCrypt 真编码。Portal 链路 DAP 经
 * {@code DelegatingPasswordEncoder} 路由到 BCrypt verifier 校验通过；登录成功后还会触发 rehash 到
 * {@code {argon2}}（{@link cn.frank.ulp.portal.security.PasswordUpgradeIT} 已覆盖），本 IT 不关心
 * rehash 结果，只关心 MFA 分支行为。
 *
 * <p><b>未挂组织：</b> seed 的 user 不挂任何组织，{@code OrgMfaPolicyService.isUserEnforced} 自然返
 * false，因此 {@code MfaTriggerStrategy} 走 DIRECT_LOGIN（未绑）或 CHALLENGE_REQUIRED（已绑），
 * SETUP_REQUIRED 分支不会触发。
 */
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class PortalMfaChallengeLoginIT extends AbstractMfaIntegrationTest {

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
    /** with-MFA 场景 seed 时记录的 base32 secret，给后续 TOTP 计算用。 */
    private String                        seededUserSecret;

    @AfterEach
    void cleanup() {
        if (seededUserId != null) {
            cleanMfaRedisKeys("user", seededUserId);
            try {
                userRepository.deleteById(seededUserId);
            } catch (RuntimeException ignored) {
                // 删失败不阻塞下一个 test —— 下一个 test 用 nanoTime 唯一 username 避免冲突
            }
            seededUserId = null;
            seededUsername = null;
            seededUserSecret = null;
        }
    }

    /**
     * 场景 1：未绑 MFA 且不在强制组织的 user 登录走直登链路。
     * 断言：200；响应体不含 {@code status=mfa_required}；不下发 pending cookie。
     */
    @Test
    void userWithoutMfa_loginDoesNotEmitMfaRequired() throws Exception {
        String rawPassword = "DirectLogin@Pwd-12345";
        // ulp_user.email_ 是 VARCHAR(50)，email = "<username>@example.com"，username 必须 ≤ 38 字符
        seedUserWithoutMfa("p-mfa-no-" + System.nanoTime(), rawPassword);

        MvcResult result = mockMvc
            .perform(post(LOGIN_PATH).with(csrf()).param("username", seededUsername)
                .param("password", rawPassword))
            .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
            .andExpect(cookie().doesNotExist(PENDING_COOKIE)).andReturn();

        JSONObject body = JSON.parseObject(result.getResponse().getContentAsString());
        assertThat(body.getString("status")).as("直登响应不应带 status=mfa_required")
            .isNotEqualTo("mfa_required");
    }

    /**
     * 场景 2：绑定 MFA 的 user 登录返回 {@code mfa_required} 占位响应 + pending cookie。
     * 断言：200；{@code $.status == "mfa_required"}；{@code $.result.challenge_id} 非空；
     * pending cookie 存在、HttpOnly、Path=/、5 分钟 TTL；Redis 里同名 key 已写入。
     */
    @Test
    void userWithMfa_loginEmitsMfaRequiredAndCookie() throws Exception {
        String rawPassword = "MfaRequired@Pwd-12345";
        seedUserWithMfa("p-mfa-on-" + System.nanoTime(), rawPassword);

        MvcResult result = mockMvc
            .perform(post(LOGIN_PATH).with(csrf()).param("username", seededUsername)
                .param("password", rawPassword))
            .andExpect(status().isOk())
            // MFA-required 路径返回 success=false（站在前端视角"登录尚未完成"，避免被当成已登录态去拉受保护接口）。
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.status").value("mfa_required"))
            .andExpect(jsonPath("$.result.mfa_required").value(true))
            .andExpect(jsonPath("$.result.challenge_id").exists())
            .andExpect(cookie().exists(PENDING_COOKIE))
            .andExpect(cookie().httpOnly(PENDING_COOKIE, true))
            .andExpect(cookie().path(PENDING_COOKIE, "/"))
            .andExpect(cookie().maxAge(PENDING_COOKIE, 300)).andReturn();

        Cookie pending = result.getResponse().getCookie(PENDING_COOKIE);
        assertThat(pending).as("pending cookie 应存在").isNotNull();
        assertThat(pending.getValue()).as("pending cookie 值非空").isNotBlank();

        JSONObject body = JSON.parseObject(result.getResponse().getContentAsString());
        String challengeIdFromBody = body.getJSONObject("result").getString("challenge_id");
        assertThat(challengeIdFromBody).as("body 中 challenge_id 与 cookie 值一致")
            .isEqualTo(pending.getValue());

        // Redis pending 落地
        assertThat(redisTemplate.hasKey(pendingKey(pending.getValue())))
            .as("Redis pending 已写入").isTrue();
    }

    /**
     * 场景 3：登录拿到 pending cookie 后用正确 TOTP 完成挑战。
     * 断言：challenge 端点 200；{@code $.status == "ok"}；pending cookie 被服务端清除（MaxAge=0）；
     * Redis pending key 已被 GETDEL 原子消费。
     */
    @Test
    void challengeWithValidOtp_committsAuthentication() throws Exception {
        String rawPassword = "ChallengeOk@Pwd-12345";
        seedUserWithMfa("p-mfa-ok-" + System.nanoTime(), rawPassword);

        // Step 1：登录拿 pending cookie
        MvcResult loginResult = mockMvc
            .perform(post(LOGIN_PATH).with(csrf()).param("username", seededUsername)
                .param("password", rawPassword))
            .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("mfa_required"))
            .andReturn();
        Cookie pending = loginResult.getResponse().getCookie(PENDING_COOKIE);
        assertThat(pending).as("登录后必须下发 pending cookie").isNotNull();
        String sourceIp = resolveStashedSourceIp(pending.getValue());

        // Step 2：本窗口正确 TOTP
        String otp = computeTotp(seededUserSecret);

        // Step 3：提交挑战，应 200 ok 并清除 cookie
        mockMvc.perform(post(CHALLENGE_PATH).with(csrf()).cookie(pending).with(req -> {
            req.setRemoteAddr(sourceIp);
            return req;
        }).contentType(MediaType.APPLICATION_JSON).content("{\"code\":\"" + otp + "\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ok"))
            .andExpect(cookie().maxAge(PENDING_COOKIE, 0));

        // Redis pending key 已被 GETDEL 原子消费
        assertThat(redisTemplate.hasKey(pendingKey(pending.getValue())))
            .as("成功后 Redis pending key 应被消费").isFalse();
    }

    /**
     * 场景 4：连错 5 次触发锁定。前 4 次 401 invalid_otp；第 5 次 423 locked_out + Retry-After。
     *
     * <p>{@code MfaLockoutService.recordFailure} 是 post-increment 返回，阈值判断
     * {@code count >= threshold(5)}：第 5 次失败 → 计数 5 → 进入 LOCKED_OUT 分支。
     * Pending entry 在 LOCKED_OUT 路径上故意不消费（spec.md Phase 3 锁窗口行为），
     * 但本测试不验证 cookie 状态，只验证 HTTP 状态码与 Retry-After 头。
     */
    @Test
    void fiveWrongOtps_finalAttemptReturnsLockedWithRetryAfter() throws Exception {
        String rawPassword = "LockoutPath@Pwd-12345";
        seedUserWithMfa("p-mfa-lk-" + System.nanoTime(), rawPassword);

        MvcResult loginResult = mockMvc
            .perform(post(LOGIN_PATH).with(csrf()).param("username", seededUsername)
                .param("password", rawPassword))
            .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("mfa_required"))
            .andReturn();
        Cookie pending = loginResult.getResponse().getCookie(PENDING_COOKIE);
        assertThat(pending).as("登录后必须下发 pending cookie").isNotNull();
        String sourceIp = resolveStashedSourceIp(pending.getValue());

        // 故意造一个错码：当前窗口正确 TOTP 最后一位 +1 mod 10，保证 6 位不命中
        String wrongOtp = nudgeOtp(computeTotp(seededUserSecret));

        // 前 4 次 → 401 invalid_otp，counter 累到 4
        for (int i = 1; i <= 4; i++) {
            mockMvc.perform(post(CHALLENGE_PATH).with(csrf()).cookie(pending).with(req -> {
                req.setRemoteAddr(sourceIp);
                return req;
            }).contentType(MediaType.APPLICATION_JSON).content("{\"code\":\"" + wrongOtp + "\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("invalid_otp"));
        }

        // 第 5 次 → 423 locked_out + Retry-After
        mockMvc.perform(post(CHALLENGE_PATH).with(csrf()).cookie(pending).with(req -> {
            req.setRemoteAddr(sourceIp);
            return req;
        }).contentType(MediaType.APPLICATION_JSON).content("{\"code\":\"" + wrongOtp + "\"}"))
            .andExpect(status().isLocked()).andExpect(jsonPath("$.status").value("locked_out"))
            .andExpect(header().exists(HttpHeaders.RETRY_AFTER));
    }

    /**
     * Seed 一条未绑 MFA 的 ENABLED user，{@code mfa_enabled=false}，不挂任何组织。
     * 密码用 {@code {bcrypt}}+BCrypt 真编码，登录链路 DelegatingPasswordEncoder 能校验通过。
     */
    private void seedUserWithoutMfa(String username, String rawPassword) {
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
        user.setMfaEnabled(Boolean.FALSE);
        seededUsername = username;
        seededUserId = userRepository.saveAndFlush(user).getId();
    }

    /**
     * Seed 一条已绑 MFA 的 ENABLED user，{@code mfa_enabled=true}，不挂任何组织。
     * 生成的明文 secret 同步存到 {@link #seededUserSecret} 字段，给 TOTP 计算用。
     */
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

    /**
     * 读出 stash 在 Redis 里的 sourceIp —— 真生产里
     * {@link cn.frank.ulp.support.security.mfa.MfaAwareAuthenticationSuccessHandler#resolveSourceIp}
     * 走 {@code WebAuthenticationDetails#getGeoLocation().getIp()} 取真机 IP，而 MockMvc 默认的
     * challenge 请求 remoteAddr 是 127.0.0.1，{@code sameIpv4Subnet /24} 校验会 fail。
     * 解：从 pendingStore peek 出 stash 时记录的 sourceIp，挑战请求用 {@code req.setRemoteAddr(sourceIp)} 对齐
     * （只是把"同一台机器同一次登录"还原成真实状态，不是绕过安全检查）。
     */
    private String resolveStashedSourceIp(String challengeId) {
        return pendingStore.peek(challengeId).map(entry -> entry.getSourceIp()).orElseThrow(
            () -> new IllegalStateException("pending entry missing for challenge " + challengeId));
    }
}
