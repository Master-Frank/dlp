## ADDED Requirements

### Requirement: TOTP 第二因子算法基线

平台 SHALL 使用 **RFC 6238 TOTP** 算法（基于 RFC 4226 HOTP）作为第二因子，参数：

- 哈希算法 = `HmacSHA1`（兼容主流 Authenticator app；HmacSHA256 / HmacSHA512 部分 app 不支持）
- 时间步长（period）= **30 秒**
- OTP 位数 = **6 位**
- 时钟漂移容忍窗口 = **±1 个时间步**（验证当前窗口 + 前一窗口 + 后一窗口，共 90 秒）
- 共享密钥长度 = **160 bits（20 bytes）**，由 `SecureRandom` 生成，Base32 编码后给前端

`otpauth://` URI 格式 MUST 符合 Google Authenticator Key URI Format：
`otpauth://totp/ULP:{username}?secret={base32-secret}&issuer=ULP&algorithm=SHA1&digits=6&period=30`

#### Scenario: 算法参数固定
- **WHEN** 后端生成 TOTP secret 并构造 `otpauth://` URI
- **THEN** URI query 参数中 `algorithm=SHA1`、`digits=6`、`period=30`，`secret` 为 Base32 编码且解码后长度为 20 字节

#### Scenario: 时钟漂移容忍 ±30 秒
- **WHEN** 客户端时钟比服务端慢 30 秒，提交基于客户端时钟生成的 TOTP 码
- **THEN** 后端验证通过；同样客户端快 30 秒亦通过；超过 ±60 秒则拒绝

### Requirement: MFA 绑定流程

ULP SHALL 提供 MFA 绑定端点 `POST /api/v1/mfa/bind/prepare` 与 `POST /api/v1/mfa/bind/confirm`，仅对**已通过密码登录**的用户开放。

绑定时序：

1. **Prepare**：服务端生成新 TOTP secret，加密后**暂存到 Redis**（key = `ULP_BIND_MFA_SECRET:{userType}:{userId}`，TTL ≤ **5 分钟**），返回 `otpauth://` URI 字符串 + Base32 secret 给前端
2. **Confirm**：前端展示 QR 码或 secret 让用户扫码 / 手动输入到 Authenticator app，用户输入第一个 6 位 OTP 后提交；服务端从 Redis 取回暂存 secret 验证 OTP，验证通过则：
   - 把 secret 用 AES-GCM 加密后写入用户行 `totp_secret_cipher` 列
   - 设置 `mfa_enabled = true`
   - 生成 10 个备份码（见下文 Requirement）
   - 删除 Redis 暂存
   - 返回备份码明文给前端**一次性展示**
3. **审计**：Prepare 触发 `PREPARE_BIND_MFA` 事件，Confirm 成功触发 `BIND_MFA` 事件

未通过密码登录或 Redis 暂存已过期 MUST 返回 `401 Unauthorized`；提交的 OTP 验证失败 MUST 返回 `400 Bad Request` + `error="invalid_otp"`，且**不消费** Redis 暂存（用户可重试）。

#### Scenario: 完整绑定流程成功
- **WHEN** 已登录用户调 prepare 拿到 secret 后用 Authenticator 算出 OTP，5 分钟内调 confirm 提交
- **THEN** 用户行 `mfa_enabled` 变为 `true`，`totp_secret_cipher` 写入加密后密文，返回 10 个备份码；Redis `ULP_BIND_MFA_SECRET:*` key 被删除

#### Scenario: Prepare 暂存过期
- **WHEN** 用户调 prepare 后超过 5 分钟才调 confirm
- **THEN** 返回 401 + `error="bind_session_expired"`，DB 状态未改变

#### Scenario: Confirm OTP 错误不消费暂存
- **WHEN** 用户在 5 分钟内调 confirm 但提交了错误 OTP（含相邻 30 秒窗口外的码）
- **THEN** 返回 400 + `error="invalid_otp"`；Redis 暂存仍在，用户可立刻重试

### Requirement: MFA 解绑流程

ULP SHALL 提供 MFA 解绑端点 `POST /api/v1/mfa/unbind`，对 Portal user 与 Console admin 同名同语义开放，要求**当前会话已通过 MFA 验证**且提交一个**有效的当前 TOTP 码**（不接受备份码，避免备份码泄露场景下被解绑）。

