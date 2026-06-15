## ADDED Requirements

### Requirement: MFA 审计事件

ULP SHALL 在 `PortalEventType` / `EventType` 注册以下 MFA 相关审计事件，绑定 / 解绑 / 登录 challenge / 锁定全链路 MUST 在对应业务路径成功或失败处发事件：

复用现有事件类型（修复 typo）：

| 事件常量 | 事件 code | 触发点 |
|---|---|---|
| `PREPARE_BIND_MFA` | `ulp:event:account:prepare_bind_mfa` | `POST /api/v1/mfa/bind/prepare` 成功生成 secret |
| `BIND_MFA` | `ulp:event:account:bind_mfa`（**typo 修复**：原 `bind_maf`） | `POST /api/v1/mfa/bind/confirm` 验证通过并落库 |
| `UNBIND_MFA` | `ulp:event:account:unbind_mfa`（**typo 修复**：原 `unbind_maf`） | `POST /api/v1/mfa/unbind` 成功 |

新增事件类型：

| 事件常量 | 事件 code | 触发点 |
|---|---|---|
| `MFA_CHALLENGE_REQUIRED` | `ulp:event:account:mfa_challenge_required` | 已绑 MFA 用户密码登录通过 → 触发 challenge 挂起 |
| `MFA_VERIFY_SUCCESS` | `ulp:event:account:mfa_verify_success` | Challenge 端点验证 TOTP / 备份码成功 |
| `MFA_VERIFY_FAILURE` | `ulp:event:account:mfa_verify_failure` | Challenge 端点验证失败（含 OTP 错误、备份码错误、challenge_expired） |
| `BACKUP_CODE_USED` | `ulp:event:account:backup_code_used` | Challenge 端点用备份码验证成功（与 `MFA_VERIFY_SUCCESS` 同时发） |
| `MFA_LOCKED_OUT` | `ulp:event:account:mfa_locked_out` | 失败计数器达阈值（5 次）触发锁定 |
| `ADMIN_RESET_USER_MFA` | `ulp:event:admin:reset_user_mfa` | Admin 调用 `POST /api/v1/admin/users/{id}/reset-mfa` 或 `/administrators/{id}/reset-mfa` 成功 |
| `ORG_MFA_POLICY_CHANGED` | `ulp:event:admin:org_mfa_policy_changed` | Admin 调用 `POST /api/v1/admin/organizations/{id}/mfa-policy` 实际改变 `mfa_enforced` 值（重复值 no-op 不发） |

所有 MFA 事件 SHALL 写入 `ulp-audit` 现有审计表，结构兼容现有字段（actorId / actorType / targetId / eventCode / occurredAt / sourceIp / userAgent）。

`MFA_VERIFY_FAILURE` 事件 details 字段 MUST 含 `failure_reason` ∈ `{ "invalid_otp", "invalid_backup_code", "challenge_expired", "challenge_session_invalid" }`，便于运维分桶统计攻击模式。

`ADMIN_RESET_USER_MFA` 事件 details 字段 MUST 含 `target_user_id`、`target_user_type` ∈ `{ "admin", "user" }`、`actor_admin_id`。

#### Scenario: BIND_MFA event code 已修复
- **WHEN** 评审 `cn.frank.ulp.audit.event.type.PortalEventType` 源码
- **THEN** `BIND_MFA` 常量的 event code 字符串为 `ulp:event:account:bind_mfa`（不含 `maf`），`UNBIND_MFA` 同理

#### Scenario: 10 个 MFA 事件类型全部注册
- **WHEN** grep `cn.frank.ulp.audit.event.type` 包下所有 `Type` 常量声明
- **THEN** 同时存在 `PREPARE_BIND_MFA`、`BIND_MFA`、`UNBIND_MFA`、`MFA_CHALLENGE_REQUIRED`、`MFA_VERIFY_SUCCESS`、`MFA_VERIFY_FAILURE`、`BACKUP_CODE_USED`、`MFA_LOCKED_OUT`、`ADMIN_RESET_USER_MFA`、`ORG_MFA_POLICY_CHANGED` 10 个常量（含修复后的 3 个 + 7 个新增）

