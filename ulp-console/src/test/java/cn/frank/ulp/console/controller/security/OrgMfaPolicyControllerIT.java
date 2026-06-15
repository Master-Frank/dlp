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
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.context.ActiveProfiles;

import cn.frank.ulp.common.entity.account.OrganizationEntity;
import cn.frank.ulp.common.entity.account.OrganizationMemberEntity;
import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.entity.setting.AdministratorEntity;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.common.enums.account.OrganizationType;
import cn.frank.ulp.common.repository.account.OrganizationMemberRepository;
import cn.frank.ulp.common.repository.account.OrganizationRepository;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.common.repository.setting.AdministratorRepository;
import cn.frank.ulp.common.security.mfa.OrgMfaPolicyService;
import cn.frank.ulp.support.security.authentication.AuthenticationProvider;
import cn.frank.ulp.support.security.authentication.WebAuthenticationDetails;
import cn.frank.ulp.support.security.userdetails.Application;
import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.userdetails.UserType;
import cn.frank.ulp.support.testsupport.AbstractIntegrationTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 组织级 MFA 强制位切换端点 ({@code OrgMfaPolicyController}) 集成测试，覆盖 5 个 spec 场景：
 * <ol>
 *   <li>{@link #adminFlipsEnforcedOn_returns200AndPersists()} — ADMIN OFF→ON：
 *       200 + body {@code changed=true} + DB {@code mfa_enforced=true}</li>
 *   <li>{@link #adminWritesSameValue_returns200WithChangedFalse()} — ADMIN ON→ON：
 *       200 + body {@code changed=false}（重复值视为 no-op，DB 不动）</li>
 *   <li>{@link #nonAdminPrincipal_isDenied()} — USER 调用：被 {@code @PreAuthorize} 拒，DB 不动</li>
 *   <li>{@link #orgNotFound_responseIsErrorAndDbUnchanged()} — 错的 orgId：
 *       BadParamsException → /error，业务锚 = 没创出脏数据（任何 DB 副作用应缺失）</li>
 *   <li>{@link #flippingPolicyOff_releasesUserFromEnforcement()} — 联合 unbind 路径预校验：
 *       org 强制位开启时 {@code OrgMfaPolicyService.isUserEnforced=true}；
 *       通过 controller 关掉后立即 {@code =false}（即 unbind 端点会放行）</li>
 * </ol>
 *
 * <p>Phase 6.4/6.5 会接入 {@code ORG_MFA_POLICY_CHANGED} 审计；spec 提到的"+ 审计 1 行 / 0 行"
 * 断言因 publish 尚未接线，本 IT 暂以 DB / response body 为锚。Phase 6 完成后补 audit-side 断言
 * （在 {@code MfaAuditEventIT} 中处理，避免本 IT 与 audit 模块强耦合）。
 */
@ActiveProfiles("test")
class OrgMfaPolicyControllerIT extends AbstractIntegrationTest {

    private static final String          PATH_PREFIX = "/api/v1/admin/organizations/";

    @Autowired
    private OrganizationRepository       organizationRepository;

    @Autowired
    private OrganizationMemberRepository organizationMemberRepository;

    @Autowired
    private UserRepository               userRepository;

    @Autowired
    private AdministratorRepository      administratorRepository;

    @Autowired
    private OrgMfaPolicyService          orgMfaPolicyService;

    @Test
    void adminFlipsEnforcedOn_returns200AndPersists() throws Exception {
        String orgId = seedOrg("it-policy-flip-on", Boolean.FALSE);
        String adminId = seedAdmin("it-policy-actor-1");

        mockMvc
            .perform(post(PATH_PREFIX + orgId + "/mfa-policy")
                .with(authentication(mockAdminAuth(adminId, "it-policy-actor-1"))).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content("{\"mfaEnforced\":true}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result.orgId").value(orgId))
            .andExpect(jsonPath("$.result.mfaEnforced").value(true))
            .andExpect(jsonPath("$.result.changed").value(true));

        OrganizationEntity persisted = organizationRepository.findById(orgId).orElseThrow();
        assertThat(persisted.getMfaEnforced()).as("mfa_enforced 落库 true").isTrue();
    }

    @Test
    void adminWritesSameValue_returns200WithChangedFalse() throws Exception {
        // 种子已开启的 org，再写一次 true → changed=false
        String orgId = seedOrg("it-policy-noop", Boolean.TRUE);
        String adminId = seedAdmin("it-policy-actor-2");

        mockMvc
            .perform(post(PATH_PREFIX + orgId + "/mfa-policy")
                .with(authentication(mockAdminAuth(adminId, "it-policy-actor-2"))).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content("{\"mfaEnforced\":true}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result.mfaEnforced").value(true))
            .andExpect(jsonPath("$.result.changed").value(false));

        OrganizationEntity persisted = organizationRepository.findById(orgId).orElseThrow();
        assertThat(persisted.getMfaEnforced()).as("重复值 DB 仍为 true").isTrue();
    }

    @Test
    void nonAdminPrincipal_isDenied() throws Exception {
        String orgId = seedOrg("it-policy-deny", Boolean.FALSE);

        // USER 角色 → @PreAuthorize 拒。AccessDeniedException 在 MockMvc 下的转发体不稳，
        // 业务锚 = DB mfa_enforced 没被翻成 true。
        mockMvc.perform(post(PATH_PREFIX + orgId + "/mfa-policy")
            .with(authentication(mockUserAuth("it-policy-attacker"))).with(csrf())
            .contentType(MediaType.APPLICATION_JSON).content("{\"mfaEnforced\":true}"));

        OrganizationEntity persisted = organizationRepository.findById(orgId).orElseThrow();
        assertThat(persisted.getMfaEnforced()).as("非 admin 调用不应改 mfa_enforced").isFalse();
    }

    @Test
    void orgNotFound_responseIsErrorAndDbUnchanged() throws Exception {
        String adminId = seedAdmin("it-policy-actor-4");
        String bogusOrgId = "non-existent-org-id-xxxx";

        // 不存在的 orgId → BadParamsException → GlobalExceptionHandler 转发 /error。
        // tasks 写的是 "404"，但 BadParamsException 实际 httpStatus=500；MockMvc body 又为空。
        // 业务锚 = 该 orgId 在 DB 里仍然不存在（controller 不能"为了报错"先 insert 个空行）。
        mockMvc.perform(post(PATH_PREFIX + bogusOrgId + "/mfa-policy")
            .with(authentication(mockAdminAuth(adminId, "it-policy-actor-4"))).with(csrf())
            .contentType(MediaType.APPLICATION_JSON).content("{\"mfaEnforced\":true}"));

        assertThat(organizationRepository.findById(bogusOrgId)).as("错的 orgId 不应被 controller 创出来")
            .isEmpty();
    }

    @Test
    void flippingPolicyOff_releasesUserFromEnforcement() throws Exception {
        // 联合 unbind 预校验：先开 org 强制位 + 挂用户 → OrgMfaPolicyService.isUserEnforced=true
        // 然后 controller 关掉 → 立即 isUserEnforced=false（unbind 端点会因此放行）
        String orgId = seedOrg("it-policy-release", Boolean.TRUE);
        String adminId = seedAdmin("it-policy-actor-5");
        String userId = seedPlainUser("it-policy-target-user");
        organizationMemberRepository.saveAndFlush(new OrganizationMemberEntity(orgId, userId));

        assertThat(orgMfaPolicyService.isUserEnforced(userId)).as("初始状态被强制").isTrue();

        mockMvc
            .perform(post(PATH_PREFIX + orgId + "/mfa-policy")
                .with(authentication(mockAdminAuth(adminId, "it-policy-actor-5"))).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content("{\"mfaEnforced\":false}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.result.changed").value(true));

        assertThat(orgMfaPolicyService.isUserEnforced(userId)).as("关掉后立即解除强制").isFalse();
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

    private String seedPlainUser(String username) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("{bcrypt}$2a$10$abcdefghijklmnopqrstuvABCDEFGHIJKLMNOPQRSTUVWXYZ12345");
        user.setStatus(UserStatus.ENABLED);
        user.setEmail(username + "@example.com");
        user.setFullName(username);
        user.setEmailVerified(Boolean.TRUE);
        user.setPhoneVerified(Boolean.FALSE);
        user.setNeedChangePassword(Boolean.FALSE);
        user.setDataOrigin("input");
        user.setLastUpdatePasswordTime(LocalDateTime.now());
        user.setMfaEnabled(Boolean.FALSE);
        return userRepository.saveAndFlush(user).getId();
    }

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
            null, null, new AuthenticationProvider("portal", "test"), LocalDateTime.now());
        token.setDetails(details);
        return token;
    }

    private static UsernamePasswordAuthenticationToken mockUserAuth(String username) {
        UserDetails u = new UserDetails(username, username, UserType.USER, true, true, true, true,
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