解绑动作：
- `mfa_enabled = false`
- `totp_secret_cipher = NULL`
- `backup_codes_json = NULL`
- 审计 `UNBIND_MFA` 事件
- 清空该用户的 Redis MFA 失败计数（`ULP_MFA_FAIL:{userType}:{userId}`）

**组织强制策略拦截（仅 Portal user 适用）**：调用方为 `ulp_user` 时，端点 MUST 在进入 TOTP 验证**之前**先调 `OrgMfaPolicyService.isUserEnforced(userId)` 检查；若返回 `true`（用户至少隶属一个 `mfa_enforced=true` 的组织），MUST 直接返回 `403 Forbidden` + `error="unbind_blocked_by_org_policy"`，**不验证 TOTP**、**不消费失败计数**、**不修改任何 DB 字段**。如果用户希望真正解绑，必须先让 admin 把所有覆盖性 org 的强制位关闭或把该用户移出这些 org。

Console admin 解绑路径**不受组织强制位约束**（admin 与组织无业务关系，见 D2 边界 #4），TOTP 验证通过即可解绑。

#### Scenario: User 解绑成功（无组织强制）
- **WHEN** Portal 用户当前会话已通过 MFA challenge，且该用户隶属的所有组织 `mfa_enforced=false`，提交解绑请求含当前有效 TOTP
- **THEN** 用户行 `mfa_enabled` 变为 `false`，`totp_secret_cipher` / `backup_codes_json` 置 NULL，Redis 失败计数清零

#### Scenario: User 解绑被组织强制拦截
- **WHEN** Portal 用户隶属的组织中存在任一 `mfa_enforced=true`，调用解绑端点（即便提交了有效 TOTP）
- **THEN** 返回 403 + `error="unbind_blocked_by_org_policy"`；DB 状态未变；Redis 失败计数**未递增**（短路在 TOTP 验证之前）

#### Scenario: Admin 解绑不受组织强制约束
- **WHEN** Console admin 当前会话已通过 MFA challenge，提交解绑请求含当前有效 TOTP
- **THEN** 返回 200；admin 行 `mfa_enabled` 变为 `false`，相关字段清空；与任何 `ulp_organization` 的 `mfa_enforced` 状态无关

#### Scenario: 解绑不接受备份码
- **WHEN** 用户（admin 或 user）提交解绑请求但 `code` 字段是备份码（8 位 Base32 风格字符）而非 6 位 TOTP
- **THEN** 返回 400 + `error="invalid_otp"`，备份码**不被消费**

### Requirement: 登录 MFA Challenge

form login `AuthenticationSuccessHandler` MUST 在密码验证通过后按以下决策树分支：

**Admin（`ulp_administrator`）路径：**
- `mfa_enabled = true` → 进入 challenge 挂起流程（见下文）
- `mfa_enabled = false` → 直接登录（不查组织强制）

**User（`ulp_user`）路径：**
- `mfa_enabled = true` → 进入 challenge 挂起流程（见下文）
- `mfa_enabled = false` 且 `OrgMfaPolicyService.isUserEnforced(userId) = true` → 直接登录（提交 Authentication）但 SHALL 在响应中下发 `ulp-mfa-setup-required` 信号：form login 用户返回 302 → `/mfa/setup`；API 客户端返回 200 + JSON `{ "mfa_setup_required": true, "reason": "org_policy" }`。同时由组织强制拦截 filter（`OrgMfaEnforcementFilter`）在后续请求中持续把任何非 `/mfa/setup` / `/logout` / 静态资源的请求 403 + `error="mfa_setup_required"`（避免用户绕过强拉只调 API）
- `mfa_enabled = false` 且 `isUserEnforced = false` → 直接登录（不挂起）

**Challenge 挂起流程**（admin 与 user 共用，进入条件：已 `mfa_enabled = true`）：

1. 把 `Authentication` 对象序列化暂存到 Redis（key = `ULP_MFA_PENDING:{uuid}`，TTL ≤ **5 分钟**）
2. 下发 `ulp-mfa-pending` cookie：UUID + `HttpOnly=true` + `Secure=true` + `SameSite=Strict` + `Path=/`，过期时间与 Redis TTL 一致
3. 返回 302 重定向到 `/mfa/challenge` 前端 SPA 路由（form login 用户）或返回 200 + JSON `{ "mfa_required": true, "challenge_id": "<uuid>" }`（API 客户端，根据 `Accept` 头判定）

Challenge 端点 `POST /api/v1/mfa/challenge` 接受 `{ code: "123456" }`（TOTP）或 `{ backupCode: "ABCD1234" }`（备份码），二选一：

