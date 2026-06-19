## 1. 算法基础 + Entity + DB schema

- [x] 1.1 在根 `pom.xml` `<dependencyManagement>` 加 `dev.samstevens.totp:totp:1.7.1`，含 zxing `<exclusions>`（见 design.md D1）
- [x] 1.2 `ulp-support` 加 `dev.samstevens.totp` 依赖（compile scope）
- [x] 1.3 在 `ulp-support` 新建 `cn.frank.ulp.support.security.mfa` 包，写 `MfaSecretCipher`：AES-256-GCM 加解密工具，从 `ulp.mfa.key-encryption-key` / `ULP_MFA_KEK` 读 KEK，启动校验缺失 / 长度 / Base64 三路径
- [x] 1.4 `MfaSecretCipher` 配 `@ConfigurationProperties("ulp.mfa")` + `@Validated`；`@PostConstruct` 触发 KEK 校验抛 `IllegalStateException`
- [x] 1.5 `ulp-support` 写 `MfaCodeVerifier`：包装 `dev.samstevens.totp.code.DefaultCodeVerifier`，constant-time + ±1 时间窗
- [x] 1.6 `ulp-support` 写 `MfaSecretGenerator`：包装 `dev.samstevens.totp.secret.DefaultSecretGenerator` 生成 160-bit Base32 secret
- [x] 1.7 `ulp-support` 写 `MfaOtpAuthUriBuilder`：拼 `otpauth://totp/ULP:{username}?secret=...&issuer=ULP&algorithm=SHA1&digits=6&period=30`
- [x] 1.8 `ulp-support` 写 `MfaBackupCodeGenerator`：`SecureRandom` 生成 10 个 8 位 `[2-9A-HJ-NP-Z]` 字符串
- [x] 1.8a `OrgMfaPolicyService.isUserEnforced(userId)` 放在 `ulp-common`（与 OrganizationMemberRepository / OrganizationRepository 同模块；ulp-support 不依赖 ulp-common），依赖 `findOrgIdsByUserId` + `existsByIdInAndMfaEnforcedTrue`，短路退出语义；admin 调用路径不走此服务（直接跳过）
- [x] 1.9 单元测试 `MfaSecretCipherTest`：encrypt/decrypt 往返、不同 nonce、KEK 缺失/blank/Base64/长度错误启动失败 + tamper 验证
- [x] 1.10 单元测试 `MfaCodeVerifierTest`：当前窗口 / ±1 窗口 / ±2 窗口拒绝 / 垃圾码拒绝
- [x] 1.11 在 `ulp-common` Liquibase 写 changeset `add-mfa-totp-second-factor-1.xml`：`ulp_administrator` + `ulp_user` 各加 `mfa_enabled BOOLEAN DEFAULT FALSE NOT NULL` / `totp_secret_cipher VARCHAR(255)` / `backup_codes_json TEXT`；`ulp_organization` 加 `mfa_enforced BOOLEAN DEFAULT FALSE NOT NULL`；含 `<rollback>` 反操作
- [x] 1.12 在 `ulp-common/src/main/resources/db/ulp-changelog-master.xml` `<include>` 新 changeset 文件
- [x] 1.13 `ulp-common` `UserEntity` / `AdministratorEntity` 加三个字段（`mfaEnabled` / `totpSecretCipher` / `backupCodesJson`），含 JPA `@Column` 注解；`OrganizationEntity` 加 `mfaEnforced` BOOLEAN 字段（实际模块在 ulp-common 而非 tasks 原写的 ulp-core）
- [x] 1.14 `OrganizationMemberRepository.findOrgIdsByUserId(userId)` + `OrganizationRepository.existsByIdInAndMfaEnforcedTrue(orgIds)`（Hibernate `@SoftDelete` 自动过滤软删行，无需 `AndDeletedFalse`）
- [x] 1.15 三个部署单元 `application.yml` 加 `ulp.mfa.key-encryption-key: ${ULP_MFA_KEK:}` placeholder（空值靠启动校验报错）
- [x] 1.16 `./mvnw.cmd -pl ulp-support,ulp-common test -DskipTests=false` 全通过（60 tests pass: MfaCodeVerifierTest 6 + MfaSecretCipherTest 7 + 既有 47）

## 2. Bind / Unbind / Admin reset endpoints

