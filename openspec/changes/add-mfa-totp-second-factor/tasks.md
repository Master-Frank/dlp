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

- [ ] 3.1 `ulp-support` 写 `MfaAwareAuthenticationSuccessHandler`（包装 `SimpleUrlAuthenticationSuccessHandler`）：成功后按用户类型分支：admin 只查 `mfa_enabled`（true → 暂存 Authentication 到 Redis + 下发 cookie + 302 `/mfa/challenge` 或 200 JSON；false → 直接登录）；user 路径放在 ulp-portal 注入（见 Phase 4），ulp-console 装配实例只走 admin 分支
- [ ] 3.2 暂存 Redis key = `ULP_MFA_PENDING:{uuid}` TTL 5 min；Authentication 用 Jackson 3 序列化（沿用 `RedisOAuth2AuthorizationService` 模式）
- [ ] 3.3 `ulp-support` 写 `MfaChallengeService`：`verifyAndCommit(uuid, code, sourceIp)` 实现读 Redis pending → 验证 TOTP / 备份码 → 校验源 IP `/24` 同段 → 成功提交 Authentication 到 `SecurityContextHolder` + Spring Session
- [ ] 3.4 `MfaChallengeService` 集成失败计数器（`ULP_MFA_FAIL:{userType}:{userId}` Redis key，TTL 15 min，达 5 锁定）；锁定期间返回 `423 Locked` + `Retry-After` header
- [ ] 3.5 `ulp-console` 写 `MfaChallengeController` 提供 `POST /api/v1/mfa/challenge` 接 `{ code }` 或 `{ backupCode }`
- [ ] 3.6 改 `ConsoleSecurityConfiguration`：注入 admin 分支的 `MfaAwareAuthenticationSuccessHandler` 替换原 success handler（ulp-console **不注册** `OrgMfaEnforcementFilter`，admin 不参与组织强制）
- [ ] 3.7 集成测试 `ConsoleMfaChallengeLoginIT`：admin 未绑 MFA 直接登录（无 setup 强拉）/ admin 自愿绑定后下次登录走 challenge / challenge 成功后正常访问 / challenge 失败 5 次锁定 4 场景

## 4. Portal form login MFA challenge gate + 组织强制

- [ ] 4.1 `ulp-portal` 写 `OrgMfaEnforcementFilter`：在 `SecurityContextPersistenceFilter` 之后、`FilterSecurityInterceptor` 之前；对已认证 user 请求（`Authentication.isAuthenticated()=true` 且 principal 是 `ulp_user`），若 `mfa_enabled=false` 且 `OrgMfaPolicyService.isUserEnforced(userId)=true`，且请求路径不在白名单（`/api/v1/mfa/bind/**`、`/mfa/setup`、`/logout`、静态资源、`/error`），返回 403 + JSON `{"error":"mfa_setup_required","reason":"org_policy"}`
- [ ] 4.2 改 `MfaAwareAuthenticationSuccessHandler` user 分支：`mfa_enabled=true` → 走 challenge；`mfa_enabled=false` 且 `isUserEnforced=true` → 直接登录（提交 Authentication）但返回 302 → `/mfa/setup` 或 200 + `{"mfa_setup_required":true,"reason":"org_policy"}`；`mfa_enabled=false` 且 `isUserEnforced=false` → 直接登录路径不变
- [ ] 4.3 改 `PortalSecurityConfiguration`：注入 user 分支的 `MfaAwareAuthenticationSuccessHandler`；注册 `OrgMfaEnforcementFilter`
- [ ] 4.4 `ulp-portal` 写 `MfaChallengeController`（复用 `MfaChallengeService`）
- [ ] 4.5 集成测试 `PortalMfaChallengeLoginIT`：自愿 user 未绑登录路径不变 / 自愿 user 绑后 challenge / 自愿 user challenge 成功访问主页 4 场景
- [ ] 4.6 集成测试 `PortalOrgMfaEnforcementIT`：被强制 user 首次登录 302 `/mfa/setup` / 访问其他路径 403 `mfa_setup_required` / 完成绑定后访问正常 / 关闭 org 强制位后无强拉 4 场景

## 5. 备份码 + 失败锁定 + ROPC 拒签