- 读 `ulp-mfa-pending` cookie 拿 UUID
- 从 Redis 取暂存 Authentication；取不到 = 过期/无效 → 返回 401 + `error="challenge_expired"` + 清 cookie
- 验证 TOTP（用 `constant-time` 比对，见 `security-baseline`）或备份码
- **成功**：提交 Authentication 到 `SecurityContextHolder` + Spring Session（**至此才是真正登录态**）；清 Redis 暂存 + 清 cookie；返回 200 + 重定向 URL；清失败计数；触发 `MFA_VERIFY_SUCCESS` 审计
- **失败**：失败计数 +1（见下方 Requirement），返回 401 + `error="invalid_code"`；触发 `MFA_VERIFY_FAILURE` 审计

#### Scenario: 已绑 MFA 用户密码登录后被挂起
- **WHEN** `mfa_enabled = true` 的用户（admin 或 user）提交正确密码到 form login
- **THEN** 响应是 302 → `/mfa/challenge` 或 200 + `{"mfa_required":true,...}`；`SecurityContextHolder` 仍为空；Spring Session 中无该用户 Authentication；Redis 含 `ULP_MFA_PENDING:{uuid}` key

#### Scenario: Challenge 通过后才登录态
- **WHEN** 用户从 `/mfa/challenge` 提交正确 TOTP
- **THEN** 后续请求 `SecurityContextHolder.getContext().getAuthentication().isAuthenticated()` 为 `true`；Spring Session 含该用户 Authentication；`ulp-mfa-pending` cookie 被清除（Max-Age=0）

#### Scenario: Challenge 过期
- **WHEN** 用户密码登录后超过 5 分钟才提交 challenge
- **THEN** 返回 401 + `error="challenge_expired"`；`ulp-mfa-pending` cookie 被清除

#### Scenario: 未强制 user 未绑 MFA 登录路径不变
- **WHEN** `ulp_user` 行 `mfa_enabled = false`，且 `OrgMfaPolicyService.isUserEnforced(userId) = false`
- **THEN** 直接登录成功，无 302 → `/mfa/challenge`、无 302 → `/mfa/setup`；Spring Session 即刻含 Authentication

#### Scenario: 自愿 admin 未绑 MFA 登录路径不变
- **WHEN** `ulp_administrator` 行 `mfa_enabled = false`（admin 不参与组织强制）
- **THEN** 直接登录成功，无 302 → `/mfa/challenge`、无 302 → `/mfa/setup`；admin 可在 console 个人设置页自愿开启 MFA

#### Scenario: 被组织强制的 user 首次登录被强拉绑定
- **WHEN** `ulp_user` 行 `mfa_enabled = false`，且至少隶属一个 `mfa_enforced = true` 的 `ulp_organization`，提交正确密码到 form login
- **THEN** 响应是 302 → `/mfa/setup`（form login）或 200 + `{"mfa_setup_required":true,"reason":"org_policy"}`（API）；Spring Session **已含** Authentication（已登录态）；后续访问任何非 `/mfa/setup` / `/logout` / 静态资源路径均返回 403 + `error="mfa_setup_required"`，直到完成绑定

### Requirement: 备份码生成与一次性消费

绑定 MFA 成功时 ULP SHALL 一次性生成 **10 个 8 位** 备份码：

- 字符集 = `[2-9A-HJ-NP-Z]`（去掉 `0/O/1/I/l` 易混字符）
- 生成器 = `java.security.SecureRandom`
- 每个备份码 SHALL 用 **Argon2id**（复用 `PasswordEncoderFactories.createDelegatingPasswordEncoder()`，前缀 `{argon2}`）哈希后存入 `backup_codes_json` 列（JSON 数组，每元素是带前缀的哈希字符串）
- 备份码明文 MUST 仅在生成那一刻返回给前端**一次**（页面展示 + 下载 .txt 按钮），后续无任何接口可重新获取明文

备份码消费规则：

- Challenge 端点接受 `{ backupCode: "..." }`，服务端**遍历** `backup_codes_json` 数组逐个 `PasswordEncoder.matches()` 比对
- 匹配成功：**同事务**从数组删除该项 + 落库 + 标记当前会话 `backup_code_used = true`（仅用于审计 `BACKUP_CODE_USED`）+ 走 challenge 成功流程
- 剩余备份码 ≤ 2 时下次登录页 SHALL 显示"建议重新生成备份码"提示
- 剩余备份码 = 0 时下次登录 challenge 通过后 SHALL 强跳到"重新生成备份码"页面（不阻塞登录态，但阻塞访问其他页）

