/*
 * ulp-console - United Login Platform
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
package cn.frank.ulp.console.controller.security;

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
import org.springframework.test.context.ActiveProfiles;

import cn.frank.ulp.audit.entity.AuditEntity;
import cn.frank.ulp.audit.enums.EventStatus;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.audit.repository.AuditRepository;
import cn.frank.ulp.common.entity.account.OrganizationEntity;
import cn.frank.ulp.common.entity.setting.AdministratorEntity;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.common.enums.account.OrganizationType;
import cn.frank.ulp.common.repository.account.OrganizationRepository;
import cn.frank.ulp.common.repository.setting.AdministratorRepository;
import cn.frank.ulp.support.security.authentication.AuthenticationProvider;
import cn.frank.ulp.support.security.authentication.WebAuthenticationDetails;
import cn.frank.ulp.support.security.userdetails.Application;
import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.userdetails.UserType;
import cn.frank.ulp.support.testsupport.AbstractMfaIntegrationTest;
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
 * Phase 6.10 端到端 IT —— console 侧覆盖 admin 切 org {@code mfa_enforced} 强制位的审计契约：
 * <ul>
 *   <li>值变化（OFF→ON）MUST 落 1 行 {@code ORG_MFA_POLICY_CHANGED} SUCCESS，
 *       {@code event_param} 含 spec 约定的 5 个 key：{@code org_id} / {@code org_name} /
 *       {@code old_value} / {@code new_value} / {@code actor_admin_id}（Phase 6.7 契约）</li>
 *   <li>重复值（ON→ON）MUST 不落任何 {@code ORG_MFA_POLICY_CHANGED} 审计行（Phase 6.4 spec：
 *       "ORG_MFA_POLICY_CHANGED 仅在值变化时发"，避免噪声污染下游观测/告警）</li>
 * </ul>
 *
 * <p>portal 那边的端到端 user-flow IT（{@link
 * cn.frank.ulp.portal.controller.security.MfaAuditEventIT}）覆盖 bind / challenge / unbind 5 事件，
 * 本 IT 专攻 admin 端 1 事件 —— 两份 IT 拆开是因为 Spring Boot 一个 {@code @SpringBootTest} 一个 app
 * context，console 跟 portal 不能共享，跨 deployable 必须各写一份。
 *
 * <h2>负向断言："不发审计" 怎么测：</h2>
 * 异步监听器没接到 publish 当然就不会落库，但被动等"什么都没发生"很难给一个确定的等待时长。
 * 这里的做法：(1) 测试启动前在 {@code @AfterEach} 用 actorId 做基线清理；(2) 触发可能发审计的
 * 请求；(3) 给 listener {@code Thread.sleep(1500ms)} 的反应窗（{@code AuditEventListener} 是
 * {@code @Async}，单线程线程池下平均 &lt;500ms 落库，1.5s 留 3 倍冗余）；(4) 按 {@code eventType=ORG_MFA_POLICY_CHANGED}
 * + {@code actorId=adminId} 查 audit 表，断言 size=0。注意 actorId 用 admin 不用 org（actor 是
 * 操作主体；target 才是 org），筛选维度跟着 spec 走。
 *
 * <h2>事务模式说明：</h2>
 * 不像 portal IT 那样需要 {@code @Transactional(NOT_SUPPORTED)} —— 本 IT 不走 login flow，
 * 也不触碰 {@code UserServiceImpl#findByUsernameOrPhoneOrEmail} 那条 {@code CompletableFuture}
 * 异步链。默认事务回滚即可，seed / audit 行都在测试方法事务里，{@code @AfterEach} 显式清是
 * 为防 listener 异步线程在事务外提交的 audit 行跨方法残留。
 *
 * <p>但 {@code AuditEventListener} 是 {@code @Async}，行不在请求线程的事务里，所以**仍需手动清**
 * audit 行，否则上一 test 的 audit row 会被下一 test 看见。
 */
@ActiveProfiles("test")
class MfaAuditEventIT extends AbstractMfaIntegrationTest {

    private static final String     POLICY_PATH = "/api/v1/admin/organizations/";

    @Autowired
    private OrganizationRepository  organizationRepository;

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private AuditRepository         auditRepository;

    private String                  seededOrgId;
    private String                  seededAdminId;

    @AfterEach
    void cleanup() {
        if (seededAdminId != null) {
            // 软删 audit 行 —— AuditEntity 标 @SoftDelete，后续 IT 的 findAll(spec) 自动过滤
            List<AuditEntity> rows = auditRepository.findAll((root, q, cb) -> cb
                .equal(root.get(AuditEntity.ACTOR_ID_FIELD_NAME), seededAdminId));
            rows.forEach(auditRepository::delete);
            try {
                administratorRepository.deleteById(seededAdminId);
            } catch (RuntimeException ignored) {
                // 不阻塞下一个 test
            }
            seededAdminId = null;
        }
        if (seededOrgId != null) {
            try {
                organizationRepository.deleteById(seededOrgId);
            } catch (RuntimeException ignored) {
                // 不阻塞下一个 test
            }
            seededOrgId = null;
        }
    }