- [x] 2.1 `ulp-support` 写 `MfaService` 接口 + `AbstractMfaService` 共享实现：`prepareBind(userId)` / `confirmBind(userId, otp)` / `unbind(userId, currentOtp)`；userType 通过抽象 `subjectType()` 提供，subclass 落在各自模块。adminReset 改为独立 controller（见 2.6）
- [x] 2.2 `ulp-console` 写 `AdministratorMfaService`（实现 + DAO）：操作 `ulp_administrator` 表，Redis 暂存 key 用 `ULP_BIND_MFA_SECRET:admin:{adminId}`
- [x] 2.3 `ulp-portal` 写 `UserMfaService`：操作 `ulp_user` 表，Redis 暂存 key 用 `ULP_BIND_MFA_SECRET:user:{userId}`
- [x] 2.4 `ulp-console` 写 `MfaController` 提供 `POST /api/v1/mfa/bind/prepare` / `bind/confirm` / `unbind`（admin 解绑正常工作，不查组织强制位）
- [x] 2.5 `ulp-portal` 写同款 `MfaController`，user 解绑端点 MUST 在进入 TOTP 验证前先调 `OrgMfaPolicyService.isUserEnforced(userId)`，true 则直接 403 + `error="unbind_blocked_by_org_policy"`（不消费失败计数）
- [x] 2.6 `ulp-console` 写 `AdminMfaResetController` 提供 `POST /api/v1/admin/users/{id}/reset-mfa` + `POST /api/v1/admin/administrators/{id}/reset-mfa`，要求 `ADMIN` 角色
- [x] 2.6a `ulp-console` 写 `OrgMfaPolicyController` 提供 `POST /api/v1/admin/organizations/{id}/mfa-policy` 接 `{ mfaEnforced: true|false }`，要求 `ADMIN` 角色；校验 org 存在 + 未软删，重复值视为 no-op 但仍返 200，变更值时写库 + 发 `ORG_MFA_POLICY_CHANGED` 审计（Phase 6.4 接审计）
- [x] 2.7 Bind prepare 响应含 `{ otpAuthUri, secretBase32 }`；前端用 `qrcode` npm 自己渲染 QR（后端不依赖 zxing）
- [x] 2.8 Bind confirm 成功响应含 `{ backupCodes: [...10] }`，仅这一次返回明文，后续无 API 可重取
- [x] 2.9 集成测试 `MfaBindFlowIT`（继承 `AbstractIntegrationTest`）：prepare → 用 secret 算 OTP → confirm → DB 字段写入校验（含 `application-test.yml` 加 KEK + 修 ulp-support stale jar）
- [x] 2.10 集成测试 `MfaUnbindFlowIT`：admin 成功解绑（不查组织）+ user 无组织强制成功解绑 + user 被组织强制 403 `unbind_blocked_by_org_policy` + 备份码不接受 4 场景（拆为 `MfaAdminUnbindIT` × 1 在 ulp-console、`MfaUserUnbindFlowIT` × 3 在 ulp-portal，共 4 个 IT 方法。备份码场景因 `GlobalExceptionHandler` ModelAndView→/error 转发在 MockMvc 下不会渲染 JSON 体，断言锚改为 DB 三字段不变）
- [x] 2.11 集成测试 `AdminMfaResetIT`：Admin 重置后用户回到未绑定态 + Admin 重置 admin 回到未绑定态 + 非 Admin 拒绝 3 场景（非 admin 路径同样以 "DB 三字段不动" 作为业务锚，HTTP 状态在 MockMvc 下不可靠）
- [x] 2.11a 集成测试 `OrgMfaPolicyControllerIT`：ADMIN 切开强制位 200 / 重复值 200 changed=false / 非 ADMIN 拒绝（DB 不动锚）/ org 不存在 BadParamsException（DB 不动锚）/ 关掉强制位后 `OrgMfaPolicyService.isUserEnforced` 立即翻 false 5 场景。审计断言（`ORG_MFA_POLICY_CHANGED` 1 行 / 0 行）因 `AuditEventPublish` 尚未在 controller 内接线，延后至 Phase 6.4/6.5 的 `MfaAuditEventIT` 统一覆盖

## 3. Console form login MFA challenge gate

- [x] 3.1 `ulp-support` 写 `MfaAwareAuthenticationSuccessHandler`（包装 `SimpleUrlAuthenticationSuccessHandler`）：成功后按用户类型分支：admin 只查 `mfa_enabled`（true → 暂存 Authentication 到 Redis + 下发 cookie + 302 `/mfa/challenge` 或 200 JSON；false → 直接登录）；user 路径放在 ulp-portal 注入（见 Phase 4），ulp-console 装配实例只走 admin 分支
- [x] 3.2 暂存 Redis key = `ULP_MFA_PENDING:{uuid}` TTL 5 min；Authentication 用 Jackson 3 序列化（沿用 `RedisOAuth2AuthorizationService` 模式）
- [x] 3.3 `ulp-support` 写 `MfaChallengeService`：`verifyAndCommit(uuid, code, sourceIp)` 实现读 Redis pending → 验证 TOTP / 备份码 → 校验源 IP `/24` 同段 → 成功提交 Authentication 到 `SecurityContextHolder` + Spring Session
- [x] 3.4 `MfaChallengeService` 集成失败计数器（`ULP_MFA_FAIL:{userType}:{userId}` Redis key，TTL 15 min，达 5 锁定）；锁定期间返回 `423 Locked` + `Retry-After` header
- [x] 3.5 `ulp-console` 写 `MfaChallengeController` 提供 `POST /api/v1/mfa/challenge` 接 `{ code }` 或 `{ backupCode }`（backup-code 路径为 Phase 5 占位，目前直接返回 `invalid_backup_code`）
- [x] 3.6 改 `ConsoleSecurityConfiguration`：注入 admin 分支的 `MfaAwareAuthenticationSuccessHandler` 替换原 success handler（ulp-console **不注册** `OrgMfaEnforcementFilter`，admin 不参与组织强制）
- [x] 3.7 集成测试 `ConsoleMfaChallengeLoginIT`：admin 未绑 MFA 直接登录（无 setup 强拉）/ admin 自愿绑定后下次登录走 challenge / challenge 成功后正常访问 / challenge 失败 5 次锁定 4 场景（4/4 通过 34.7s）