- [ ] 5.1 `MfaBackupCodeService`：`consume(userType, userId, code)` 遍历 `backup_codes_json` `matches()` → 删除消费项 → 同事务落库 → 返回剩余数量
- [ ] 5.2 `MfaChallengeService` 接 backup code 路径：剩余 ≤ 2 时响应含 `regenerate_backup_codes_warning: true`，剩余 = 0 时响应含 `regenerate_backup_codes_required: true`
- [ ] 5.3 Bind / Unbind / Challenge 端点全部接 `MfaLockoutService`：成功清零计数，失败 +1，达阈值返回 423 + `Retry-After`
- [ ] 5.4 改 `ulp-openapi` `OAuth2AuthorizationResourceOwnerPasswordAuthenticationProvider`（fork in `ulp-protocol-oidc`）：authenticate 流程取 UserDetails 后查 `mfa_enabled`，true 则 `throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_GRANT, "MFA is required for this user; password grant is not supported", null))`
- [ ] 5.5 集成测试 `MfaBackupCodeIT`：消费 9 个剩 1 触发警告 / 消费第 10 个触发强制重生成 / 同备份码重放被拒 3 场景
- [ ] 5.6 集成测试 `MfaLockoutIT`：5 次失败触发 423 / 锁定期内不消耗 pending / 成功清零 3 场景
- [ ] 5.7 集成测试 `RopcMfaRejectIT`：开 MFA 用户 ROPC 返回 400 invalid_grant + 未开 MFA 用户 ROPC 正常返 token 2 场景

## 6. 审计事件接线

- [ ] 6.1 改 `ulp-audit/cn/frank/ulp/audit/event/type/PortalEventType.java` 第 80 行 `BIND_MFA` event code `bind_maf` → `bind_mfa`
- [ ] 6.2 改 `UNBIND_MFA` event code `unbind_maf` → `unbind_mfa`（design.md D7 笔记）
- [ ] 6.3 `PortalEventType` 新加 5 个常量：`MFA_CHALLENGE_REQUIRED` / `MFA_VERIFY_SUCCESS` / `MFA_VERIFY_FAILURE` / `BACKUP_CODE_USED` / `MFA_LOCKED_OUT`
- [ ] 6.4 新加 `ADMIN_RESET_USER_MFA` + `ORG_MFA_POLICY_CHANGED` 事件类型（放在 `ConsoleEventType` 而非 `PortalEventType`，按现有命名习惯判断）
- [ ] 6.5 `MfaService` / `MfaChallengeService` / `MfaLockoutService` / `AdminMfaResetController` / `OrgMfaPolicyController` 所有成功 / 失败 / 值变更路径调 `AuditEventPublisher.publish(...)` 发对应事件
- [ ] 6.6 `MFA_VERIFY_FAILURE` details 字段含 `failure_reason` ∈ `{ invalid_otp, invalid_backup_code, challenge_expired, challenge_session_invalid }`
- [ ] 6.7 `ADMIN_RESET_USER_MFA` details 字段含 `target_user_id` / `target_user_type` / `actor_admin_id`；`ORG_MFA_POLICY_CHANGED` details 字段含 `org_id` / `org_name` / `old_value` / `new_value` / `actor_admin_id`
- [ ] 6.8 注册 Micrometer 指标：`ulp_mfa_verify_total` Counter / `ulp_mfa_lockout_total` Counter / `ulp_mfa_pending_active` Gauge（Redis SCAN, ≥30s 采样）/ `ulp_mfa_bind_total` Counter
- [ ] 6.9 `actuator` IT 校验 prometheus 端点能拉到 `ulp_mfa_*` 指标
- [ ] 6.10 集成测试 `MfaAuditEventIT`：完整 bind → challenge fail → challenge success → unbind 流程 + admin 改 org `mfa_enforced` 值变更/重复值 后查 `ulp-audit` 表，确认各事件 event code 与 details 字段符合 spec（含 `ORG_MFA_POLICY_CHANGED` 仅在值变化时发的断言）

## 7. 前端 UI（console-fe + portal-fe）

