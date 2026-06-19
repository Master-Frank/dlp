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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import cn.frank.ulp.common.entity.account.OrganizationEntity;
import cn.frank.ulp.common.entity.account.OrganizationMemberEntity;
import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.common.enums.account.OrganizationType;
import cn.frank.ulp.common.repository.account.OrganizationMemberRepository;
import cn.frank.ulp.common.repository.account.OrganizationRepository;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.common.security.mfa.OrgMfaPolicyService;
import cn.frank.ulp.support.security.authentication.AuthenticationProvider;
import cn.frank.ulp.support.security.authentication.WebAuthenticationDetails;
import cn.frank.ulp.support.security.mfa.MfaBackupCodeGenerator;
import cn.frank.ulp.support.security.mfa.MfaSecretCipher;
import cn.frank.ulp.support.security.mfa.MfaSecretGenerator;
import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.userdetails.UserType;
import cn.frank.ulp.support.testsupport.AbstractMfaIntegrationTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Phase 4.6 端到端 IT —— 验证 portal 侧组织级 MFA 强制门完整闭环：
 *
 * <ol>
 *   <li>{@link #enforcedUserFirstLogin_returnsMfaSetupRequired()}
 *       — 未绑 user + 所在组织 {@code mfa_enforced=true} 走真实 {@code POST /api/v1/login}：
 *       响应 200 + {@code status="mfa_setup_required"} + {@code result.reason="org_policy"} +
 *       {@code result.setup_path="/mfa/setup"}（spec 原写 302，Phase 4.2 落地为 200 JSON：
 *       Authentication 已 commit 到 session，前端按 status 跳 {@code /mfa/setup}）；
 *       <b>不</b> 下发 {@code ulp-mfa-pending} cookie（那是 challenge 路径专用）。</li>
 *
 *   <li>{@link #enforcedUserAccessingProtectedPath_returns403MfaSetupRequired()}
 *       — 未绑 user + 强制组织已认证，访问受保护端点
 *       {@code GET /api/v1/session/current_user} 被 {@link
 *       cn.frank.ulp.portal.security.mfa.OrgMfaEnforcementFilter} 拦：
 *       403 + body {@code {"error":"mfa_setup_required","reason":"org_policy"}}。</li>
 *
 *   <li>{@link #boundUserUnderEnforcement_isNotBlocked()}
 *       — 已绑 user（{@code mfa_enabled=true} + cipher 写入）+ 同一强制组织 → filter
 *       的 {@code loadActiveCipher != null} 分支放过，访问受保护端点不再 403。
 *       验证「完成绑定后访问正常」语义。</li>
 *
 *   <li>{@link #unenforcedOrg_doesNotBlockUnboundUser()}
 *       — 未绑 user + 所在组织 {@code mfa_enforced=false} → filter 的 {@code
 *       isUserEnforced=false} 分支放过。验证「关闭 org 强制位后无强拉」语义。
 *       <b>不</b> 走 toggle 路径：策略位即时翻转语义已由 Phase 2.11a
 *       {@code OrgMfaPolicyControllerIT} 覆盖，此处只验本 filter 对 {@code false}
 *       的响应行为，避免重复跨层断言。</li>
 * </ol>
 *
 * <p><b>事务模式：</b> 与 {@link PortalMfaChallengeLoginIT} 同款 {@code @Transactional
 * (propagation = NOT_SUPPORTED)} —— 场景 1 走的 {@code POST /api/v1/login} 链路里
 * {@code UserServiceImpl#findByUsernameOrPhoneOrEmail} 用 {@code CompletableFuture.
 * supplyAsync} 三路异步查 user，独立线程脱离测试事务上下文。代价是 seed 真写库，需
 * {@code @AfterEach} 手动清账号 + 组织 + 成员关系。
 *
 * <p><b>用户名长度约束：</b> {@code ulp_user.email_} 是 VARCHAR(50)，email = "<username>
 * @example.com"，username 必须 ≤ 38 字符。用 {@code p-org-{e1,e2,e3,e4}-<nanoTime>}
 * 前缀（13 字符 + 19 字符 nanoTime ≈ 32 字符），留余量。
 *
 * <p><b>受保护端点选择：</b> {@code /api/v1/session/current_user} —— portal 公共端点，
 * 不在 {@link cn.frank.ulp.portal.security.mfa.OrgMfaEnforcementFilter#DEFAULT_WHITELIST}
 * 白名单内，必经 filter；落到 controller 后调 {@code UserUtils.getUser()} 真查库，需要
 * 用户实体存在（seed 已保证）。
 */
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class PortalOrgMfaEnforcementIT extends AbstractMfaIntegrationTest {

    private static final String          LOGIN_PATH      = "/api/v1/login";
    private static final String          PROTECTED_PATH  = "/api/v1/session/current_user";
    private static final String          PENDING_COOKIE  = "ulp-mfa-pending";

    @Autowired
    private UserRepository               userRepository;

    @Autowired
    private OrganizationRepository       organizationRepository;

    @Autowired
    private OrganizationMemberRepository organizationMemberRepository;

    @Autowired
    private PasswordEncoder              passwordEncoder;

    @Autowired
    private MfaSecretGenerator           mfaSecretGenerator;

    @Autowired
    private MfaSecretCipher              mfaSecretCipher;

    @Autowired
    private MfaBackupCodeGenerator       mfaBackupCodeGenerator;

    @Autowired
    private OrgMfaPolicyService          orgMfaPolicyService;

    /**
     * Per-test cleanup chain: member rows reference org+user via FK-less columns but the
     * deleted-flag soft-delete is fine for IT isolation; we hard-delete by id for repeatable
     * runs. Order: member → org → user (no real FKs but matches logical dependency).
     */
    private final List<String>           seededMemberIds = new ArrayList<>();
    private final List<String>           seededOrgIds    = new ArrayList<>();
    private final List<String>           seededUserIds   = new ArrayList<>();

    @AfterEach
    void cleanup() {
        seededMemberIds.forEach(id -> {
            try {
                organizationMemberRepository.deleteById(id);
            } catch (RuntimeException ignored) {
            }
        });
        seededOrgIds.forEach(id -> {
            try {
                organizationRepository.deleteById(id);
            } catch (RuntimeException ignored) {
            }
        });
        seededUserIds.forEach(id -> {
            try {
                userRepository.deleteById(id);
            } catch (RuntimeException ignored) {
            }
        });
        seededMemberIds.clear();
        seededOrgIds.clear();
        seededUserIds.clear();
    }

    /**
     * 场景 1：未绑 user + 强制组织 → POST /api/v1/login → 200 mfa_setup_required + 无 pending cookie。
     */
    @Test
    void enforcedUserFirstLogin_returnsMfaSetupRequired() throws Exception {
        String rawPassword = "SetupRequired@Pwd-12345";
        String username = "p-org-e1-" + System.nanoTime();
        String userId = seedUserWithoutMfa(username, rawPassword);
        seedEnforcedOrg(userId, "p-org-e1-" + System.nanoTime(), true);

        // 防御性断言：seed 是否真的让 isUserEnforced 返回 true。若此处 false，说明
        // saveAndFlush 后 OR-语义查询读不到 member+org 行 —— 多半是 NOT_SUPPORTED 下
        // saveAndFlush 没真提交，或 OrgMfaPolicyService 的 query 漏读软删过滤。
        assertThat(orgMfaPolicyService.isUserEnforced(userId))
            .as("seed 完成后 isUserEnforced(userId) 必须为 true，否则 strategy 不会走 SETUP_REQUIRED").isTrue();

        MvcResult result = mockMvc
            .perform(post(LOGIN_PATH).with(csrf()).param("username", username).param("password",
                rawPassword))
            .andExpect(status().isOk())
            // SETUP_REQUIRED 路径与 MFA_REQUIRED 一样回 success=false：登录尚未完成，前端
            // 不应把这次响应当成已登录态去拉受保护接口。
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.status").value("mfa_setup_required"))
            .andExpect(jsonPath("$.result.mfa_setup_required").value(true))
            .andExpect(jsonPath("$.result.reason").value("org_policy"))
            .andExpect(jsonPath("$.result.setup_path").value("/mfa/setup"))
            // SETUP 路径不下发 pending cookie —— 那是 CHALLENGE 路径专用，不应混在一起。
            .andExpect(cookie().doesNotExist(PENDING_COOKIE)).andReturn();

        JSONObject body = JSON.parseObject(result.getResponse().getContentAsString());
        assertThat(body.getJSONObject("result").getString("challenge_id"))
            .as("SETUP 路径不应带 challenge_id").isNull();
    }

    /**
     * 场景 2：未绑 user + 强制组织 + 已认证 → GET /api/v1/session/current_user → 403。
     */
    @Test
    void enforcedUserAccessingProtectedPath_returns403MfaSetupRequired() throws Exception {
        String username = "p-org-e2-" + System.nanoTime();
        String userId = seedUserWithoutMfa(username, "Filter403@Pwd-12345");
        seedEnforcedOrg(userId, "p-org-e2-" + System.nanoTime(), true);

        mockMvc.perform(get(PROTECTED_PATH).with(authentication(mockUserAuth(userId, username))))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.error").value("mfa_setup_required"))
            .andExpect(jsonPath("$.reason").value("org_policy"));
    }

    /**
     * 场景 3：已绑 user + 强制组织 + 已认证 → filter 放行 → 不再 403。
     */
    @Test
    void boundUserUnderEnforcement_isNotBlocked() throws Exception {
        String username = "p-org-e3-" + System.nanoTime();
        String userId = seedUserWithMfa(username, "BoundOk@Pwd-12345");
        seedEnforcedOrg(userId, "p-org-e3-" + System.nanoTime(), true);

        mockMvc.perform(get(PROTECTED_PATH).with(authentication(mockUserAuth(userId, username))))
            // 关键：不是 403 mfa_setup_required —— filter 必须放行已绑用户。controller 本身
            // 可能 200（正常返回 CurrentUserResult），也可能因为某些 detail 字段缺失走 5xx，
            // 但不应该是 filter 的 403 mfa_setup_required。
            .andExpect(result -> {
                int s = result.getResponse().getStatus();
                assertThat(s).as("已绑用户在强制组织下不应被 filter 403 拦截").isNotEqualTo(403);
                String body = result.getResponse().getContentAsString();
                if (body != null && !body.isEmpty()) {
                    assertThat(body).as("响应体不应含 mfa_setup_required 错误")
                        .doesNotContain("mfa_setup_required");
                }
            });
    }

    /**
     * 场景 4：未绑 user + 非强制组织 + 已认证 → filter 放行 → 不再 403。
     */
    @Test
    void unenforcedOrg_doesNotBlockUnboundUser() throws Exception {
        String username = "p-org-e4-" + System.nanoTime();
        String userId = seedUserWithoutMfa(username, "Voluntary@Pwd-12345");
        seedEnforcedOrg(userId, "p-org-e4-" + System.nanoTime(), false);

        mockMvc.perform(get(PROTECTED_PATH).with(authentication(mockUserAuth(userId, username))))
            // 与场景 3 同样的断言形态：不是 filter 的 403 mfa_setup_required。
            .andExpect(result -> {
                int s = result.getResponse().getStatus();
                assertThat(s).as("自愿组下未绑用户不应被 filter 403 拦截").isNotEqualTo(403);
                String body = result.getResponse().getContentAsString();
                if (body != null && !body.isEmpty()) {
                    assertThat(body).as("响应体不应含 mfa_setup_required 错误")
                        .doesNotContain("mfa_setup_required");
                }
            });
    }

    /**
     * 建一条 ENABLED + mfa_enabled=false 的 user，不挂任何组织（调用方自行 seedEnforcedOrg）。
     */
    private String seedUserWithoutMfa(String username, String rawPassword) {
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
        String userId = userRepository.saveAndFlush(user).getId();
        seededUserIds.add(userId);
        return userId;
    }

    /**
     * 建一条 ENABLED + mfa_enabled=true + 完整 cipher / backup_codes 的 user。MFA 行为下，
     * filter 的 {@code loadActiveCipher} 仅依赖 totp_secret_cipher 非空，但 mfa_enabled
     * 列 NOT NULL 必须给 TRUE 否则触发 ConstraintViolation。
     */
    private String seedUserWithMfa(String username, String rawPassword) {
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
        String userId = userRepository.saveAndFlush(user).getId();
        seededUserIds.add(userId);
        return userId;
    }

    /**
     * 建一个叶子节点组织（强制位由参数 enforced 决定），并把 user 挂上去。组织 code / path /
     * display_path 列 NOT NULL，业务上由组织树构建逻辑算出，IT 直接造叶子节点占位即可。
     */
    private void seedEnforcedOrg(String userId, String orgCode, boolean enforced) {
        OrganizationEntity org = new OrganizationEntity();
        org.setName("IT Org " + orgCode);
        org.setCode(orgCode);
        org.setType(OrganizationType.GROUP);
        org.setLeaf(Boolean.TRUE);
        org.setEnabled(Boolean.TRUE);
        org.setMfaEnforced(enforced);
        org.setDataOrigin("input");
        org.setPath("/" + orgCode);
        org.setDisplayPath("/IT Org " + orgCode);
        String orgId = organizationRepository.saveAndFlush(org).getId();
        seededOrgIds.add(orgId);
        OrganizationMemberEntity member = organizationMemberRepository
            .saveAndFlush(new OrganizationMemberEntity(orgId, userId));
        seededMemberIds.add(member.getId());
    }

    /**
     * 与 {@link MfaUserUnbindFlowIT#mockUserAuth} 同款：第一个构造参数 = id，
     * {@code SecurityUtils.getCurrentUserId()} 取的就是它；WebAuthenticationDetails
     * 给个稳定值，避免 filter 链上有人想读 geoLocation 时 NPE。
     */
    private static UsernamePasswordAuthenticationToken mockUserAuth(String userId,
                                                                    String username) {
        UserDetails u = new UserDetails(userId, username, UserType.USER, true, true, true, true,
            AuthorityUtils.NO_AUTHORITIES);
        u.setUpdateTime(LocalDateTime.now());
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken
            .authenticated(u, null, u.getAuthorities());
        WebAuthenticationDetails details = new WebAuthenticationDetails("127.0.0.1", "test-session",
            null, null, new AuthenticationProvider("portal", "test"), LocalDateTime.now());
        token.setDetails(details);
        return token;
    }
}