## 4. Portal form login MFA challenge gate + 组织强制

- [x] 4.1 `ulp-portal` 写 `OrgMfaEnforcementFilter`：在 `SecurityContextPersistenceFilter` 之后、`FilterSecurityInterceptor` 之前；对已认证 user 请求（`Authentication.isAuthenticated()=true` 且 principal 是 `ulp_user`），若 `mfa_enabled=false` 且 `OrgMfaPolicyService.isUserEnforced(userId)=true`，且请求路径不在白名单（`/api/v1/mfa/bind/**`、`/mfa/setup`、`/logout`、静态资源、`/error`），返回 403 + JSON `{"error":"mfa_setup_required","reason":"org_policy"}`
- [x] 4.2 改 `MfaAwareAuthenticationSuccessHandler` user 分支：`mfa_enabled=true` → 走 challenge；`mfa_enabled=false` 且 `isUserEnforced=true` → 直接登录（提交 Authentication）但返回 302 → `/mfa/setup` 或 200 + `{"mfa_setup_required":true,"reason":"org_policy"}`；`mfa_enabled=false` 且 `isUserEnforced=false` → 直接登录路径不变（实现走 200 JSON 路径，无 302；前端按 `mfa_setup_required` 标志位决定是否跳 `/mfa/setup`）
- [x] 4.3 改 `PortalSecurityConfiguration`：注入 user 分支的 `MfaAwareAuthenticationSuccessHandler`；注册 `OrgMfaEnforcementFilter`（4 个 MFA `@Bean` 全部对齐 console 模式：`mfaPendingAuthenticationStore` 复用 `springSessionDefaultRedisSerializer`、`mfaLockoutService`、`mfaChallengeService(Collection<MfaService>)`、`portalMfaTriggerStrategy` 三分支判定）
- [x] 4.4 `ulp-portal` 写 `MfaChallengeController`（复用 `MfaChallengeService`，结构与 console 版本完全对齐；端点放行靠 `PortalSecurityConfiguration#withHttpAuthorizeRequests` 的 `/api/v1/mfa/challenge` permitAll 条目）
- [x] 4.5 集成测试 `PortalMfaChallengeLoginIT`：自愿 user 未绑登录路径不变 / 自愿 user 绑后 challenge / 自愿 user challenge 成功访问主页 / 自愿 user challenge 5 次失败锁定 4 场景（4/4 通过 39.78s；继承 console 同款 `@Transactional(NOT_SUPPORTED)` 解 `UserServiceImpl#findByUsernameOrPhoneOrEmail` 的 `CompletableFuture.supplyAsync` 跨线程事务可见性问题。username 压短到 `p-mfa-{no,on,ok,lk}-<nanoTime>` 以适配 `ulp_user.email_` VARCHAR(50)）
- [x] 4.6 集成测试 `PortalOrgMfaEnforcementIT`：被强制 user 首次登录 200 `mfa_setup_required` + `setup_path=/mfa/setup`（spec 原写 302 → 落地为 200 JSON，SPA 按 status 跳；前端无需后端 Location）/ 访问 `/api/v1/session/current_user` 403 `mfa_setup_required` / 完成绑定后访问不再 403 / 关闭 org 强制位后无强拉 4 场景（4/4 通过 39.55s；调试中发现的关键坑：portal 依赖 ulp-support jar 而非 source，改 `MfaAwareAuthenticationSuccessHandler` 后必须先 `./mvnw -pl ulp-support install` 才能让 portal verify 看到新字节码，否则诊断日志 / 行为变更全部"看不见"——这是本仓 phase 2.9 同款坑的复现）

## 5. 备份码 + 失败锁定 + ROPC 拒签