#### Scenario: 组织强制位变更审计
- **WHEN** ADMIN 调用 `POST /api/v1/admin/organizations/42/mfa-policy` 把 `mfa_enforced` 从 `false` 改为 `true`
- **THEN** 审计表新增一行 event code = `ulp:event:admin:org_mfa_policy_changed`，`details` JSON 字段含 `"org_id":42`、`"old_value":false`、`"new_value":true`、`"actor_admin_id":<调用者 id>`

#### Scenario: 组织强制位重复值不发审计
- **WHEN** ADMIN 调用相同端点 body `{"mfaEnforced":true}` 但 org 42 当前已是 `true`
- **THEN** 端点返回 200，但审计表**未新增** `ORG_MFA_POLICY_CHANGED` 行

#### Scenario: Challenge 失败审计含原因
- **WHEN** 用户在 challenge 端提交错误 OTP
- **THEN** 审计表新增一行 event code = `ulp:event:account:mfa_verify_failure`，`details` JSON 字段含 `"failure_reason":"invalid_otp"`

#### Scenario: 备份码使用同时发两个事件
- **WHEN** 用户在 challenge 端提交正确备份码
- **THEN** 审计表新增**两行**：一行 `mfa_verify_success`、一行 `backup_code_used`，两行 `occurredAt` 差 ≤ 100ms

#### Scenario: 锁定事件含目标账号
- **WHEN** 用户失败 5 次触发锁定
- **THEN** 审计表新增一行 event code = `ulp:event:account:mfa_locked_out`，`actorId` 即被锁用户 id，`details` 含 `lock_duration_seconds: 900`

### Requirement: MFA 失败锁定指标

`/actuator/prometheus` 端点 SHALL 暴露下列 Micrometer 指标，便于监控 MFA 攻击模式：

| 指标名 | 类型 | 标签 |
|---|---|---|
| `ulp_mfa_verify_total` | Counter | `result` ∈ `{success, failure}`，`mode` ∈ `{totp, backup_code}` |
| `ulp_mfa_lockout_total` | Counter | `user_type` ∈ `{admin, user}` |
| `ulp_mfa_pending_active` | Gauge | （无标签，反映当前 Redis 中 `ULP_MFA_PENDING:*` key 数量） |
| `ulp_mfa_bind_total` | Counter | `result` ∈ `{success, failure}` |

`ulp_mfa_pending_active` Gauge MUST 通过 Redis `SCAN` 实现而非 `KEYS`（避免阻塞 Redis），采样间隔 ≥ 30 秒。

监控告警建议（实施侧不强制，但 spec 留示范阈值给运维）：

- `rate(ulp_mfa_verify_total{result="failure"}[5m]) > 10` → 怀疑暴力破解
- `rate(ulp_mfa_lockout_total[15m]) > 3` → 可能是合法用户被锁，或大规模攻击

#### Scenario: prometheus 端点暴露 MFA 指标
- **WHEN** 触发一次 MFA 绑定成功 + 一次 challenge 失败后，请求 `GET /actuator/prometheus`
- **THEN** 响应 body 同时包含 `ulp_mfa_bind_total{result="success"} 1` 和 `ulp_mfa_verify_total{result="failure",mode="totp"} 1`（或更高，具体值取决于历史）

#### Scenario: pending Gauge 反映当前挂起数
- **WHEN** 同时有 3 个用户密码登录通过但未完成 challenge（Redis 含 3 个 `ULP_MFA_PENDING:*` key），等待 ≥ 30 秒后查 prometheus
- **THEN** `ulp_mfa_pending_active 3.0`（允许 ±1 误差，因为 SCAN 采样有可能正好遇到 key 过期）
