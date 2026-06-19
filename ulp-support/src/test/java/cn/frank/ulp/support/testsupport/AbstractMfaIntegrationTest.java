/*
 * ulp-support - ULP support library
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
package cn.frank.ulp.support.testsupport;

import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.CodeGenerationException;

/**
 * MFA 集成测试共享基类：把 14 个 Phase 2-6 MFA IT 反复手写的 TOTP 计算 / Redis key 前缀 / Redis 清理
 * 三组样板抽到一处。
 *
 * <h2>设计取舍</h2>
 * <ul>
 *   <li><b>不引入 entity / DTO 依赖</b>：本基类住在 {@code ulp-support} test 源，下游 console / portal
 *       两个 deployable 都依赖它的 test-jar；如果让基类持有 {@code UserEntity} / {@code AdministratorEntity}
 *       这类只在 {@code ulp-common} 出现的实体，{@code ulp-support} test scope 必须反向加 {@code ulp-common}
 *       依赖，破坏分层。所以 seed 用户的逻辑仍留在各 IT，本基类只提供"和实体无关"的工具。</li>
 *   <li><b>不收敛 {@code mockUserAuth} / {@code mockAdminAuth}</b>：{@code UserDetails} 也住在上游模块，
 *       同上理由。</li>
 *   <li><b>subjectType 用 string 而非 enum</b>：与生产代码 {@link cn.frank.ulp.support.security.mfa.MfaService#subjectType()}
 *       同款约定，{@code "user"} 或 {@code "admin"}。基类做大小写归一 + 防御性校验。</li>
 *   <li><b>TOTP generator stateless</b>：{@link DefaultCodeGenerator} 自身无状态，复用一个 instance 没有
 *       并发风险，每个 IT 不再各自 {@code new} 一份。</li>
 * </ul>
 *
 * <h2>跨测试隔离</h2>
 * <p>{@link #cleanMfaRedisKeys(String, String)} 是 best-effort —— 删 {@code ULP_BIND_MFA_SECRET:{type}:{id}}
 * 与 {@code ULP_MFA_FAIL:{type}:{id}} 两条精确 key，{@code ULP_MFA_PENDING:{uuid}} 因 key 后缀是
 * 随机 UUID 不能按 subjectId 反推，由各 IT 自己 cookie 路径回收 / 或在 challenge 成功路径靠
 * {@code MfaPendingAuthenticationStore.delete} 自动 consume；本基类不做 SCAN，避免在
 * 共享容器 + reuse 模式下误删并发跑的其他 test 的 pending。</p>
 *
 * <h2>事务行为</h2>
 * <p>不覆盖父类的 {@code @Transactional}。各 IT 视自身 seed 路径是否触发跨线程 {@code CompletableFuture.supplyAsync}
 * （portal 的 {@code UserServiceImpl#findByUsernameOrPhoneOrEmail} / console 的 {@code AdministratorServiceImpl}
 * 同款）来决定是否在 IT 类上额外加 {@code @Transactional(propagation = NOT_SUPPORTED)}；本基类对此保持中立。</p>
 */
public abstract class AbstractMfaIntegrationTest extends AbstractIntegrationTest {

    /** 与 {@code MfaPendingAuthenticationStore.KEY_PREFIX} 一致。 */
    public static final String                PENDING_KEY_PREFIX  = "ULP_MFA_PENDING:";

    /** 与 {@code MfaLockoutService.KEY_PREFIX} 一致。 */
    public static final String                FAIL_KEY_PREFIX     = "ULP_MFA_FAIL:";

    /** 与 {@code AbstractMfaService.STAGING_KEY_PREFIX} 一致。 */
    public static final String                BIND_KEY_PREFIX     = "ULP_BIND_MFA_SECRET:";

    /** 与生产 TOTP 校验同款 30 秒时间步。 */
    public static final long                  TOTP_STEP_SECONDS   = 30L;

    private static final Set<String>          VALID_SUBJECT_TYPES = Set.of("user", "admin");

    private static final DefaultCodeGenerator TOTP_GENERATOR      = new DefaultCodeGenerator(
        HashingAlgorithm.SHA1);

    @Autowired
    protected StringRedisTemplate             redisTemplate;

    /**
     * 计算当前 30s 窗口的 6 位 TOTP，给"刚 seed → 立即提交"路径用。
     *
     * @param secretBase32 RFC 4648 Base32 编码的 TOTP secret（{@code MfaSecretGenerator.generate()} 产物）
     * @return 6 位数字 OTP
     */
    protected final String computeTotp(String secretBase32) {
        return computeTotpAtUnixSeconds(secretBase32, System.currentTimeMillis() / 1000L);
    }