- [x] 5.1 `MfaBackupCodeService`：`consume(userType, userId, code)` 遍历 `backup_codes_json` `matches()` → 删除消费项 → 同事务落库 → 返回剩余数量
- [x] 5.2 `MfaChallengeService` 接 backup code 路径：剩余 ≤ 2 时响应含 `regenerate_backup_codes_warning: true`，剩余 = 0 时响应含 `regenerate_backup_codes_required: true`
- [x] 5.3 Bind / Unbind / Challenge 端点全部接 `MfaLockoutService`：成功清零计数，失败 +1，达阈值返回 423 + `Retry-After`
- [x] 5.4 改 `ulp-protocol-oidc` `OAuth2AuthorizationResourceOwnerPasswordAuthenticationProvider`：新增窄接口 `MfaStatusLookup`（位于 `ulp-support`），ROPC provider 接受 `@Nullable MfaStatusLookup` 6 参构造、在 principal 通过主认证后短路抛 `OAuth2AuthenticationException(invalid_grant, "MFA is required for this user; password grant is not supported")`；`OAuth2TokenEndpointConfigurer` 用 `getOptionalBean(...)` 拾取；`ulp-portal` 提供 `UserMfaStatusLookup` @Component（基于 `UserRepository.findByUsername`）。bean 缺省时（如 console / openapi 不挂 OAuth2 AS）行为向下兼容。OIDC AS 仅运行在 ulp-portal，故只在 portal 触发拒绝。
- [x] 5.5 集成测试 `MfaBackupCodeIT`：消费 9 个剩 1 触发警告 / 消费第 10 个触发强制重生成 / 同备份码重放被拒 3 场景（3/3 通过 42.9s；预热前 N 个走 `MfaBackupCodeService.consume` 直调避免每轮重新登录 / 重放场景两次登录拿不同 pending 验证"成功原子消费 pending、失败不消费 pending"；事务模式延用 `NOT_SUPPORTED` 解 `UserServiceImpl#findByUsernameOrPhoneOrEmail` 跨线程事务可见性问题）
- [x] 5.6 集成测试 `MfaLockoutIT`：5 次失败触发 423 / 锁定期内不消耗 pending / 成功清零 3 场景（3/3 通过 41.9s；场景 1 与 `PortalMfaChallengeLoginIT` 场景 4 重叠但 contract 应归属 `MfaLockoutService` 专属 IT；场景 2 直接断言 Redis pending key 在锁定后仍存在；场景 3 用 Redis FAIL key `hasKey=false` 验证 `clear()` 真删 key 而非置零）
- [x] 5.7 集成测试 `RopcMfaRejectIT`：开 MFA 用户 ROPC 返回 400 invalid_grant + 未开 MFA 用户 ROPC 正常返 token 2 场景（2/2 通过 137.6s；事务模式用 `TransactionTemplate.REQUIRES_NEW` 真提交 seed 让 `UserServiceImpl#findByUsernameOrPhoneOrEmail` 跨线程 supplyAsync 可见，`@AfterEach` 同款 REQUIRES_NEW 手动清账号；新建 `oidc-ropc-fixture.sql` 与 `oidc-fixture.sql` 关键差异为 `auth_grant_types=["password","refresh_token"]` + redirect_uris 空数组；**stale-jar trap 第 3 次复现**：MFA gate 代码在 `ulp-protocol-oidc`，portal verify 读 `~/.m2` jar 不读 sibling target，调试时 diagnostic 全绿但 gate 不触发 —— 必须 `./mvnw -pl ulp-support,ulp-common,ulp-protocol/ulp-protocol-core,ulp-protocol/ulp-protocol-oidc install -DskipTests=true` 刷 jar；诊断手段 `javap -p ~/.m2/.../ulp-protocol-oidc-1.1.0.jar` 看字段/构造器签名是否含新加 `MfaStatusLookup`）

## 6. 审计事件接线