- [ ] 7.1 `console-fe` 个人设置页加 "安全 / MFA" tab（admin 自愿）：未绑显示"立即绑定"按钮 / 已绑显示"重新生成备份码"+"解绑"按钮（解绑需输当前 OTP）；admin 解绑不受组织强制约束
- [ ] 7.2 `console-fe` 组织管理页 org 节点详情新增"强制 MFA"开关组件（Switch + 文字说明"开启后该组织下用户首次登录或解绑后被强制绑定 MFA"），调 `POST /api/v1/admin/organizations/{id}/mfa-policy`
- [ ] 7.3 `console-fe` 新增 `/console/mfa/setup` 页（admin 自愿绑定入口）：调 prepare 拿 secret → 用 `qrcode` npm 渲染 QR PNG → 用户输第一个 OTP → 调 confirm → 展示 10 个备份码（含下载 .txt 按钮）
- [ ] 7.4 `console-fe` 新增 `/console/mfa/challenge` 页：admin 登录响应是 mfa_required 时跳转到此 → 输 6 位 TOTP 或切换备份码 → 调 challenge → 成功跳回原 redirectUrl
- [ ] 7.5 `portal-fe` 账户设置页加同款 "安全 / MFA" tab；被组织强制覆盖的 user 看到的"解绑"按钮 SHALL 灰禁 + 文案"所在组织已启用强制 MFA，无法解绑"
- [ ] 7.6 `portal-fe` 新增 `/mfa/setup` 页：被强制覆盖的 user 登录后被重定向至此完成绑定（流程同 console setup）
- [ ] 7.7 `portal-fe` 新增 `/mfa/challenge` 页（同 console 版本）
- [ ] 7.8 `portal-fe` 加 `mfa_setup_required` 全局拦截：API 响应 403 + `error="mfa_setup_required"` 时自动跳 `/mfa/setup`
- [ ] 7.9 两端 `pnpm install qrcode` + `pnpm i -D @types/qrcode`
- [ ] 7.10 两端 `pnpm openapi` 重新生成 API client（含新增 MFA 端点 + 组织 mfa-policy 端点）
- [ ] 7.11 两端 `pnpm build` 通过 + 浏览器手动烟测：admin 自愿绑定 → challenge → 解绑 / admin 切开 org 强制位 → 该 org user 登录被强拉 → 绑定 → 解绑被拒 全流程

## 8. 集成测试套件统筹

- [ ] 8.1 创建 `AbstractMfaIntegrationTest`（继承 `AbstractIntegrationTest`），提供 helper：`createUserWithMfa(secret)` / `computeTotp(secret, instant)` / `cleanMfaRedisKeys(userId)`
- [ ] 8.2 复核第 2-6 阶段所有 MFA IT 类继承自 `AbstractMfaIntegrationTest`，复用 helper
- [ ] 8.3 每个 IT 类 `@AfterEach` 清 Redis pending / fail counter / bind secret 三类 key（参考 `integration-testing` spec 要求）
- [ ] 8.4 `./mvnw.cmd clean verify -DskipTests=false` 全部 MFA IT 通过；记录新增 IT 数与总耗时增加（更新 OpenSpec change 的 notes 或 README integration tests 段）

## 9. 文档 + Spec promotion + 归档

- [ ] 9.1 `README.md` 加 "MFA 部署前置" 段：KEK 生成命令（`openssl rand -base64 32` / PowerShell）+ K8s Secret 写入示例 + 团队密码管理器灾难恢复备份建议
- [ ] 9.2 `CLAUDE.md` 加 "MFA 第二因子" 段：KEK 配置要求 / 强制策略 = 组织级 `mfa_enforced` 位 + admin 自愿 / 多组织 OR 不沿父链 / 解绑被拒 403 `unbind_blocked_by_org_policy` / ROPC 拒签行为 / 三个部署单元都需要 `ULP_MFA_KEK` env
- [ ] 9.3 `CLAUDE.md` "Configuration that's easy to get wrong" 段加：`MfaAwareAuthenticationSuccessHandler` 必须显式装配到 `ConsoleSecurityConfiguration` 与 `PortalSecurityConfiguration`（参照 Argon2id 那一段写法）；`OrgMfaEnforcementFilter` 仅装配到 `PortalSecurityConfiguration`（admin 不参与组织强制）
- [ ] 9.4 三个 deployable 的 `application-example.yml`（如有）补 `ulp.mfa.key-encryption-key` 注释化示例 + 生成命令链接
- [ ] 9.5 写 advisory: ROPC 客户端如果用 MFA 用户的密码 grant 会被拒（OpenAPI 集成方需迁移到 Auth Code Flow）
- [ ] 9.6 跑 `openspec validate add-mfa-totp-second-factor`（如有），无错误
- [ ] 9.7 三个部署单元本地启动烟测：console:1898 / portal:1989 / openapi:1988 各启一遍，跑端到端流程，确保启动校验生效（删 KEK 启动失败 + 正常 KEK 启动成功）
- [ ] 9.8 `git commit` + 推 feature 分支 + 开 PR；review 通过合并到 main 后跑 `/opsx:archive add-mfa-totp-second-factor`，spec promote 到 `openspec/specs/mfa/spec.md`（新）+ `security-baseline` / `observability` 合并 delta
- [ ] 9.9 更新 `C:\Users\frankzhang\.claude\plans\ulp-post-rebrand-roadmap.md`：把 #87 从待办移到已完成
