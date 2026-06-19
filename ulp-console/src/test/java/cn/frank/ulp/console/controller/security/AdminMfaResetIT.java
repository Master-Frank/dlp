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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.alibaba.fastjson2.JSON;

import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.entity.setting.AdministratorEntity;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.common.repository.account.UserRepository;
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
 * Admin 重置他人 MFA 端点 ({@code AdminMfaResetController}) 集成测试：
 * <ul>
 *   <li>{@link #adminResetsUser_clearsAllMfaFields()} — ADMIN 调
 *       {@code POST /api/v1/admin/users/{id}/reset-mfa}：200，user 三字段清空</li>
 *   <li>{@link #adminResetsAdministrator_clearsAllMfaFields()} — ADMIN 调
 *       {@code POST /api/v1/admin/administrators/{id}/reset-mfa}：200，admin 三字段清空</li>
 *   <li>{@link #nonAdminPrincipal_isDenied()} — USER 角色调用 reset：被 {@code @PreAuthorize} 拒，
 *       业务三字段必须保持不变（HTTP 状态以"DB 不动"为锚，参考 MfaUserUnbindFlowIT 负路径注释）</li>
 * </ul>
 *
 * <p>种子用户/管理员预设为已绑定 MFA（三字段非空），重置后断言全部清空。
 * Admin 重置 <b>不</b> 验证目标的 OTP — 不需要构造 secret/OTP 对，直接 reset 即可。
 */
@ActiveProfiles("test")
class AdminMfaResetIT extends AbstractMfaIntegrationTest {

    private static final String     USER_RESET_PATH  = "/api/v1/admin/users/";
    private static final String     ADMIN_RESET_PATH = "/api/v1/admin/administrators/";

    @Autowired
    private UserRepository          userRepository;

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private MfaSecretCipher         mfaSecretCipher;

    @Autowired
    private MfaSecretGenerator      mfaSecretGenerator;

    @Autowired
    private MfaBackupCodeGenerator  mfaBackupCodeGenerator;

    @Autowired
    private PasswordEncoder         passwordEncoder;

    /**
     * 跟踪 seed 的 user / admin id —— @AfterEach 用它们清 Redis 三类 MFA key
     * （pending / fail counter / bind secret）。reset 端点本身只做 DB 清，不写 Redis；
     * 加 defensive cleanup 满足 Phase 8.3 spec "每个 MFA IT 必须显式清 Redis"。
     */
    private final List<String>      seededUserIds  = new ArrayList<>();
    private final List<String>      seededAdminIds = new ArrayList<>();

    @AfterEach
    void cleanupMfaRedisKeys() {
        seededUserIds.forEach(id -> cleanMfaRedisKeys("user", id));
        seededAdminIds.forEach(id -> cleanMfaRedisKeys("admin", id));
        seededUserIds.clear();
        seededAdminIds.clear();
    }

    @Test
    void adminResetsUser_clearsAllMfaFields() throws Exception {
        String targetUserId = seedUserWithMfa("it-mfa-admin-reset-user-1");
        String actingAdminId = seedAdminWithoutMfa("it-mfa-admin-actor-1");

        mockMvc
            .perform(post(USER_RESET_PATH + targetUserId + "/reset-mfa")
                .with(authentication(mockAdminAuth(actingAdminId, "it-mfa-admin-actor-1")))
                .with(csrf()))
            .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result").value(true));

        UserEntity persisted = userRepository.findById(targetUserId).orElseThrow();
        assertThat(persisted.getMfaEnabled()).as("mfa_enabled 应被清回 false").isFalse();
        assertThat(persisted.getTotpSecretCipher()).as("totp_secret_cipher 应清空").isNull();
        assertThat(persisted.getBackupCodesJson()).as("backup_codes_json 应清空").isNull();
    }

    @Test
    void adminResetsAdministrator_clearsAllMfaFields() throws Exception {
        String targetAdminId = seedAdminWithMfa("it-mfa-admin-reset-target-2");
        String actingAdminId = seedAdminWithoutMfa("it-mfa-admin-actor-2");

        mockMvc
            .perform(post(ADMIN_RESET_PATH + targetAdminId + "/reset-mfa")
                .with(authentication(mockAdminAuth(actingAdminId, "it-mfa-admin-actor-2")))
                .with(csrf()))
            .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result").value(true));

        AdministratorEntity persisted = administratorRepository.findById(targetAdminId)
            .orElseThrow();
        assertThat(persisted.getMfaEnabled()).as("mfa_enabled 应被清回 false").isFalse();
        assertThat(persisted.getTotpSecretCipher()).as("totp_secret_cipher 应清空").isNull();
        assertThat(persisted.getBackupCodesJson()).as("backup_codes_json 应清空").isNull();
    }

    @Test
    void nonAdminPrincipal_isDenied() throws Exception {
        String targetUserId = seedUserWithMfa("it-mfa-admin-reset-user-3");
        // 用一个 USER 类型 principal 调 admin-only 端点。@PreAuthorize 走 @sae.hasAuthority(ADMIN)，
        // USER 必拒；AccessDeniedException 上抛后由 ExceptionTranslationFilter / handler 处理。
        // 与 MfaUserUnbindFlowIT 备份码场景同思路：MockMvc 下 forward 到 /error 的状态/体不稳，
        // 业务不变量锚 = DB 三字段不动。
        mockMvc.perform(post(USER_RESET_PATH + targetUserId + "/reset-mfa")
            .with(authentication(mockUserAuth("it-mfa-admin-reset-attacker"))).with(csrf()));

        UserEntity persisted = userRepository.findById(targetUserId).orElseThrow();
        assertThat(persisted.getMfaEnabled()).as("非 admin 调用不应改 mfa_enabled").isTrue();
        assertThat(persisted.getTotpSecretCipher()).as("cipher 不应被清").isNotNull();
        assertThat(persisted.getBackupCodesJson()).as("backup_codes_json 不应被清").isNotNull();
    }

    private String seedUserWithMfa(String username) {
        String secretBase32 = mfaSecretGenerator.generate();
        String cipher = mfaSecretCipher.encrypt(secretBase32.getBytes(StandardCharsets.UTF_8));
        List<String> plaintextCodes = mfaBackupCodeGenerator.generate();
        String backupCodesJson = JSON.toJSONString(
            plaintextCodes.stream().map(passwordEncoder::encode).collect(Collectors.toList()));

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
        user.setMfaEnabled(Boolean.TRUE);
        user.setTotpSecretCipher(cipher);
        user.setBackupCodesJson(backupCodesJson);
        String userId = userRepository.saveAndFlush(user).getId();
        seededUserIds.add(userId);
        return userId;
    }

    private String seedAdminWithMfa(String username) {
        String secretBase32 = mfaSecretGenerator.generate();
        String cipher = mfaSecretCipher.encrypt(secretBase32.getBytes(StandardCharsets.UTF_8));
        List<String> plaintextCodes = mfaBackupCodeGenerator.generate();
        String backupCodesJson = JSON.toJSONString(
            plaintextCodes.stream().map(passwordEncoder::encode).collect(Collectors.toList()));

        AdministratorEntity admin = new AdministratorEntity();
        admin.setUsername(username);
        admin.setPassword("{bcrypt}$2a$10$abcdefghijklmnopqrstuvABCDEFGHIJKLMNOPQRSTUVWXYZ12345");
        admin.setStatus(UserStatus.ENABLED);
        admin.setEmail(username + "@example.com");
        admin.setEmailVerified(Boolean.TRUE);
        admin.setPhoneVerified(Boolean.FALSE);
        admin.setNeedChangePassword(Boolean.FALSE);
        admin.setLastUpdatePasswordTime(LocalDateTime.now());
        admin.setMfaEnabled(Boolean.TRUE);
        admin.setTotpSecretCipher(cipher);
        admin.setBackupCodesJson(backupCodesJson);
        String adminId = administratorRepository.saveAndFlush(admin).getId();
        seededAdminIds.add(adminId);
        return adminId;
    }

    private String seedAdminWithoutMfa(String username) {
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
        String adminId = administratorRepository.saveAndFlush(admin).getId();
        seededAdminIds.add(adminId);
        return adminId;
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
        // UserType.USER 必拒 @sae.hasAuthority(ADMIN)。
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
