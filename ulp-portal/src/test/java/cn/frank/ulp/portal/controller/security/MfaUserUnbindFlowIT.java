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
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.alibaba.fastjson2.JSON;

import cn.frank.ulp.common.entity.account.OrganizationEntity;
import cn.frank.ulp.common.entity.account.OrganizationMemberEntity;
import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.common.enums.account.OrganizationType;
import cn.frank.ulp.common.repository.account.OrganizationMemberRepository;
import cn.frank.ulp.common.repository.account.OrganizationRepository;
import cn.frank.ulp.common.repository.account.UserRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * End-user MFA 解绑流程的 3 场景集成测试（admin 路径在
 * {@code MfaAdminUnbindIT} 单独测）：
 * <ul>
 *   <li>{@link #userWithoutEnforcement_unbindsSuccessfully()}
 *       — 无组织强制位 → 正常 OTP 验证 → 200 + 三字段清空</li>
 *   <li>{@link #userEnforcedByOrg_returns403WithoutTouchingOtp()}
 *       — 隶属强制位组织 → 进入 TOTP 前 403 {@code unbind_blocked_by_org_policy}，
 *       <b>不</b> 消费失败计数（lockout Phase 5 接入后会有专项 IT 校验）</li>
 *   <li>{@link #backupCodeIsNotAcceptedForUnbind()}
 *       — unbind 端点 <b>不</b> 接受备份码，传备份码视为无效 OTP → 5xx</li>
 * </ul>
 *
 * <p>每个场景独立 seed user + 调用，事务回滚保证 SQL 隔离。
 */
@ActiveProfiles("test")
class MfaUserUnbindFlowIT extends AbstractMfaIntegrationTest {

    private static final String          UNBIND_PATH   = "/api/v1/mfa/unbind";

    @Autowired
    private UserRepository               userRepository;

    @Autowired
    private OrganizationRepository       organizationRepository;

    @Autowired
    private OrganizationMemberRepository organizationMemberRepository;

    @Autowired
    private MfaSecretCipher              mfaSecretCipher;

    @Autowired
    private MfaSecretGenerator           mfaSecretGenerator;

    @Autowired
    private MfaBackupCodeGenerator       mfaBackupCodeGenerator;

    @Autowired
    private PasswordEncoder              passwordEncoder;

    /**
     * 跟踪每个测试方法 seed 的 userId —— @AfterEach 用它清 Redis 三类 MFA key
     * （pending / fail counter / bind secret），防止 test3 的 invalid-OTP 路径写入的
     * {@code ULP_MFA_FAIL:user:{userId}} 泄漏给后续 IT。account / org / member 行靠
     * 测试事务回滚清除，Redis 不在事务里所以必须显式清。
     */
    private final List<String>           seededUserIds = new ArrayList<>();

    @AfterEach
    void cleanupMfaRedisKeys() {
        seededUserIds.forEach(id -> cleanMfaRedisKeys("user", id));
        seededUserIds.clear();
    }

    @Test
    void userWithoutEnforcement_unbindsSuccessfully() throws Exception {
        SeededUser seeded = seedUserWithMfa("it-mfa-unbind-user-1");

        String otp = computeTotp(seeded.secretBase32());

        String body = "{\"currentOtp\":\"" + otp + "\"}";
        mockMvc
            .perform(post(UNBIND_PATH).with(authentication(mockUserAuth(seeded.userId())))
                .with(csrf()).contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result").value(true));

        UserEntity persisted = userRepository.findById(seeded.userId()).orElseThrow();
        assertThat(persisted.getMfaEnabled()).as("mfa_enabled 应被清回 false").isFalse();
        assertThat(persisted.getTotpSecretCipher()).as("totp_secret_cipher 应清空").isNull();
        assertThat(persisted.getBackupCodesJson()).as("backup_codes_json 应清空").isNull();
    }

    @Test
    void userEnforcedByOrg_returns403WithoutTouchingOtp() throws Exception {
        SeededUser seeded = seedUserWithMfa("it-mfa-unbind-user-2");

        // 建一个 mfa_enforced=true 的组织，并把用户挂上去
        OrganizationEntity org = new OrganizationEntity();
        org.setName("Enforced Org");
        org.setCode("it-mfa-enforced-org");
        org.setType(OrganizationType.GROUP);
        org.setLeaf(Boolean.TRUE);
        org.setEnabled(Boolean.TRUE);
        org.setMfaEnforced(Boolean.TRUE);
        // data_origin / path_ / display_path 列 NOT NULL（见 1.0.0-changelog.xml ulp_organization）
        // 业务上 path/display_path 由组织树构建逻辑算出，IT 直接造叶子节点给占位值即可
        org.setDataOrigin("input");
        org.setPath("/it-mfa-enforced-org");
        org.setDisplayPath("/Enforced Org");
        String orgId = organizationRepository.saveAndFlush(org).getId();
        organizationMemberRepository
            .saveAndFlush(new OrganizationMemberEntity(orgId, seeded.userId()));

        // 故意传一个绝对不会通过 TOTP 校验的字符串：组织强制位应在 OTP 校验之前就 403
        String body = "{\"currentOtp\":\"000000\"}";
        mockMvc
            .perform(post(UNBIND_PATH).with(authentication(mockUserAuth(seeded.userId())))
                .with(csrf()).contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.status").value("unbind_blocked_by_org_policy"));

        // DB 三字段 NOT 改动：mfa_enabled 仍 true、cipher / json 仍非空
        UserEntity persisted = userRepository.findById(seeded.userId()).orElseThrow();
        assertThat(persisted.getMfaEnabled()).as("被组织强制时 unbind 必须无副作用").isTrue();
        assertThat(persisted.getTotpSecretCipher()).as("cipher 不应被清").isNotNull();
        assertThat(persisted.getBackupCodesJson()).as("backup_codes_json 不应被清").isNotNull();
    }

    @Test
    void backupCodeIsNotAcceptedForUnbind() throws Exception {
        SeededUser seeded = seedUserWithMfa("it-mfa-unbind-user-3");

        // 用任意一个明文备份码（8 位 [2-9A-HJ-NP-Z]）冒充 OTP
        String backupCode = seeded.plaintextBackupCodes().get(0);
        String body = "{\"currentOtp\":\"" + backupCode + "\"}";

        // unbind 端点显式只走 codeVerifier.isValid(secret, otp)，备份码格式 ≠ 6 位 TOTP，
        // 必然失败 → BadParamsException → GlobalExceptionHandler.ulpException 走 ModelAndView 转发 /error。
        // MockMvc 下这条 forward 路径不会真的渲染 BasicErrorController 的 JSON 体（body 为空、HTTP 状态不稳），
        // 所以只能以"业务不变量"——DB 三字段没动——作为断言锚。只 perform，不断响应。
        mockMvc.perform(post(UNBIND_PATH).with(authentication(mockUserAuth(seeded.userId())))
            .with(csrf()).contentType(MediaType.APPLICATION_JSON).content(body));

        // DB 三字段未动
        UserEntity persisted = userRepository.findById(seeded.userId()).orElseThrow();
        assertThat(persisted.getMfaEnabled()).as("失败 unbind 不应改 mfa_enabled").isTrue();
        assertThat(persisted.getTotpSecretCipher()).as("cipher 不应被清").isNotNull();
    }

    private SeededUser seedUserWithMfa(String username) {
        String secretBase32 = mfaSecretGenerator.generate();
        String cipher = mfaSecretCipher.encrypt(secretBase32.getBytes(StandardCharsets.UTF_8));
        List<String> plaintextCodes = mfaBackupCodeGenerator.generate();
        String backupCodesJson = JSON.toJSONString(
            plaintextCodes.stream().map(passwordEncoder::encode).collect(Collectors.toList()));

        UserEntity user = new UserEntity();
        user.setUsername(username);
        // 任意密文；MFA 流程不读密码字段
        user.setPassword("{bcrypt}$2a$10$abcdefghijklmnopqrstuvABCDEFGHIJKLMNOPQRSTUVWXYZ12345");
        user.setStatus(UserStatus.ENABLED);
        user.setEmail(username + "@example.com");
        user.setFullName(username);
        user.setEmailVerified(Boolean.TRUE);
        user.setPhoneVerified(Boolean.FALSE);
        user.setNeedChangePassword(Boolean.FALSE);
        user.setDataOrigin("input");
        user.setLastUpdatePasswordTime(LocalDateTime.now());
        // mfa_enabled 列 NOT NULL，Boolean 默认 null 会绕过 DB DEFAULT FALSE 触发 ConstraintViolation
        user.setMfaEnabled(Boolean.TRUE);
        user.setTotpSecretCipher(cipher);
        user.setBackupCodesJson(backupCodesJson);
        String userId = userRepository.saveAndFlush(user).getId();
        seededUserIds.add(userId);
        return new SeededUser(userId, secretBase32, plaintextCodes);
    }

    private static UsernamePasswordAuthenticationToken mockUserAuth(String userId) {
        // 第一个构造参数 = id，SecurityUtils.getCurrentUserId() 取的就是它
        UserDetails u = new UserDetails(userId, "it-mfa-unbind-user", UserType.USER, true, true,
            true, true, AuthorityUtils.NO_AUTHORITIES);
        u.setUpdateTime(LocalDateTime.now());
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken
            .authenticated(u, null, u.getAuthorities());
        WebAuthenticationDetails details = new WebAuthenticationDetails("127.0.0.1", "test-session",
            null, null, new AuthenticationProvider("portal", "test"), LocalDateTime.now());
        token.setDetails(details);
        return token;
    }

    private record SeededUser(String userId, String secretBase32,
                              List<String> plaintextBackupCodes) {
    }
}
