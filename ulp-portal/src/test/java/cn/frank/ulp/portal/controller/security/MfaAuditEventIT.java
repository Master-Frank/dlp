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

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson2.JSON;

import cn.frank.ulp.audit.entity.AuditEntity;
import cn.frank.ulp.audit.enums.EventStatus;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.audit.repository.AuditRepository;
import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.support.security.authentication.AuthenticationProvider;
import cn.frank.ulp.support.security.authentication.WebAuthenticationDetails;
import cn.frank.ulp.support.security.mfa.MfaPendingAuthenticationStore;
import cn.frank.ulp.support.security.userdetails.Application;
import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.userdetails.UserType;
import cn.frank.ulp.support.testsupport.AbstractMfaIntegrationTest;

import jakarta.servlet.http.Cookie;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Phase 6.10 端到端 IT —— 串起 portal 端完整的 MFA 业务流，断言每个 outcome 都按 spec 落到了
 * {@code ulp_audit} 表里，且 event code 字符串与 details 字段符合 Phase 6.1-6.7 的契约。
 *
 * <p>本 IT 不是 controller 行为测试（那些场景分散在 {@link
 * cn.frank.ulp.portal.controller.security.MfaUserUnbindFlowIT}、{@link PortalMfaChallengeLoginIT}、
 * {@link MfaLockoutIT} 等）；它专门给"audit 模块对外承诺"上钉子：当我以后改 controller 实现细节，
 * 只要 audit 行还在、event_param 字段还在、event code 还在，下游 ulp-audit 消费者就不会断。
 *
 * <h2>流程一棵走完：</h2>
 * <ol>
 *   <li>{@code POST /api/v1/mfa/bind/prepare}     → {@code PREPARE_BIND_MFA} SUCCESS</li>
 *   <li>{@code POST /api/v1/mfa/bind/confirm}     → {@code BIND_MFA}         SUCCESS</li>
 *   <li>{@code POST /api/v1/login}（已绑）→ pending cookie</li>
 *   <li>{@code POST /api/v1/mfa/challenge}（错码）→ {@code MFA_VERIFY_FAILURE} FAIL
 *       + {@code event_param} 含 {@code "failure_reason":"invalid_otp"}（Phase 6.6 契约）</li>
 *   <li>{@code POST /api/v1/mfa/challenge}（对码）→ {@code MFA_VERIFY_SUCCESS} SUCCESS</li>
 *   <li>{@code POST /api/v1/mfa/unbind}（对码）   → {@code UNBIND_MFA}         SUCCESS
 *       —— 同时验证 Phase 6.2 typo fix：event code 必须是 {@code unbind_mfa} 而不是 {@code unbind_maf}</li>
 * </ol>
 *
 * <h2>设计要点：</h2>
 * <ul>
 *   <li><b>{@code @Transactional(NOT_SUPPORTED)}</b>：跟 {@link PortalMfaChallengeLoginIT} 同款原因 ——
 *       {@code UserServiceImpl#findByUsernameOrPhoneOrEmail} 用 {@code CompletableFuture.supplyAsync}
 *       做三路查询，异步线程脱离测试事务上下文，看不到事务内未 commit 的 seed INSERT。代价是
 *       seed/audit 都真写库，需 {@code @AfterEach} 显式清账号 + 软删 audit 行 + 清 Redis。</li>
 *   <li><b>Awaitility polling</b>：{@link cn.frank.ulp.audit.event.AuditEventListener} 标了
 *       {@code @Async}，行不在请求线程里立即出现。轮询 {@code AuditRepository}（{@code @SoftDelete}
 *       自动过滤已删行）+ {@code JpaSpecificationExecutor} 按 {@code event_type / actor_id / event_status}
 *       三键定位，每轮 100ms 最长 5s —— 异步落库通常 &lt;500ms。</li>
 *   <li><b>actor 绑定到 seededUserId</b>：所有 6 条 audit 行 actorId 都是这个 user 的 id，
 *       {@code @AfterEach} 用此键扫一遍软删，避免污染其他 IT。</li>
 *   <li><b>独立两个 deployable 各一份 IT</b>：portal 这份覆盖用户侧 5 个事件；console 那份
 *       ({@code cn.frank.ulp.console.controller.security.MfaAuditEventIT}) 覆盖 admin 端
 *       {@code ORG_MFA_POLICY_CHANGED} 仅在值变化时发的契约。Spring Boot 一个 {@code @SpringBootTest}
 *       一个 app context，跨 deployable 必须分开 IT。</li>
 * </ul>
 *
 * <h2>actor 的来源：</h2>
 * bind/prepare、bind/confirm、unbind 路径靠 {@code SecurityContextHolder}（mock auth 注入），
 * challenge 失败路径靠 {@code MfaChallengeService.peekPendingAuthentication}（成功路径靠登录后
 * SecurityContext 内的 Authentication）。三条来源最终都解析到同一个 {@code seededUserId}，
 * 这正是审计能"按用户回溯"的前提，本 IT 顺手把这个不变量也钉死。
 */
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class MfaAuditEventIT extends AbstractMfaIntegrationTest {

    private static final String           LOGIN_PATH        = "/api/v1/login";
    private static final String           BIND_PREPARE_PATH = "/api/v1/mfa/bind/prepare";
    private static final String           BIND_CONFIRM_PATH = "/api/v1/mfa/bind/confirm";
    private static final String           UNBIND_PATH       = "/api/v1/mfa/unbind";
    private static final String           CHALLENGE_PATH    = "/api/v1/mfa/challenge";
    private static final String           PENDING_COOKIE    = "ulp-mfa-pending";

    @Autowired
    private UserRepository                userRepository;

    @Autowired
    private AuditRepository               auditRepository;

    @Autowired
    private MfaPendingAuthenticationStore pendingStore;

    private String                        seededUserId;
    private String                        seededUsername;

    @AfterEach
    void cleanup() {
        if (seededUserId != null) {
            // 软删 audit 行 —— AuditEntity 标 @SoftDelete，delete() 写 is_deleted=1，
            // 后续 IT 的 findAll(spec) 自动过滤。比 @Modifying 自定 query 更不易翻车。
            List<AuditEntity> rows = auditRepository.findAll(
                (root, q, cb) -> cb.equal(root.get(AuditEntity.ACTOR_ID_FIELD_NAME), seededUserId));
            rows.forEach(auditRepository::delete);

            cleanMfaRedisKeys("user", seededUserId);

            try {
                userRepository.deleteById(seededUserId);
            } catch (RuntimeException ignored) {
                // 删失败不阻塞下一个 test —— 下一个 test 用 nanoTime 唯一 username 避免冲突
            }
            seededUserId = null;
            seededUsername = null;
        }
    }

    /**
     * 唯一测试方法：spec.md "完整 bind → challenge fail → challenge success → unbind 流程"。
     * 六个 outcome 各产 1 行 audit，{@code event_param} 含 spec 约定字段，event code 走 typo-fixed 字符串。
     */
    @Test
    void endToEndMfaFlow_emitsExpectedAuditTrail() throws Exception {
        String rawPassword = "Audit@Pwd-12345";
        // ulp_user.email_ VARCHAR(50)；username 必须 ≤38 字符以适配 "<username>@example.com"
        seedUserWithoutMfa("p-audit-" + System.nanoTime(), rawPassword);

        // Step 1: bind/prepare → PREPARE_BIND_MFA
        MvcResult prepResult = mockMvc
            .perform(post(BIND_PREPARE_PATH)
                .with(authentication(mockUserAuth(seededUserId, seededUsername))).with(csrf()))
            .andExpect(status().isOk()).andReturn();
        String prepBody = prepResult.getResponse().getContentAsString();
        String secretBase32 = JSON.parseObject(prepBody).getJSONObject("result")
            .getString("secretBase32");
        assertThat(secretBase32).as("bind/prepare 必须返回 secretBase32 供前端 + 测试自算 TOTP").isNotBlank();

        AuditEntity prepareRow = awaitAuditRow(EventType.PREPARE_BIND_MFA, seededUserId,
            EventStatus.SUCCESS);
        assertThat(prepareRow.getEventType().getCode()).as("Phase 6 event code 字符串契约")
            .isEqualTo("ulp:event:account:prepare_bind_mfa");

        // Step 2: bind/confirm (correct OTP) → BIND_MFA
        String confirmOtp = totp(secretBase32);
        mockMvc.perform(post(BIND_CONFIRM_PATH)
            .with(authentication(mockUserAuth(seededUserId, seededUsername))).with(csrf())
            .contentType(MediaType.APPLICATION_JSON).content("{\"otp\":\"" + confirmOtp + "\"}"))
            .andExpect(status().isOk());

        AuditEntity bindRow = awaitAuditRow(EventType.BIND_MFA, seededUserId, EventStatus.SUCCESS);
        assertThat(bindRow.getEventType().getCode()).as("Phase 6.1 typo fix: bind_maf → bind_mfa")
            .isEqualTo("ulp:event:account:bind_mfa");

        // Step 3: login (now MFA enabled) → pending cookie
        MvcResult loginResult = mockMvc
            .perform(post(LOGIN_PATH).with(csrf()).param("username", seededUsername)
                .param("password", rawPassword))
            .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("mfa_required"))
            .andReturn();
        Cookie pending = loginResult.getResponse().getCookie(PENDING_COOKIE);
        assertThat(pending).as("已绑 user 登录必下发 pending cookie").isNotNull();
        String sourceIp = pendingStore.peek(pending.getValue()).orElseThrow().getSourceIp();

        // Step 4: challenge (wrong OTP) → MFA_VERIFY_FAILURE failure_reason=invalid_otp
        String wrongOtp = nudgeOtp(totp(secretBase32));
        mockMvc.perform(post(CHALLENGE_PATH).with(csrf()).cookie(pending).with(req -> {
            req.setRemoteAddr(sourceIp);
            return req;
        }).contentType(MediaType.APPLICATION_JSON).content("{\"code\":\"" + wrongOtp + "\"}"))
            .andExpect(status().isUnauthorized());

        AuditEntity failureRow = awaitAuditRow(EventType.MFA_VERIFY_FAILURE, seededUserId,
            EventStatus.FAIL);
        assertThat(failureRow.getEventParam())
            .as("Phase 6.6 契约：MFA_VERIFY_FAILURE.event_param 必须含 failure_reason=invalid_otp")
            .contains("\"failure_reason\":\"invalid_otp\"");

        // Step 5: challenge (correct OTP) → MFA_VERIFY_SUCCESS
        String validOtp = totp(secretBase32);
        mockMvc.perform(post(CHALLENGE_PATH).with(csrf()).cookie(pending).with(req -> {
            req.setRemoteAddr(sourceIp);
            return req;
        }).contentType(MediaType.APPLICATION_JSON).content("{\"code\":\"" + validOtp + "\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ok"));

        awaitAuditRow(EventType.MFA_VERIFY_SUCCESS, seededUserId, EventStatus.SUCCESS);

        // Step 6: unbind (correct OTP) → UNBIND_MFA
        // OTP 在同 30s 窗口里多次 verify 仍合法（无 replay 层），fresh compute 仅为时序保险
        String unbindOtp = totp(secretBase32);
        mockMvc.perform(
            post(UNBIND_PATH).with(authentication(mockUserAuth(seededUserId, seededUsername)))
                .with(csrf()).contentType(MediaType.APPLICATION_JSON)
                .content("{\"currentOtp\":\"" + unbindOtp + "\"}"))
            .andExpect(status().isOk());

        AuditEntity unbindRow = awaitAuditRow(EventType.UNBIND_MFA, seededUserId,
            EventStatus.SUCCESS);
        assertThat(unbindRow.getEventType().getCode())
            .as("Phase 6.2 typo fix: unbind_maf → unbind_mfa")
            .isEqualTo("ulp:event:account:unbind_mfa");
    }

    /**
     * 轮询直到匹配的 audit 行落库；每轮 100ms，最长 5s。返回首条匹配行供 caller 进一步断言
     * {@code event_param} / {@code event_content} 等字段。
     */
    private AuditEntity awaitAuditRow(EventType type, String actorId, EventStatus status) {
        await().atMost(5, SECONDS).pollInterval(100, MILLISECONDS).untilAsserted(() -> {
            List<AuditEntity> rows = findAuditRows(type, actorId, status);
            assertThat(rows).as("audit row for %s / actor=%s / status=%s", type, actorId, status)
                .isNotEmpty();
        });
        return findAuditRows(type, actorId, status).get(0);
    }

    private List<AuditEntity> findAuditRows(EventType type, String actorId, EventStatus status) {
        return auditRepository.findAll(
            (root, q, cb) -> cb.and(cb.equal(root.get(AuditEntity.EVENT_TYPE_FIELD_NAME), type),
                cb.equal(root.get(AuditEntity.ACTOR_ID_FIELD_NAME), actorId),
                cb.equal(root.get("eventStatus"), status)));
    }

    private String totp(String secretBase32) {
        return computeTotp(secretBase32);
    }

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
     * mock 一个已认证的 portal user。结构与 {@link
     * cn.frank.ulp.portal.controller.security.MfaUserUnbindFlowIT}、{@link
     * cn.frank.ulp.console.controller.security.OrgMfaPolicyControllerIT} 同款 ——
     * {@code WebAuthenticationDetails} 必须有 {@code AuthenticationProvider}，否则
     * {@code AuditEventPublish.getActor()} 走 {@code .getAuthenticationProvider().getType()} NPE。
     */
    private static UsernamePasswordAuthenticationToken mockUserAuth(String userId,
                                                                    String username) {
        UserDetails u = new UserDetails(userId, username, UserType.USER, true, true, true, true,
            AuthorityUtils.NO_AUTHORITIES);
        Set<Application> apps = new HashSet<>();
        u.setApplications(apps);
        u.setUpdateTime(LocalDateTime.now());
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken
            .authenticated(u, null, u.getAuthorities());
        WebAuthenticationDetails details = new WebAuthenticationDetails("127.0.0.1", "test-session",
            null, null, new AuthenticationProvider("portal", "test"), LocalDateTime.now());
        token.setDetails(details);
        return token;
    }
}