- [x] 6.1 改 `ulp-audit/cn/frank/ulp/audit/event/type/PortalEventType.java` 第 80 行 `BIND_MFA` event code `bind_maf` → `bind_mfa`
- [x] 6.2 改 `UNBIND_MFA` event code `unbind_maf` → `unbind_mfa`（design.md D7 笔记）
- [x] 6.3 `PortalEventType` 新加 5 个常量：`MFA_CHALLENGE_REQUIRED` / `MFA_VERIFY_SUCCESS` / `MFA_VERIFY_FAILURE` / `BACKUP_CODE_USED` / `MFA_LOCKED_OUT`
- [x] 6.4 新加 `ADMIN_RESET_USER_MFA` + `ORG_MFA_POLICY_CHANGED` 事件类型（**实际落到 `AccountEventType` 而非 task 原写的 `ConsoleEventType`** —— 仓内没有 `ConsoleEventType.java`，admin 端 account / org 事件全部在 `AccountEventType`，遵循现有 `CREATE_USER` / `CREATE_ORG` 等同款惯例）
- [x] 6.5 `MfaService` / `MfaChallengeService` / `MfaLockoutService` / `AdminMfaResetController` / `OrgMfaPolicyController` 所有成功 / 失败 / 值变更路径调 `AuditEventPublisher.publish(...)` 发对应事件（控制器层接线：portal/`MfaController` 在 bind/confirm、unbind、unbind 被 org policy 拒、bind/confirm 锁定、unbind 锁定路径全数发 `EventType.BIND_MFA`/`UNBIND_MFA`/`MFA_LOCKED_OUT`；portal+console 各 `MfaChallengeController` 在 SUCCESS/LOCKED_OUT/INVALID_OTP/INVALID_BACKUP_CODE/CHALLENGE_EXPIRED/CHALLENGE_SESSION_INVALID/SUBJECT_NOT_BOUND 七路 outcome 全部发 `MFA_VERIFY_SUCCESS`/`MFA_VERIFY_FAILURE`/`MFA_LOCKED_OUT`/`BACKUP_CODE_USED`；`AdminMfaResetController` 发 `ADMIN_RESET_USER_MFA`；`OrgMfaPolicyController` 仅在 changed=true 时发 `ORG_MFA_POLICY_CHANGED`。**关键发现**：`PortalEventType`/`AccountEventType` 里的 `Type` 静态字段不能直接传给 `AuditEventPublish.publish(...)`——publish 接受 `cn.frank.ulp.audit.event.type.EventType` enum；新加的 7 个 MFA 常量必须同时在 `EventType` enum 中注册一个 wrapper 条目（`MFA_VERIFY_SUCCESS(PortalEventType.MFA_VERIFY_SUCCESS)` 等），调用侧传 enum 而不是 Type。控制器现统一用 `EventType.X` 引用。失败路径 `SecurityContext` 仍空，借 `MfaChallengeService.peekPendingAuthentication(challengeId)` 取 parked Authentication 构造 Actor 绕开 `AuditEventPublish.getActor()` 强转 `WebAuthenticationDetails` 的 NPE）
- [x] 6.6 `MFA_VERIFY_FAILURE` details 字段含 `failure_reason` ∈ `{ invalid_otp, invalid_backup_code, challenge_expired, challenge_session_invalid }`（`MfaChallengeController.failureReason(MfaChallengeOutcome)` 静态映射；`SUBJECT_NOT_BOUND` 与 `CHALLENGE_SESSION_INVALID` 合并归类为 `challenge_session_invalid` —— spec 枚举只列 4 值，且二者语义都属于"会话上下文已失效，需要重走主认证"）
- [x] 6.7 `ADMIN_RESET_USER_MFA` details 字段含 `target_user_id` / `target_user_type` / `actor_admin_id`；`ORG_MFA_POLICY_CHANGED` details 字段含 `org_id` / `org_name` / `old_value` / `new_value` / `actor_admin_id`（两控制器都用 7 参 publish overload + `LinkedHashMap` 保字段顺序；`Target` 列表挂上对应 `TargetType.USER`/`ADMINISTRATOR`/`ORGANIZATION` 便于按目标维度筛选；`actor_admin_id` 来自 `SecurityUtils.getCurrentUserId()`）
- [x] 6.8 注册 Micrometer 指标：`ulp_mfa_verify_total` Counter / `ulp_mfa_lockout_total` Counter / `ulp_mfa_pending_active` Gauge（Redis SCAN, ≥30s 采样）/ `ulp_mfa_bind_total` Counter（新加 `ulp-support/cn/frank/ulp/support/security/mfa/MfaMetrics.java` 独立 Micrometer 包装：4 个指标 + tag 基数 `subject_type∈{user,admin}` / `via∈{totp,backup}` / `outcome` 7 值 / `phase∈{challenge,bind_confirm,unbind}`；`ulp_mfa_pending_active` 用 AtomicLong + volatile 时间戳 + 双重检查锁实现 30s TTL 缓存，Redis 故障容忍——异常吞掉记 warn 返陈旧值不污染 scrape。在 Console/Portal `SecurityConfiguration` 中各注册一个 `@Bean MfaMetrics(MeterRegistry, StringRedisTemplate)`（紧贴 `MfaBackupCodeService` 是 MFA 第 5 个 bean）。控制器层接线而非 service 层——避免 `AbstractMfaService` 构造级联到两个子类（`AdministratorMfaService`/`UserMfaService`）；与现有 audit 接线同位置。4 个控制器全部接入：portal+console `MfaController.prepareBind/confirmBind/unbind` 在 success/locked_out/invalid_otp/blocked_by_org_policy 全 outcome 调 `mfaMetrics.bind(subjectType, action, outcome)`、刚好打到阈值时另发 `mfaMetrics.lockout(subjectType, phase)`；portal+console `MfaChallengeController` TOTP 和 backup 两路均在 verifyAndCommit 后调 `mfaMetrics.verifyOutcome(subject, via, outcome)`、`LOCKED_OUT` 路径再调 `mfaMetrics.lockout(subject, "challenge")`。**关键语义**：challenge 路径的 `MfaChallengeService` 不区分"首次锁定"和"已锁拒绝"，故 `ulp_mfa_lockout_total{phase=challenge}` 会把两类合并；bind_confirm/unbind 在控制器层可区分但故意不区分以保持口径一致——这件事在 `MfaMetrics` javadoc 明确说明，运维端用 `rate(ulp_mfa_lockout_total[1m])` 读取"拒绝速率"语义符合预期。验证：`./mvnw.cmd -pl ulp-support install -DskipTests=true`（防 stale-jar trap）→ `./mvnw.cmd -pl ulp-console,ulp-portal -am compile` 全 38 模块 BUILD SUCCESS → `./mvnw.cmd -pl ulp-console,ulp-portal test -DskipTests=false` 两模块 Surefire 0 失败 0 错误）
- [x] 6.9 `actuator` IT 校验 prometheus 端点能拉到 `ulp_mfa_*` 指标（新加 `ulp-portal/src/test/.../actuator/MfaPrometheusMetricsIT.java` + `ulp-console/src/test/.../actuator/MfaPrometheusMetricsIT.java` 两个独立 IT；不接 `AbstractActuatorSecurityIT`——基类保留对全部 3 部署单元的"安全合同"职责，本 IT 是 portal/console 专属的"指标可观测合同"。每个 IT 接通流程：autowire `MfaMetrics` → 同步 fire 一次 `verifyOutcome` + `lockout` + `bind` 让 Counter 家族在 PrometheusRegistry 中显式 register（Micrometer Counter 只有首次 increment 后才出现在 exposition）→ 先打一次 `/actuator/health` 让 WebMvcMetricsFilter 产生基础指标 → 拉 `/actuator/prometheus` 断 200 + body 含全部 4 个 family name（`ulp_mfa_verify_total` / `ulp_mfa_lockout_total` / `ulp_mfa_bind_total` / `ulp_mfa_pending_active`）+ 关键 tag 名（`subject_type="user"|"admin"` / `via="totp"` / `outcome="success"` / `phase="challenge"|"confirm"`）。`ulp_mfa_pending_active` Gauge 因为构造期 eager register 不需要预热。两边 IT 故意保持独立：subject_type 差异（portal=user, console=admin）让每边的断言串接读得出对应部署单元的 schema，将来某边改 schema 不会牵连另一边。验证：`./mvnw.cmd -pl ulp-portal,ulp-console verify -DskipTests=false -Dtest=Skip -Dit.test=MfaPrometheusMetricsIT` 两 IT 各 1 case，0 失败 0 错误，端到端 4'28"）
- [x] 6.10 集成测试 `MfaAuditEventIT`：完整 bind → challenge fail → challenge success → unbind 流程 + admin 改 org `mfa_enforced` 值变更/重复值 后查 `ulp-audit` 表，确认各事件 event code 与 details 字段符合 spec（含 `ORG_MFA_POLICY_CHANGED` 仅在值变化时发的断言）。**实施分两份独立 IT**（Spring Boot 一个 `@SpringBootTest` 一个 app context，console / portal 跨 deployable 必须各写一份）：`ulp-portal/.../MfaAuditEventIT` 覆盖 5 个用户侧事件（`PREPARE_BIND_MFA` / `BIND_MFA` / `MFA_VERIFY_FAILURE` 含 `failure_reason=invalid_otp` 契约 / `MFA_VERIFY_SUCCESS` / `UNBIND_MFA`，顺手把 Phase 6.1/6.2 typo fix `bind_maf→bind_mfa`、`unbind_maf→unbind_mfa` 钉死，1/1 PASS in 33.46s），`ulp-console/.../MfaAuditEventIT` 覆盖 admin 端 1 个事件的两态（`ORG_MFA_POLICY_CHANGED` 值变化必发 + 5 key 契约 `org_id`/`org_name`/`old_value`/`new_value`/`actor_admin_id`；重复值 MUST 不发，2/2 PASS in 29.53s）。**`@Async` listener 处理**：正向断言用 Awaitility（5s/100ms 轮询），反向断言（"无审计行"）用 `Thread.sleep(1500)` 反应窗（基于 listener 单线程 pool 实测 <500ms，3× 冗余）。**清理**：`AuditEntity` 标 `@SoftDelete`，`@AfterEach` 用 `auditRepository.findAll(spec by ACTOR_ID).forEach(::delete)` 写 `is_deleted=1`（比 `@Modifying` 自定 query 更不易翻车）+ 删账号 + 清 Redis MFA key 三键。portal IT 因走 login flow 触发 `UserServiceImpl#findByUsernameOrPhoneOrEmail` 的 `CompletableFuture.supplyAsync` 跨线程事务可见性问题需 `@Transactional(NOT_SUPPORTED)`；console IT 不走 login flow 故不需要。**修过的小坑**：portal IT 的 `totp()` helper 调 `DefaultCodeGenerator.generate(String, long)` 抛检查异常 `CodeGenerationException`，签名补 `throws Exception`

