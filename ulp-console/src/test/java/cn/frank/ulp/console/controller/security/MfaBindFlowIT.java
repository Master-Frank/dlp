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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import cn.frank.ulp.common.entity.setting.AdministratorEntity;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.common.repository.setting.AdministratorRepository;
import cn.frank.ulp.support.security.authentication.AuthenticationProvider;
import cn.frank.ulp.support.security.authentication.WebAuthenticationDetails;
import cn.frank.ulp.support.security.mfa.MfaSecretCipher;
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
 * Admin self-service MFA 绑定流程的端到端集成测试：
 * prepare → 用 secret 算 OTP → confirm → DB 字段写入校验。
 *
 * <p>{@code @Transactional} 在测试方法外回滚，但 Redis 暂存的 staging key 不在事务内，
 * {@code @AfterEach} 必须显式清理 {@code ULP_BIND_MFA_SECRET:admin:{adminId}}。
 */
@ActiveProfiles("test")
class MfaBindFlowIT extends AbstractMfaIntegrationTest {

    private static final String     PREPARE_PATH = "/api/v1/mfa/bind/prepare";
    private static final String     CONFIRM_PATH = "/api/v1/mfa/bind/confirm";

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private MfaSecretCipher         mfaSecretCipher;

    private String                  seededAdminId;
    private String                  seededAdminUsername;

    @BeforeEach
    void seedAdmin() {
        seededAdminUsername = "it-mfa-bind-admin";
        AdministratorEntity admin = new AdministratorEntity();
        admin.setUsername(seededAdminUsername);
        // 任意密文即可；MFA 流程不读密码字段
        admin.setPassword("{bcrypt}$2a$10$abcdefghijklmnopqrstuvABCDEFGHIJKLMNOPQRSTUVWXYZ12345");
        admin.setStatus(UserStatus.ENABLED);
        admin.setEmail(seededAdminUsername + "@example.com");
        admin.setEmailVerified(Boolean.TRUE);
        admin.setPhoneVerified(Boolean.FALSE);
        admin.setNeedChangePassword(Boolean.FALSE);
        admin.setLastUpdatePasswordTime(LocalDateTime.now());
        // mfa_enabled 列 NOT NULL，DB 有 DEFAULT FALSE 但 JPA insert 显式传 null 会绕过 default，
        // 触发 ConstraintViolation。必须显式赋值。
        admin.setMfaEnabled(Boolean.FALSE);
        // saveAndFlush —— 同 PasswordUpgradeIT 注释：纯 save() 在 @Transactional 测试事务里
        // 可能不立即落库，导致后续 controller 路径 findById 看不到 seed 账号。
        seededAdminId = administratorRepository.saveAndFlush(admin).getId();
    }

    @AfterEach
    void cleanRedis() {
        cleanMfaRedisKeys("admin", seededAdminId);
    }

    @Test
    void prepareThenConfirm_writesAllMfaFields() throws Exception {
        // 1. prepare：返回 otpAuthUri + secretBase32，secret 同步暂存进 Redis
        MvcResult prepareResult = mockMvc
            .perform(
                post(PREPARE_PATH).with(authentication(mockAdminAuth(seededAdminId))).with(csrf()))
            .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result.otpAuthUri").exists())
            .andExpect(jsonPath("$.result.secretBase32").exists()).andReturn();

        JSONObject prepareJson = JSON.parseObject(prepareResult.getResponse().getContentAsString());
        JSONObject preparePayload = prepareJson.getJSONObject("result");
        String secretBase32 = preparePayload.getString("secretBase32");
        String otpAuthUri = preparePayload.getString("otpAuthUri");

        assertThat(secretBase32).as("secretBase32 非空").isNotBlank();
        assertThat(otpAuthUri).as("otpAuthUri 含 otpauth:// 前缀").startsWith("otpauth://totp/");
        assertThat(otpAuthUri).as("otpAuthUri 含 secret 查询参数").contains("secret=" + secretBase32);

        // Redis 暂存可见
        assertThat(redisTemplate.opsForValue().get(bindStagingKey("admin", seededAdminId)))
            .as("Redis staging 写入").isEqualTo(secretBase32);

        // 2. 用 secret 算当前窗口 OTP
        String otp = computeTotp(secretBase32);

        // 3. confirm：返回 10 个明文 backupCodes
        String confirmBody = "{\"otp\":\"" + otp + "\"}";
        MvcResult confirmResult = mockMvc
            .perform(post(CONFIRM_PATH).with(authentication(mockAdminAuth(seededAdminId)))
                .with(csrf()).contentType(MediaType.APPLICATION_JSON).content(confirmBody))
            .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result.backupCodes").isArray())
            .andExpect(jsonPath("$.result.backupCodes.length()").value(10)).andReturn();

        JSONObject confirmJson = JSON.parseObject(confirmResult.getResponse().getContentAsString());
        List<String> backupCodes = confirmJson.getJSONObject("result").getList("backupCodes",
            String.class);
        // 备份码格式 8 位 [2-9A-HJ-NP-Z]
        for (String code : backupCodes) {
            assertThat(code).as("backup code 8 位字符").hasSize(8).matches("[2-9A-HJ-NP-Z]{8}");
        }

        // 4. DB 字段断言
        AdministratorEntity persisted = administratorRepository.findById(seededAdminId)
            .orElseThrow();
        assertThat(persisted.getMfaEnabled()).as("mfa_enabled 写为 true").isTrue();
        assertThat(persisted.getTotpSecretCipher()).as("totp_secret_cipher 非空").isNotBlank();
        assertThat(persisted.getBackupCodesJson()).as("backup_codes_json 非空").isNotBlank();

        // cipher 可解密回原始 secret
        byte[] decrypted = mfaSecretCipher.decrypt(persisted.getTotpSecretCipher());
        assertThat(new String(decrypted, StandardCharsets.UTF_8))
            .as("totp_secret_cipher 解密 = 原 secret").isEqualTo(secretBase32);

        // backup_codes_json 是 10 个哈希字符串
        List<String> hashedInDb = JSON.parseArray(persisted.getBackupCodesJson(), String.class);
        assertThat(hashedInDb).as("DB 中 hashed backup codes 10 个").hasSize(10);
        // 不应等于明文（最起码不应有任何明文等于密文）
        assertThat(hashedInDb).as("DB 中不存明文备份码").noneMatch(backupCodes::contains);

        // Redis staging 应该被清除
        assertThat(redisTemplate.opsForValue().get(bindStagingKey("admin", seededAdminId)))
            .as("confirm 后 Redis staging 清除").isNull();
    }

    /**
     * 构造一个具备 ADMIN 权限、principal id = adminId 的 mock Authentication。
     * 参考 {@code OrganizationControllerIT#mockAdminAuthentication} 的注释背景。
     */
    private static UsernamePasswordAuthenticationToken mockAdminAuth(String adminId) {
        UserDetails u = new UserDetails(adminId, "it-mfa-bind-admin", UserType.ADMIN, true, true,
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
