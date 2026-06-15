## Why

ULP 当前只有"用户名 + 密码"单因子认证；Argon2id 已经把 at-rest 哈希做到 OWASP 2024 推荐强度，但只要密码被钓鱼/泄露/撞库命中，攻击者就能直接登录 Admin Console 拿到全平台租户级权限——这是 SOC2 / ISO 27001 / 阿里云合规审计第一个会问的缺口。

行业基线：管理员账户必须开启第二因子，普通用户至少要能开启。当前仓库里只有零散脚手架——`PortalEventType.{BIND_MFA, UNBIND_MFA, PREPARE_BIND_MFA}` 三个事件类型 + `UlpConstants.ULP_BIND_MFA_SECRET[_EXPIRE]` 两个 Redis key 名——没有任何 entity、endpoint、service、filter，登录链路里也没有 MFA gate。本期把这块从 0 拉到 v1。

## What Changes

- **新增 TOTP（RFC 6238）第二因子能力**：用户/管理员可以通过扫码绑定 Authenticator app（Google Authenticator / 1Password / Authy 等），登录时密码通过后跳 MFA challenge 页输 6 位动态码
- **强制策略 = 组织级强制位**：admin / user 默认都自愿启用；admin 在 console 给某个 `ulp_organization` 节点打 `mfa_enforced=true` 强制位 → 该 org 下未绑 MFA 的 user 首次登录被强拉到 `/mfa/setup`、已绑 user 调解绑直接 403；admin 不参与组织强制（永远自愿）；多组织归属取 OR 语义、不沿父链继承；过渡期 0（立即生效）
- **备份码体系**：绑定成功时一次性下发 10 个 8 位备份码，Argon2id 哈希存库，每个一次性使用；用完后强制重新生成
- **TOTP 密钥静态加密**：DB 不存明文 secret，存 AES-GCM 密文，KEK (Key Encryption Key) 由部署方通过 `ulp.mfa.key-encryption-key` 配置/env 提供，缺失则启动拒绝
- **ROPC `mfa_required` 拒签**：openapi `/oauth2/token grant_type=password` 对开了 MFA 的用户返回 `invalid_grant` + `error_description=mfa_required`，因 ROPC 不支持二因子交互
- **失败锁定**：单账户连续 5 次 MFA 验证失败锁定 15 min（独立于密码锁定，避免攻击者通过 MFA 失败间接锁死合法用户密码登录）
- **审计事件**：复用已有 `BIND_MFA / UNBIND_MFA / PREPARE_BIND_MFA`（顺手修 `BIND_MFA` event code 的 `bind_maf` typo → `bind_mfa`），新加 7 个 `MFA_CHALLENGE_REQUIRED / MFA_VERIFY_SUCCESS / MFA_VERIFY_FAILURE / BACKUP_CODE_USED / MFA_LOCKED_OUT / ADMIN_RESET_USER_MFA / ORG_MFA_POLICY_CHANGED`
- **DB schema**：`ulp_user` / `ulp_administrator` 各加 3 列 (`mfa_enabled BOOLEAN`、`totp_secret_cipher VARCHAR(255)`、`backup_codes_json TEXT`)，`ulp_organization` 加 1 列 (`mfa_enforced BOOLEAN`)。失败计数走 Redis，不建表
- **前端 UI**：console-fe 个人设置页加 "MFA 安全" tab + 组织管理页 org 节点详情加"强制 MFA"开关；portal-fe 账户设置页加同款 MFA tab；两端都加 `/mfa/challenge` 与 `/mfa/setup` 页

**BREAKING**: 配置层面引入硬性新依赖项 `ulp.mfa.key-encryption-key`——升级到此版本的部署必须先生成 32 字节随机 KEK 写入 env/配置（生成命令将在文档给出），否则启动失败。

## Capabilities

### New Capabilities
- `mfa`: TOTP 第二因子的完整业务能力——绑定流程（PREPARE_BIND → showQR → VERIFY → BIND）、解绑流程、登录二因子 gate、备份码生成/校验/消费、失败锁定

### Modified Capabilities
- `security-baseline`: 新增"MFA 第二因子"要求块——TOTP 算法标准 (RFC 6238)、密钥 at-rest 加密 (AES-GCM + KEK)、challenge 一次性 + 60s 过期、verify 必须 constant-time、备份码必须 Argon2id 哈希且一次性
- `observability`: 复用现有 3 个 MFA 事件 + 新加 7 个 MFA 审计事件（`MFA_CHALLENGE_REQUIRED` / `MFA_VERIFY_SUCCESS` / `MFA_VERIFY_FAILURE` / `BACKUP_CODE_USED` / `MFA_LOCKED_OUT` / `ADMIN_RESET_USER_MFA` / `ORG_MFA_POLICY_CHANGED`）；MFA 失败锁定指标暴露给 Prometheus

## Impact

**Code 改动范围**：
- 新模块：`ulp-support/.../security/mfa/` 共享 TOTP 算法 + KEK 加解密工具 + `OrgMfaPolicyService`
- `ulp-core`: `UserEntity` / `AdministratorEntity` 加 3 列；`OrganizationEntity` 加 1 列（`mfaEnforced`）；不建新表
- `ulp-common`: Liquibase changeset 新增（ulp-changelog-master.xml include 新文件）
- `ulp-console` + `ulp-portal`:
  - bind/unbind/challenge endpoint（3 个 controller）+ service 实现
  - `MfaAwareAuthenticationSuccessHandler` 包装替换原 success handler；新增 `OrgMfaEnforcementFilter` 拦截被组织强制但未绑的 user 请求（仅 ulp-portal 装配，ulp-console 不需要因为 admin 不参与组织强制）
  - security config 改造（沿用 Argon2id 那一波的 `DaoAuthenticationProvider` 显式装配模式）
  - console-fe 加 MFA 个人设置 tab + 组织管理"强制 MFA"开关 + `/mfa/setup` + `/mfa/challenge` 页
  - portal-fe 加 MFA 个人设置 tab + `/mfa/setup` + `/mfa/challenge` 页
- `ulp-console`: 新增 `OrgMfaPolicyController` 提供 `POST /api/v1/admin/organizations/{id}/mfa-policy`（ADMIN 角色）
- `ulp-openapi`: ROPC provider 加 `mfa_required` 拒签分支
- `ulp-audit`: `PortalEventType` 修 typo + 加 7 个新事件类型（含 `ORG_MFA_POLICY_CHANGED`）
- 三个 deployable 的 `application.yml` 加 `ulp.mfa.*` 配置 placeholder + 文档

**外部依赖**：
- 新加 `dev.samstevens.totp:totp:1.7.1`（Apache 2.0，1.6k star，含 QR URI 生成）—— 决策细节见 design.md

**部署**：
- 必须先生成 KEK 写入 env (`ULP_MFA_KEK=<base64-32-bytes>`)，否则启动拒绝
- DB 迁移会加 3 列到 user/administrator + 新建 1 张表，Liquibase 自动跑

**集成测试**：
- 新加 `MfaBindFlowIT` / `MfaChallengeLoginIT` / `BackupCodeIT` 三个 IT（沿用 `AbstractIntegrationTest`）

**文档**：
- CLAUDE.md 加 "MFA 第二因子" 段，写明 KEK 配置要求 + 强制策略 + ROPC 拒签行为
- README 加部署前置条件（生成 KEK 命令）