备份码 MUST NOT 用于绑定 / 解绑流程（仅用于 challenge）。

#### Scenario: 生成 10 个备份码并哈希入库
- **WHEN** 用户完成 MFA 绑定 confirm
- **THEN** 响应 JSON 含 `backupCodes`（10 个 8 位字符串），DB `backup_codes_json` 列含 10 元素 JSON 数组，每元素以 `{argon2}` 开头

#### Scenario: 备份码使用后从 DB 删除
- **WHEN** 用户在 challenge 端提交备份码 `XXXXXXXX` 验证通过
- **THEN** 重新查询 DB，`backup_codes_json` 数组从 10 元素变为 9 元素；同一备份码再次提交返回 401 + `error="invalid_code"`

#### Scenario: 备份码用完触发重新生成
- **WHEN** 用户消费第 10 个备份码（剩余 = 0）
- **THEN** challenge 成功，且响应含 `regenerate_backup_codes_required: true`；后端任何受保护页面在用户重新生成前 SHALL 返回 403 + `error="backup_codes_exhausted"`

### Requirement: MFA 强制策略（组织级）

ULP SHALL 实施"组织级强制位"模型作为唯一的 MFA 强制策略：

- **Admin（`ulp_administrator` 表所有账号）**：默认自愿启用，admin 在 console 个人设置页"安全 / MFA" tab 自主开启 / 解绑。admin **不参与**组织强制（admin 与 `ulp_organization` 无业务关系）
- **User（`ulp_user` 表所有账号）**：默认自愿启用。若该 user 通过 `ulp_organization_member` 直接归属的任一 `ulp_organization` 行 `mfa_enforced = true`（OR 语义），则该 user 被组织强制覆盖：
  - 未绑定时（`mfa_enabled = false`）→ 下次 form login 成功后被强拉到 `/mfa/setup` 完成绑定（见"登录 MFA Challenge" Requirement）
  - 已绑定时（`mfa_enabled = true`）→ 行为与自愿用户一致（登录走 challenge）
  - 调用解绑端点 → 直接 403 `error="unbind_blocked_by_org_policy"`（见"MFA 解绑流程" Requirement）

**叠加规则**：用户多组织归属时，**任一**直接归属 org 的 `mfa_enforced = true` 即触发强制。**不沿父链继承**——只看 user → org 的直接归属关系，不沿 `ulp_organization.parentId` 向上 / 向下传播。

**过渡期**：组织强制位切开后**立即生效**（grace = 0），该 org 下所有未绑 MFA 的 user 在**下一次登录**时即被强拉。`application.yml` MUST NOT 暴露 grace period 配置开关。

**全局开关红线**：MUST NOT 在 `application.yml` 暴露任何"全局关闭组织强制"或"切换为旧 admin-only 强制模型"的开关——`mfa_enforced` 列只能通过下方 admin 端点修改，配置文件不能旁路。

#### Scenario: 被强制的 user 首次登录被强拉绑定
- **WHEN** Portal 用户 `mfa_enabled = false`，且通过 `ulp_organization_member` 直接归属的某个 `ulp_organization` 行 `mfa_enforced = true`，提交正确密码到 form login
- **THEN** 响应是 302 → `/mfa/setup` 或 200 + `{"mfa_setup_required":true,"reason":"org_policy"}`；尝试访问其他 Portal 页面（如 `/portal/home`）返回 403 + `error="mfa_setup_required"`，直到绑定完成

#### Scenario: User 多组织归属任一开启即强制
- **WHEN** Portal 用户 P 归属 org A、org B，A.`mfa_enforced=false` 且 B.`mfa_enforced=true`
- **THEN** `OrgMfaPolicyService.isUserEnforced(P)` 返回 `true`；P 走强制路径

#### Scenario: 不沿父链继承
- **WHEN** Portal 用户 Q 仅直接归属子组织 C（`mfa_enforced=false`），其父组织 D `mfa_enforced=true`，Q 不直接归属 D
- **THEN** `OrgMfaPolicyService.isUserEnforced(Q)` 返回 `false`；Q 不被强制

#### Scenario: Admin 不参与组织强制
- **WHEN** Console admin 账号 `mfa_enabled = false`，存在 `mfa_enforced=true` 的 org
- **THEN** admin 登录路径不变（直接登录成功），无 302 → `/mfa/setup`；admin 与任何 org 的 `mfa_enforced` 状态无关