    /**
     * 计算指定 unix 时间点所属窗口的 TOTP，给"时间窗口越界 / ±1 步" 类断言用。
     *
     * @param secretBase32 RFC 4648 Base32 TOTP secret
     * @param unixSeconds  unix epoch 秒数（{@link System#currentTimeMillis()} / 1000）
     * @return 6 位数字 OTP
     */
    protected final String computeTotpAtUnixSeconds(String secretBase32, long unixSeconds) {
        Objects.requireNonNull(secretBase32, "secretBase32");
        long counter = unixSeconds / TOTP_STEP_SECONDS;
        try {
            return TOTP_GENERATOR.generate(secretBase32, counter);
        } catch (CodeGenerationException e) {
            // SHA1 + RFC 4648 secret 走到这里只可能是密钥串非 Base32 —— 测试 setup bug，直接炸。
            throw new IllegalStateException(
                "TOTP code generation failed for the provided Base32 secret", e);
        }
    }

    /**
     * 把一个正确的 6 位 OTP 末位 +1 mod 10，得到一个保证落出当前 30s 窗口的错码。
     *
     * <p>所有 IT 的"故意失败"路径都用这条规则，规避"随机生成的错码偶尔命中下一窗口的合法 OTP"
     * （30s 窗口边缘 ±1 偏移 + verifier 默认接受 ±1 窗口的小概率事件）。</p>
     *
     * @param correctOtp 6 位 [0-9] 数字
     * @return 同长度、最后一位被替换的错码
     */
    protected static String nudgeOtp(String correctOtp) {
        Objects.requireNonNull(correctOtp, "correctOtp");
        if (correctOtp.length() < 1) {
            throw new IllegalArgumentException("correctOtp must not be empty");
        }
        char[] chars = correctOtp.toCharArray();
        char last = chars[chars.length - 1];
        if (last < '0' || last > '9') {
            throw new IllegalArgumentException("correctOtp last char must be a digit");
        }
        chars[chars.length - 1] = (char) ('0' + ((last - '0') + 1) % 10);
        return new String(chars);
    }

    /** Pending 表 key（uuid 取自 cookie `ulp-mfa-pending`）。 */
    protected static String pendingKey(String challengeUuid) {
        return PENDING_KEY_PREFIX + Objects.requireNonNull(challengeUuid, "challengeUuid");
    }

    /**
     * Brute-force 失败计数 key。
     *
     * @param subjectType {@code "user"} 或 {@code "admin"}（大小写不敏感）
     * @param subjectId   user/admin 主键 id
     */
    protected static String failKey(String subjectType, String subjectId) {
        return FAIL_KEY_PREFIX + normalizeSubjectType(subjectType) + ":"
               + Objects.requireNonNull(subjectId, "subjectId");
    }

    /**
     * Bind prepare 暂存 secret key（{@code MfaService.prepareBind} 写入，{@code confirmBind} 读取后删除）。
     *
     * @param subjectType {@code "user"} 或 {@code "admin"}（大小写不敏感）
     * @param subjectId   user/admin 主键 id
     */
    protected static String bindStagingKey(String subjectType, String subjectId) {
        return BIND_KEY_PREFIX + normalizeSubjectType(subjectType) + ":"
               + Objects.requireNonNull(subjectId, "subjectId");
    }

    /**
     * 删除指定 subject 的 FAIL + BIND 两条精确 key。
     *
     * <p>PENDING key 后缀是 UUID，无法按 subjectId 反推；challenge 成功路径会被
     * {@code MfaPendingAuthenticationStore.delete} 自动 consume，未消费的会在 5 分钟后自然过期。
     * 各 IT 若需精确清，应在收到 pending cookie 时记录其 UUID 并在 {@code @AfterEach} 显式 {@code redisTemplate.delete(pendingKey(uuid))}。</p>
     *
     * @param subjectType {@code "user"} 或 {@code "admin"}
     * @param subjectId   user/admin 主键 id；{@code null} 时整个调用 no-op，方便 {@code @AfterEach}
     *                    在 seed 步骤抛错的情况下直接调用而不必先 null 检查
     */
    protected final void cleanMfaRedisKeys(String subjectType, String subjectId) {
        if (subjectId == null) {
            return;
        }
        String type = normalizeSubjectType(subjectType);
        redisTemplate.delete(FAIL_KEY_PREFIX + type + ":" + subjectId);
        redisTemplate.delete(BIND_KEY_PREFIX + type + ":" + subjectId);
    }

    private static String normalizeSubjectType(String subjectType) {
        Objects.requireNonNull(subjectType, "subjectType");
        String normalized = subjectType.toLowerCase(Locale.ROOT);
        if (!VALID_SUBJECT_TYPES.contains(normalized)) {
            throw new IllegalArgumentException(
                "subjectType must be one of " + VALID_SUBJECT_TYPES + ", got: " + subjectType);
        }
        return normalized;
    }
}