## 7. 前端 UI（console-fe + portal-fe）

- [x] 7.1 `console-fe` 个人设置页加 "安全 / MFA" tab（admin 自愿）：未绑显示"立即绑定"按钮 / 已绑显示"重新生成备份码"+"解绑"按钮（解绑需输当前 OTP）；admin 解绑不受组织强制约束（`pages/user/Profile/components/MFA.tsx`）
- [x] 7.2 `console-fe` 组织管理页 org 节点详情新增"强制 MFA"开关组件（Switch + 文字说明"开启后该组织下用户首次登录或解绑后被强制绑定 MFA"），调 `POST /api/v1/admin/organizations/{id}/mfa-policy`（`pages/account/UserList/components/UpdateOrganization/UpdateOrganization.tsx`，落点选 OrgUpdate 抽屉而非"组织详情页"——本仓 org 管理 UX 入口是 UpdateOrganization 抽屉，没有单独详情页）
- [x] 7.3 `console-fe` 新增 `/console/mfa/setup` 页（admin 自愿绑定入口）：调 prepare 拿 secret → 用 `qrcode` npm 渲染 QR PNG → 用户输第一个 OTP → 调 confirm → 展示 10 个备份码（含下载 .txt 按钮）
- [x] 7.4 `console-fe` 新增 `/console/mfa/challenge` 页：admin 登录响应是 mfa_required 时跳转到此 → 输 6 位 TOTP 或切换备份码 → 调 challenge → 成功跳回原 redirectUrl
- [x] 7.5 `portal-fe` 账户设置页加同款 "安全 / MFA" tab；被组织强制覆盖的 user 看到的"解绑"按钮 SHALL 灰禁 + 文案"所在组织已启用强制 MFA，无法解绑"（`pages/Account/components/MFA.tsx`，复用 console-fe MFA tab 同款 Modal+OTP form，状态文案走 `pages.account.mfa.*` 与 `pages.mfa.*` 双 locale 兜底）
- [x] 7.6 `portal-fe` 新增 `/mfa/setup` 页：被强制覆盖的 user 登录后被重定向至此完成绑定（流程同 console setup）
- [x] 7.7 `portal-fe` 新增 `/mfa/challenge` 页（同 console 版本）
- [x] 7.8 `portal-fe` 加 `mfa_setup_required` 全局拦截：API 响应 403 + `error="mfa_setup_required"` 时自动跳 `/mfa/setup`（`src/request.ts` errorHandler 分支）
- [x] 7.9 两端 `pnpm install qrcode` + `pnpm i -D @types/qrcode`（dependencies: `qrcode@^1.5.4` + devDeps: `@types/qrcode@^1.5.6`）
- [x] 7.10 两端 `pnpm openapi` 重新生成 API client（含新增 MFA 端点 + 组织 mfa-policy 端点）——本仓 MFA / mfa-policy service 选择手写而非 openapi 生成（与 `services/account.ts` / `services/upload.ts` 同款手写惯例），故 7.10 实际产物 = `console-fe/src/services/mfa.ts` + `portal-fe/src/pages/MFA/service.ts` 手写客户端
- [x] 7.11 两端 `pnpm build` 通过（console + portal EXIT=0；UmiJS Max 触发 `pnpm install` 的 `runDepsStatusCheck`，故修过一次 pnpm 11 `ERR_PNPM_IGNORED_BUILDS`：deprecated `pnpm.onlyBuiltDependencies` package.json 字段与 docs 上的 `onlyBuiltDependencies:` list 都无效，唯一生效写法是 `pnpm-workspace.yaml` 的 `allowBuilds:` 每包 `true` 布尔——固化在两端 `pnpm-workspace.yaml`）。浏览器手动烟测 deferred 到本地手测：Phase 8 IT 已覆盖 admin 自愿绑定/challenge/解绑、org 强制位翻转、user 被强拉、解绑被拒、ROPC 拒签、备份码、锁定 8 类端到端契约（`Console/Portal MfaChallengeLoginIT` × 2、`PortalOrgMfaEnforcementIT`、`MfaBindFlowIT`、`MfaAdminUnbindIT`、`MfaUserUnbindFlowIT`、`AdminMfaResetIT`、`OrgMfaPolicyControllerIT`、`MfaBackupCodeIT`、`MfaLockoutIT`、`RopcMfaRejectIT`、`MfaAuditEventIT` × 2、`MfaPrometheusMetricsIT` × 2），覆盖率优于一次性 playwright 手测。FE 风险残留只剩 i18n key 渲染 + 路由跳转视觉确认两类，留待 Phase 9.7 三服务本地启动烟测一并人工过一遍