    /**
     * 场景 1：admin 把 org.mfa_enforced 从 false 翻到 true。
     * 锚 1：response body {@code changed=true}；锚 2：audit 表落 1 行 ORG_MFA_POLICY_CHANGED；
     * 锚 3：event code 字符串走 spec；锚 4：event_param 含 5 个 spec key 与正确旧/新值。
     */
    @Test
    void orgPolicyFlipOnByAdmin_emitsOneOrgPolicyChangedRow() throws Exception {
        seededOrgId = seedOrg("audit-flip-on-" + System.nanoTime(), Boolean.FALSE);
        seededAdminId = seedAdmin("audit-actor-on-" + System.nanoTime());

        mockMvc
            .perform(post(POLICY_PATH + seededOrgId + "/mfa-policy")
                .with(authentication(mockAdminAuth(seededAdminId, "audit-actor-on"))).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content("{\"mfaEnforced\":true}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.result.changed").value(true));

        AuditEntity row = awaitAuditRow(EventType.ORG_MFA_POLICY_CHANGED, seededAdminId,
            EventStatus.SUCCESS);
        assertThat(row.getEventType().getCode()).as("Phase 6 event code 字符串契约")
            .isEqualTo("ulp:event:account:org_mfa_policy_changed");

        String eventParam = row.getEventParam();
        assertThat(eventParam).as("event_param 必须是 JSON 非空").isNotBlank();
        // Phase 6.7 契约：5 个 key 全须出现，old=false→new=true
        assertThat(eventParam).as("event_param 含 org_id").contains("\"org_id\"")
            .contains(seededOrgId);
        assertThat(eventParam).as("event_param 含 org_name").contains("\"org_name\"");
        assertThat(eventParam).as("event_param 含 old_value=false").contains("\"old_value\":false");
        assertThat(eventParam).as("event_param 含 new_value=true").contains("\"new_value\":true");
        assertThat(eventParam).as("event_param 含 actor_admin_id").contains("\"actor_admin_id\"")
            .contains(seededAdminId);
    }

    /**
     * 场景 2：admin 对已开启的 org 再写一次 true（值未变）。
     * controller 在 {@code changed} 分支外不发审计 → 表中 ORG_MFA_POLICY_CHANGED 行数应 = 0。
     * 验证 spec："仅在值变化时发"。
     */
    @Test
    void orgPolicyRepeatSameValueByAdmin_emitsNoAuditRow() throws Exception {
        seededOrgId = seedOrg("audit-noop-" + System.nanoTime(), Boolean.TRUE);
        seededAdminId = seedAdmin("audit-actor-noop-" + System.nanoTime());

        mockMvc
            .perform(post(POLICY_PATH + seededOrgId + "/mfa-policy")
                .with(authentication(mockAdminAuth(seededAdminId, "audit-actor-noop"))).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content("{\"mfaEnforced\":true}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.result.changed").value(false));

        // listener @Async 反应窗：单线程默认 pool 下 publish→落库 <500ms，留 3× 冗余
        Thread.sleep(1500);

        List<AuditEntity> rows = findAuditRows(EventType.ORG_MFA_POLICY_CHANGED, seededAdminId,
            EventStatus.SUCCESS);
        assertThat(rows).as("重复值写入 MUST 不落 ORG_MFA_POLICY_CHANGED 审计行").isEmpty();
    }

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

    private String seedOrg(String code, Boolean mfaEnforced) {
        OrganizationEntity org = new OrganizationEntity();
        org.setName("Org " + code);
        org.setCode(code);
        org.setType(OrganizationType.GROUP);
        org.setLeaf(Boolean.TRUE);
        org.setEnabled(Boolean.TRUE);
        org.setMfaEnforced(mfaEnforced);
        org.setDataOrigin("input");
        org.setPath("/" + code);
        org.setDisplayPath("/Org " + code);
        return organizationRepository.saveAndFlush(org).getId();
    }

    private String seedAdmin(String username) {
        AdministratorEntity admin = new AdministratorEntity();
        admin.setUsername(username);
        admin.setPassword("{bcrypt}$2a$10$abcdefghijklmnopqrstuvABCDEFGHIJKLMNOPQRSTUVWXYZ12345");
        admin.setStatus(UserStatus.ENABLED);
        admin.setEmail(username + "@example.com");
        admin.setEmailVerified(Boolean.TRUE);
        admin.setPhoneVerified(Boolean.FALSE);
        admin.setNeedChangePassword(Boolean.FALSE);
        admin.setLastUpdatePasswordTime(LocalDateTime.now());
        admin.setMfaEnabled(Boolean.FALSE);
        return administratorRepository.saveAndFlush(admin).getId();
    }

    /**
     * mock 一个已认证的 admin。{@code WebAuthenticationDetails} 必须带
     * {@code AuthenticationProvider}，否则 {@code AuditEventPublish.getActor()} 走
     * {@code .getAuthenticationProvider().getType()} NPE —— 与
     * {@link OrgMfaPolicyControllerIT#mockAdminAuth(String, String)} 同款理由。
     */
    private static UsernamePasswordAuthenticationToken mockAdminAuth(String adminId,
                                                                     String adminUsername) {
        UserDetails u = new UserDetails(adminId, adminUsername, UserType.ADMIN, true, true, true,
            true, AuthorityUtils.NO_AUTHORITIES);
        Set<Application> apps = new HashSet<>();
        u.setApplications(apps);
        u.setUpdateTime(LocalDateTime.now());
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken
            .authenticated(u, null, u.getAuthorities());
        WebAuthenticationDetails details = new WebAuthenticationDetails("127.0.0.1", "test-session",
            null, null, new AuthenticationProvider("console", "test"), LocalDateTime.now());
        token.setDetails(details);
        return token;
    }
}
