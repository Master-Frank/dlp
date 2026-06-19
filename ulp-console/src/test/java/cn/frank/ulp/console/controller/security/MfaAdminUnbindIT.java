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

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

import cn.frank.ulp.common.entity.setting.AdministratorEntity;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.common.repository.setting.AdministratorRepository;
import cn.frank.ulp.support.security.authentication.AuthenticationProvider;
import cn.frank.ulp.support.security.authentication.WebAuthenticationDetails;
import cn.frank.ulp.support.security.mfa.MfaBackupCodeGenerator;
import cn.frank.ulp.support.security.mfa.MfaSecretCipher;
import cn.frank.ulp.support.security.mfa.MfaSecretGenerator;
import cn.frank.ulp.support.security.userdetails.Application;
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
 * Admin self-service MFA 解绑流程：种子一个已绑定 MFA 的 admin，用当前窗口 OTP 调
 * {@code POST /api/v1/mfa/unbind} 应成功，DB 三字段清空。
 *
 * <p>关键不变量：admin 解绑路径 <b>不</b> 查 {@link cn.frank.ulp.common.security.mfa.OrgMfaPolicyService}
 * — 组织强制位仅对 end-user 生效，admin 永远自愿。
 */
@ActiveProfiles("test")
class MfaAdminUnbindIT extends AbstractMfaIntegrationTest {

    private static final String        UNBIND_PATH   = "/api/v1/mfa/unbind";

    @Autowired
    private AdministratorRepository    administratorRepository;

    @Autowired
    private MfaSecretCipher            mfaSecretCipher;

    @Autowired
    private MfaSecretGenerator         mfaSecretGenerator;

    @Autowired
    private MfaBackupCodeGenerator     mfaBackupCodeGenerator;

    @Autowired
    private PasswordEncoder            passwordEncoder;

    /**
     * 跟踪每个测试方法 seed 的 adminId —— @AfterEach 用它清 Redis 三类 MFA key
     * （pending / fail counter / bind secret）。本测试只走 success 路径不会写
     * {@code ULP_MFA_FAIL:admin:{id}}，但 spec Phase 8.3 要求 MFA IT 强制 Redis 清理契约，
     * 加 defensive cleanup 防止后续场景扩展时漏掉。
     */
    private String                     seededAdminId;

    @AfterEach
    void cleanupMfaRedisKeys() {
        if (seededAdminId != null) {
            cleanMfaRedisKeys("admin", seededAdminId);
            seededAdminId = null;
        }
    }

    @Test
    void unbind_withValidOtp_clearsAllMfaFields() throws Exception {
        // 种子一个已经绑过 MFA 的 admin（直接造数据，跳过 prepare/confirm）
        String secretBase32 = mfaSecretGenerator.generate();
        String cipher = mfaSecretCipher.encrypt(secretBase32.getBytes(StandardCharsets.UTF_8));
        List<String> backupCodes = mfaBackupCodeGenerator.generate();
        String backupCodesJson = JSON.toJSONString(
            backupCodes.stream().map(passwordEncoder::encode).collect(Collectors.toList()));

        AdministratorEntity admin = new AdministratorEntity();
        admin.setUsername("it-mfa-unbind-admin");
        admin.setPassword("{bcrypt}$2a$10$abcdefghijklmnopqrstuvABCDEFGHIJKLMNOPQRSTUVWXYZ12345");
        admin.setStatus(UserStatus.ENABLED);
        admin.setEmail("it-mfa-unbind-admin@example.com");
        admin.setEmailVerified(Boolean.TRUE);
        admin.setPhoneVerified(Boolean.FALSE);
        admin.setNeedChangePassword(Boolean.FALSE);
        admin.setLastUpdatePasswordTime(LocalDateTime.now());
        admin.setMfaEnabled(Boolean.TRUE);
        admin.setTotpSecretCipher(cipher);
        admin.setBackupCodesJson(backupCodesJson);
        String adminId = administratorRepository.saveAndFlush(admin).getId();
        seededAdminId = adminId;

        // 算当前窗口 OTP
        String otp = computeTotp(secretBase32);

        String body = "{\"currentOtp\":\"" + otp + "\"}";
        mockMvc
            .perform(post(UNBIND_PATH).with(authentication(mockAdminAuth(adminId))).with(csrf())
                .contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result").value(true));

        AdministratorEntity persisted = administratorRepository.findById(adminId).orElseThrow();
        assertThat(persisted.getMfaEnabled()).as("mfa_enabled 写为 false").isFalse();
        assertThat(persisted.getTotpSecretCipher()).as("totp_secret_cipher 置空").isNull();
        assertThat(persisted.getBackupCodesJson()).as("backup_codes_json 置空").isNull();
    }

    private static UsernamePasswordAuthenticationToken mockAdminAuth(String adminId) {
        UserDetails u = new UserDetails(adminId, "it-mfa-unbind-admin", UserType.ADMIN, true, true,
            true, true, AuthorityUtils.NO_AUTHORITIES);
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