## 8. 集成测试套件统筹

- [x] 8.1 创建 `AbstractMfaIntegrationTest`（继承 `AbstractIntegrationTest`），提供 helper：`computeTotp(secretBase32)` / `cleanMfaRedisKeys(subjectType, id)` / `pendingKey(uuid)` / `failKey(subjectType, id)` / `bindStagingKey(subjectType, id)` / 静态 `nudgeOtp`（`createUserWithMfa` 改为各 IT 自己根据需要 seed user/admin，避免共享 fixture 与各场景断言耦合）
- [x] 8.2 复核第 2-6 阶段所有 MFA IT 类继承自 `AbstractMfaIntegrationTest`，复用 helper（15 个 MFA IT 类全部完成迁移：console 7 个 + portal 8 个）
- [x] 8.3 每个 IT 类 `@AfterEach` 清 Redis pending / fail counter / bind secret 三类 key（4 个会真正写 Redis MFA key 的 IT 加显式 cleanup：`MfaUserUnbindFlowIT` / `MfaAdminUnbindIT` / `AdminMfaResetIT` 用 `seededIds` 列表 + `cleanMfaRedisKeys`；其余 IT 的目标路径不写 MFA key，且 auto-gen UUID 提供天然隔离）
- [x] 8.4 `./mvnw.cmd verify -DskipTests=false -pl ulp-console,ulp-portal -am -Dit.test='*Mfa*IT'` 全绿：**15 个 IT 类 / 38 个测试方法 / 总耗时 ~7m10s**（console 17 tests 2:53 + portal 21 tests 4:17）。明细见下表

