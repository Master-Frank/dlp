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
package cn.frank.ulp.portal.protocol.oidc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.support.security.mfa.MfaSecretCipher;
import cn.frank.ulp.support.security.mfa.MfaSecretGenerator;
import cn.frank.ulp.support.testsupport.AbstractMfaIntegrationTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Phase 5.7 端到端 IT —— 覆盖 {@code OAuth2AuthorizationResourceOwnerPasswordAuthenticationProvider}
 * 在 {@code POST /api/v1/authorize/{appCode}/oauth2/token} 上对 MFA 已启用账号的拒签契约：
 *
 * <ol>
 *   <li>{@link #mfaEnabledUser_ropcGrant_isRejectedWithInvalidGrant()} —
 *       {@code mfa_enabled=true} 的 user 走 password grant → 400 {@code invalid_grant} +
 *       {@code error_description} 含 "MFA"（spec.md Phase 5.4 安全约束：password grant
 *       没有收集第二因子的路径，必须直接拒签，避免绕过 MFA）</li>
 *   <li>{@link #nonMfaUser_ropcGrant_returnsAccessToken()} —
 *       {@code mfa_enabled=false} 的 user 走 password grant → 200 + 非空 access_token，
 *       保证拒签开关不会误伤普通用户</li>
 * </ol>
 *
 * <p><b>事务模式：</b> 继承父类 {@code @Transactional}（默认 REQUIRED，测试结束回滚），
 * 不再像 {@link cn.frank.ulp.portal.controller.security.PortalMfaChallengeLoginIT} 那样
 * 用 {@code NOT_SUPPORTED} 整体禁事务。原因：ROPC 路径在 {@code OidcAuthorizationServerContextFilter}
 * 里通过 {@code ApplicationServiceLoader.getApplicationServiceByAppCode} → {@code AppRepository.findByCode}
 * 触发 Spring {@code @Cacheable} 写 Redis，Jackson 序列化 {@code AppEntity} 时要读 lazy 关联
 * {@code groups}；{@code NOT_SUPPORTED} 会挂起测试事务，Hibernate session 在 cache 写出前关闭，
 * 抛 {@code LazyInitializationException}。
 *
 * <p>但 {@code UserServiceImpl#findByUsernameOrPhoneOrEmail} 是 {@code CompletableFuture.supplyAsync}
 * 跨线程，看不到测试事务内 uncommitted 的 seed user。解决方案：seed/cleanup 走
 * {@link TransactionTemplate} + {@code PROPAGATION_REQUIRES_NEW}，把账号真提交到 DB（独立事务，
 * 不受测试事务回滚影响），由 {@code @AfterEach} 在同款 REQUIRES_NEW 事务里手动清账号。
 *
 * <p><b>App 种子：</b> {@code @Sql} 加载 {@code oidc-ropc-fixture.sql} —— 与 {@code oidc-fixture.sql}
 * 的关键差异是 {@code auth_grant_types='["password","refresh_token"]'}。Sql 脚本带幂等 DELETE，
 * 同 JVM 重跑也安全。
 *
 * <p><b>username 长度约束：</b> {@code ulp_user.email_} 是 VARCHAR(50)，
 * email 拼成 "{username}@example.com" 需保证 ≤ 38 字符；用 {@code r-mfa-{on,no}-<nanoTime>} 前缀。
 *
 * <p><b>Stale-jar trap (本仓 phase 2.9 / 4.6 同款坑第 3 次复现):</b> MFA gate 代码在
 * {@code ulp-protocol/ulp-protocol-oidc}，{@code UserMfaStatusLookup} 在 portal 自己模块。
 * portal verify 会从 {@code ~/.m2/repository/cn/frank/ulp/ulp-protocol-oidc/} 拉 jar，而不是
 * 直接读 sibling module 的 target。改完 OIDC / support 源后必须先：
 * <pre>./mvnw.cmd -pl ulp-support,ulp-common,ulp-protocol/ulp-protocol-core,ulp-protocol/ulp-protocol-oidc install -DskipTests=true -Dfrontend.skip=true -Dlicense.skip=true -Dformatter.skip=true -Dimpsort.skip=true</pre>
 * 否则 portal IT 看到的还是老字节码 ——"代码看对了但 gate 不触发"的症状几乎必然指向这个。
 * 诊断手段：{@code javap -p} 反编译 {@code ~/.m2} 下的 jar 看字段 / 方法签名是否含新加入的
 * MfaStatusLookup 6-arg 构造器。
 */
@ActiveProfiles("test")
@Sql(scripts = "/db/oidc-ropc-fixture.sql")
class RopcMfaRejectIT extends AbstractMfaIntegrationTest {

    private static final String        APP_CODE       = "test-ropc-app";
    private static final String        CLIENT_ID      = "ropc-client";
    private static final String        CLIENT_SECRET  = "ropc-secret";
    private static final String        TOKEN_ENDPOINT = "/api/v1/authorize/" + APP_CODE
                                                        + "/oauth2/token";

    @Autowired
    private UserRepository             userRepository;

    @Autowired
    private MfaSecretGenerator         mfaSecretGenerator;

    @Autowired
    private MfaSecretCipher            mfaSecretCipher;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private TransactionTemplate        requiresNewTx;

    private String                     seededUserId;

    private TransactionTemplate requiresNewTx() {
        if (requiresNewTx == null) {
            requiresNewTx = new TransactionTemplate(transactionManager);
            requiresNewTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        }
        return requiresNewTx;
    }

    @AfterEach
    void cleanup() {
        if (seededUserId != null) {
            try {
                requiresNewTx()
                    .executeWithoutResult(status -> userRepository.deleteById(seededUserId));
            } catch (RuntimeException ignored) {
                // 不阻塞下一个 test —— nanoTime 唯一 username 避免冲突
            }
            seededUserId = null;
        }
    }

    /**
     * 场景 1：MFA 已启用 user 走 ROPC → 400 invalid_grant + error_description 含 "MFA"。
     *
     * <p>路径：{@code OAuth2AuthorizationResourceOwnerPasswordAuthenticationProvider#authenticate}
     * L191-203 —— 主认证通过后 {@code MfaStatusLookup.isMfaEnabled(principal.getName())} 返 true
     * 即抛 {@code OAuth2AuthenticationException(invalid_grant,"MFA is required...")}，
     * Spring Auth Server 的 {@code OAuth2ErrorHttpMessageConverter} 把异常落成 400 JSON
     * {@code {error, error_description, error_uri}}。
     */
    @Test
    void mfaEnabledUser_ropcGrant_isRejectedWithInvalidGrant() throws Exception {
        String rawPassword = "RopcRejectMfa@Pwd-12345";
        String username = "r-mfa-on-" + System.nanoTime();
        seedUser(username, rawPassword, true);

        mockMvc
            .perform(
                post(TOKEN_ENDPOINT).header("Authorization", basicAuth(CLIENT_ID, CLIENT_SECRET))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("grant_type", "password").param("username", username)
                    .param("password", rawPassword).param("scope", "openid"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("invalid_grant")).andExpect(
                jsonPath("$.error_description").value(org.hamcrest.Matchers.containsString("MFA")));
    }

    /**
     * 场景 2：未开 MFA 的 user 走 ROPC → 200 + 非空 access_token。
     * 保证拒签开关只对 {@code mfa_enabled=true} 触发，不误伤普通账号。
     */
    @Test
    void nonMfaUser_ropcGrant_returnsAccessToken() throws Exception {
        String rawPassword = "RopcAllowNoMfa@Pwd-12345";
        String username = "r-mfa-no-" + System.nanoTime();
        seedUser(username, rawPassword, false);

        mockMvc
            .perform(
                post(TOKEN_ENDPOINT).header("Authorization", basicAuth(CLIENT_ID, CLIENT_SECRET))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("grant_type", "password").param("username", username)
                    .param("password", rawPassword).param("scope", "openid"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.access_token").isNotEmpty())
            .andExpect(jsonPath("$.token_type").value("Bearer"));
    }

    /**
     * Seed 一条 ENABLED user：bcrypt 真编码密码、按入参决定 {@code mfa_enabled} +
     * （若开启）写一份加密 TOTP secret 占位（拒签路径不会真用到这把 secret，但落库一份与生产语义对齐）。
     */
    private void seedUser(String username, String rawPassword, boolean mfaEnabled) {
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
        user.setMfaEnabled(mfaEnabled);
        if (mfaEnabled) {
            String secretBase32 = mfaSecretGenerator.generate();
            user.setTotpSecretCipher(
                mfaSecretCipher.encrypt(secretBase32.getBytes(StandardCharsets.UTF_8)));
        }
        seededUserId = requiresNewTx().execute(status -> userRepository.saveAndFlush(user).getId());
        assertThat(seededUserId).as("seed user 应拿到非空 id").isNotBlank();
    }

    private static String basicAuth(String user, String pass) {
        String token = Base64.getEncoder()
            .encodeToString((user + ":" + pass).getBytes(StandardCharsets.UTF_8));
        return "Basic " + token;
    }
}