#### Scenario: 未被任何 org 覆盖的 user 自愿
- **WHEN** Portal 用户 R 隶属的所有 org `mfa_enforced` 均为 `false`，`mfa_enabled = false`
- **THEN** 直接登录成功，全 Portal 功能可用，无任何 MFA 提示阻塞

#### Scenario: 配置开关不存在
- **WHEN** 评审三个部署单元的 `application.yml` 与 `@ConfigurationProperties` 类
- **THEN** 不存在 `ulp.mfa.admin-enforced=*` / `ulp.mfa.org-enforcement-enabled=*` / `ulp.mfa.grace-period-*` 等允许旁路或软推广组织强制的键

### Requirement: 组织级 MFA 强制策略管理

ULP SHALL 提供管理端点 `POST /api/v1/admin/organizations/{id}/mfa-policy`，接受请求体 `{ mfaEnforced: true | false }`，要求调用方持有 `ADMIN` 角色。

行为：
- 校验 `id` 对应的 `ulp_organization` 存在且未软删除（否则返回 404）
- 写 `mfa_enforced` 列为请求值；若值与当前值一致则视为 no-op（不写库，不发审计），但仍返回 200
- 实际变更（值变化）SHALL 触发审计事件 `ORG_MFA_POLICY_CHANGED`（含 `org_id`、`org_name`、`old_value`、`new_value`、`actor_admin_id`，见 `observability` spec）
- 立即生效：该 org 下所有 user 在下一次 `OrgMfaPolicyService.isUserEnforced` 查询时反映新值；MUST NOT 引入缓存层导致变更延迟（v1 直接查表，若 v2 引入缓存须在 spec change 中显式声明 TTL）

Console 的"组织管理"页面 SHALL 在 org 节点详情中提供"强制 MFA"开关组件，调用该端点；该开关 MUST 仅对 `ADMIN` 角色可见与可操作。

#### Scenario: Admin 开启 org 强制位
- **WHEN** ADMIN 调用 `POST /api/v1/admin/organizations/42/mfa-policy` body `{"mfaEnforced":true}`，org 42 当前 `mfa_enforced=false`
- **THEN** 返回 200；DB 中 `ulp_organization` id=42 行 `mfa_enforced=true`；审计表新增一行 `ORG_MFA_POLICY_CHANGED`，details 含 `org_id=42`、`old_value=false`、`new_value=true`

#### Scenario: 重复值视为 no-op 但仍返 200
- **WHEN** ADMIN 调用相同端点 body `{"mfaEnforced":true}`，org 42 当前已是 `true`
- **THEN** 返回 200；DB 未写入；审计表**未新增** `ORG_MFA_POLICY_CHANGED` 行

#### Scenario: 非 ADMIN 无权调用
- **WHEN** 普通用户（非 ADMIN）发起 `POST /api/v1/admin/organizations/{id}/mfa-policy`
- **THEN** 返回 403

#### Scenario: org 不存在返回 404
- **WHEN** ADMIN 调用端点但 `id` 在 `ulp_organization` 表中不存在或已软删除
- **THEN** 返回 404；审计表无新增；DB 无写入

#### Scenario: 关闭强制位后用户立即可解绑
- **WHEN** org 42 原 `mfa_enforced=true` 时 user U（仅归属 org 42）解绑被拒；ADMIN 调端点改为 `false` 后 U 立即重试解绑（携带有效 TOTP）
- **THEN** 解绑成功，DB 中 U 行 `mfa_enabled=false`、`totp_secret_cipher=NULL`、`backup_codes_json=NULL`

### Requirement: MFA 失败锁定

ULP SHALL 对 MFA challenge 失败实施独立计数与锁定：

- 计数器 key = `ULP_MFA_FAIL:{userType}:{userId}`（`userType` ∈ `{admin, user}`），存 Redis，TTL = **15 分钟**（每次失败 reset TTL，形成滑动窗口）
- 阈值 = 连续 **5 次** 失败 → 锁定 **15 分钟**
- 锁定期间 challenge 端点 MUST 返回 `423 Locked` + `Retry-After: <seconds>` header + `error="mfa_locked"`；**MUST NOT** 消耗 `ULP_MFA_PENDING:{uuid}` 暂存（避免攻击者用废合法 challenge）
- 成功验证 MUST 立即清零计数器
- 此计数器 MUST 与密码登录失败计数器**独立**（不允许攻击者通过 MFA 失败间接锁死合法用户密码登录通道）
- 锁定触发 SHALL 审计 `MFA_LOCKED_OUT` 事件