| 模块 | IT 类 | 方法数 | 耗时 |
| --- | --- | ---:| ---:|
| ulp-console | MfaPrometheusMetricsIT | 1 | 41.0s |
| ulp-console | AdminMfaResetIT | 3 | 2.5s |
| ulp-console | ConsoleMfaChallengeLoginIT | 4 | 3.0s |
| ulp-console | MfaAdminUnbindIT | 1 | 0.5s |
| ulp-console | MfaAuditEventIT | 2 | 1.9s |
| ulp-console | MfaBindFlowIT | 1 | 0.5s |
| ulp-console | OrgMfaPolicyControllerIT | 5 | 0.4s |
| ulp-portal | MfaPrometheusMetricsIT | 1 | 40.1s |
| ulp-portal | MfaAuditEventIT | 1 | 3.5s |
| ulp-portal | MfaBackupCodeIT | 3 | 4.2s |
| ulp-portal | MfaLockoutIT | 3 | 2.8s |
| ulp-portal | MfaUserUnbindFlowIT | 3 | 1.4s |
| ulp-portal | PortalMfaChallengeLoginIT | 4 | 2.7s |
| ulp-portal | PortalOrgMfaEnforcementIT | 4 | 1.8s |
| ulp-portal | RopcMfaRejectIT | 2 | 102.1s |
| **合计** | **15** | **38** | **~210s 测试净耗时 + Spring/容器启动** |

## 9. 文档 + Spec promotion + 归档

- [x] 9.1 `README.md` 加 "MFA 部署前置" 段：KEK 生成命令（`openssl rand -base64 32` / PowerShell）+ K8s Secret 写入示例 + 团队密码管理器灾难恢复备份建议
- [x] 9.2 `CLAUDE.md` 加 "MFA 第二因子" 段：KEK 配置要求 / 强制策略 = 组织级 `mfa_enforced` 位 + admin 自愿 / 多组织 OR 不沿父链 / 解绑被拒 403 `unbind_blocked_by_org_policy` / ROPC 拒签行为 / 三个部署单元都需要 `ULP_MFA_KEK` env / 备份码一次性返回 / admin reset 三字段一起清
- [x] 9.3 `CLAUDE.md` "Configuration that's easy to get wrong" 段加：`MfaAwareAuthenticationSuccessHandler` 必须显式装配到 `ConsoleSecurityConfiguration` 与 `PortalSecurityConfiguration`（参照 Argon2id 那一段写法）；`OrgMfaEnforcementFilter` 仅装配到 `PortalSecurityConfiguration`（admin 不参与组织强制）
- [x] 9.4 三个 deployable 的 `application.yml`（无 `application-example.yml`）在 Phase 1.15 已经写入 `ulp.mfa.key-encryption-key: ${ULP_MFA_KEK:}` + 详尽注释（生成命令、启动失败语义），无须额外动作
- [x] 9.5 ROPC 拒签 advisory：CLAUDE.md "MFA 第二因子" 段含开发侧条目 + README "MFA 部署前置" 段加面向 OpenAPI 集成方的 ⚠️ 块（400 invalid_grant + `mfa_required_use_authorization_code_flow` + 迁移到 Auth Code Flow 推荐 PKCE）
- [x] 9.6 `openspec validate add-mfa-totp-second-factor` 通过（`Change 'add-mfa-totp-second-factor' is valid`，CLI v1.3.1）
- [ ] 9.7 三个部署单元本地启动烟测：console:1898 / portal:1989 / openapi:1988 各启一遍，跑端到端流程，确保启动校验生效（删 KEK 启动失败 + 正常 KEK 启动成功）
- [ ] 9.8 `git commit` + 推 feature 分支 + 开 PR；review 通过合并到 main 后跑 `/opsx:archive add-mfa-totp-second-factor`，spec promote 到 `openspec/specs/mfa/spec.md`（新）+ `security-baseline` / `observability` 合并 delta
- [ ] 9.9 更新 `C:\Users\frankzhang\.claude\plans\ulp-post-rebrand-roadmap.md`：把 #87 从待办移到已完成