#### Scenario: 5 次失败触发锁定
- **WHEN** 用户在 15 分钟内连续 5 次提交错误 TOTP
- **THEN** 第 5 次失败后 Redis `ULP_MFA_FAIL:{userType}:{userId}` = 5；第 6 次请求 challenge 返回 423 + `Retry-After` header（值 ≤ 900）

#### Scenario: 锁定期内不消耗 pending
- **WHEN** 用户处于锁定状态，仍持有有效 `ulp-mfa-pending` cookie，发起 challenge 请求
- **THEN** 返回 423，且 Redis `ULP_MFA_PENDING:{uuid}` key 仍存在（未删除）

#### Scenario: 成功验证清零计数器
- **WHEN** 用户失败 3 次后提交正确 TOTP
- **THEN** challenge 成功，Redis `ULP_MFA_FAIL:{userType}:{userId}` 被 DEL

#### Scenario: MFA 锁定不影响密码登录
- **WHEN** 用户 MFA 被锁定 15 分钟期间，重新发起 form login 提交正确密码
- **THEN** 密码验证仍正常通过（计数器独立），新生成 `ULP_MFA_PENDING:{uuid}`；后续 challenge 仍返回 423 直到 MFA 锁定窗口过期

### Requirement: ROPC + MFA 用户拒签

`openapi` 的 OIDC ROPC（`grant_type=password`）端点 MUST 在认证流程中检查目标 user 的 `mfa_enabled` 字段：

- `mfa_enabled = true` → 立即抛 `OAuth2AuthenticationException` 含 `OAuth2Error(OAuth2ErrorCodes.INVALID_GRANT, "MFA is required for this user; password grant is not supported", ...)`，HTTP 400 + 标准 OIDC 错误响应
- `mfa_enabled = false` → 走原密码验证 + Argon2id auto-rehash 路径（不变）

该检查 MUST 在 `OAuth2AuthorizationResourceOwnerPasswordAuthenticationProvider` fork 内实施。

#### Scenario: ROPC 拒签已开 MFA 用户
- **WHEN** 对 openapi `/oauth2/token` POST 含 `grant_type=password&username=alice&password=...`，alice 的 `mfa_enabled = true`
- **THEN** 响应 400 + JSON `{"error":"invalid_grant","error_description":"MFA is required for this user; password grant is not supported"}`

#### Scenario: ROPC 对未开 MFA 用户行为不变
- **WHEN** 同上但 alice `mfa_enabled = false`
- **THEN** 正常返回 access_token + refresh_token（Argon2id auto-rehash 仍生效）

### Requirement: 管理员重置用户 MFA

ULP SHALL 提供管理端点 `POST /api/v1/admin/users/{id}/reset-mfa` 与 `POST /api/v1/admin/administrators/{id}/reset-mfa`，要求调用方持有 `ADMIN` 角色。

重置动作：
- `mfa_enabled = false`
- `totp_secret_cipher = NULL`
- `backup_codes_json = NULL`
- 清空 Redis 失败计数 `ULP_MFA_FAIL:{userType}:{userId}` 与暂存 `ULP_BIND_MFA_SECRET:{userType}:{userId}`、`ULP_MFA_PENDING:{uuid}`（若存在）
- 审计 `ADMIN_RESET_USER_MFA` 事件（含目标 userId / userType / 操作 adminId）

被重置的用户下次登录：
- Admin → 走未绑定流程（自愿，不强拉）
- User → 若隶属的所有 org `mfa_enforced=false`，走未绑定流程（自愿）；若任一 org `mfa_enforced=true`，被强拉到 `/mfa/setup` 重新绑定

#### Scenario: 管理员重置后用户回到未绑定态
- **WHEN** Admin 调用 `POST /api/v1/admin/users/{id}/reset-mfa`，目标 user `mfa_enabled` 原为 `true`
- **THEN** 响应 200；DB 该 user 行 `mfa_enabled = false`、`totp_secret_cipher` / `backup_codes_json` 均为 NULL；审计表新增一行 `ADMIN_RESET_USER_MFA` 事件含 `targetUserId` 与 `actorAdminId`

#### Scenario: 非 Admin 无权调用
- **WHEN** 普通用户（非 ADMIN）发起 `POST /api/v1/admin/users/{id}/reset-mfa`
- **THEN** 返回 403
