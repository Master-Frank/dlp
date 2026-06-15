## Context

ULP 仓库当前在认证安全上有两个落地能力：
- Argon2id 密码哈希 + 自动 rehash（`security-baseline` spec）—— 在 `2026-06-13` 落地，覆盖 `ulp_administrator` 与 `ulp_user`
- 五个 OAuth2 grant type（auth code / refresh / client credentials / ROPC / device code）+ form login（Console + Portal）

MFA 是这条 baseline 的下一步。仓库里有三个零散信号说明"MFA 是早期规划但从未实现"：
1. `ulp-support/.../UlpConstants.java` 有 `ULP_BIND_MFA_SECRET` + `ULP_BIND_MFA_SECRET_EXPIRE` 两个 Redis key 常量
2. `ulp-audit/.../PortalEventType.java` 声明了 `BIND_MFA / UNBIND_MFA / PREPARE_BIND_MFA` 三个事件类型（且 `BIND_MFA` 的 event code 字符串有 typo `bind_maf`）
3. 没有 entity 字段、controller、service、filter、UI——纯空壳

业务约束：
- v1 单租户（task #38 已决策），不需要按租户配置不同 MFA 策略
- 三个 deployable 服务（console:1898 / portal:1989 / openapi:1988），MFA 主要影响前两个；openapi 只承担 ROPC 拒签
- Java 21 + Spring Boot 4.0 + Spring Security 7 + Spring Session 4 (Redis) + Hibernate 7 + Jackson 3
- 前端是 UmiJS Max + Ant Design Pro

## Goals / Non-Goals

**Goals:**
- TOTP（RFC 6238）作为二因子，兼容主流 Authenticator app（Google Authenticator / 1Password / Authy / Microsoft Authenticator）
- 一套 backend 共享逻辑（in `ulp-support`），Console 和 Portal 复用
- 绑定 / 解绑 / 登录 challenge / 备份码 / 失败锁定 / 审计 全链路打通
- TOTP 密钥静态加密（AES-GCM + 部署方提供 KEK），DB 落库永远不出现明文
- 强制策略：Admin / User 默认都自愿；admin 在 console 可以给 `ulp_organization` 单独打"强制"位，被覆盖的 user 首次登录 / 解绑后会被强制拉去绑定（按组织而非账户类型分层）
- 全部新代码有集成测试覆盖（沿用 `AbstractIntegrationTest` + Testcontainers）

**Non-Goals:**
- WebAuthn / FIDO2 / Passkey（另起 proposal）
- DingTalk/Feishu push as second factor（已有 SMS/Mail OTP filter，不属本期）
- "记住此设备 30 天"（v2）
- OIDC ID Token 暴露 `acr` / `amr` claim 让 RP 感知 MFA 状态（v2）
- 多租户 MFA 策略配置（v2，先单租户）
- SMS / Mail OTP 作为 second factor（这两已存在的 filter 是单因子 OTP 登录，不和 TOTP 共用 challenge UI）
- 管理员后台代用户重置 MFA（v1 走"管理员重置用户密码同时清 MFA 字段"间接实现，不单做 UI）

## Decisions

### D1: TOTP 库选 `dev.samstevens.totp:totp:1.7.1`

- 选 A：`dev.samstevens.totp:totp:1.7.1`（Apache 2.0，1.6k★，含 RFC 6238 算法 + QR URI 生成 + zxing 二维码 PNG 渲染 + secret generator + recovery code generator）
- 备选 B：`org.aerogear:aerogear-otp-java`（2018 后无更新，弃）
- 备选 C：JDK `Mac` + 自写 ~50 行（无第三方依赖，但要自己写 QR PNG、密钥编码、Hotp 计数器漂移补偿等附件，工作量比预想大）

**Rationale**: A 是事实标准，含 QR + secret + recovery code 三个组件，省一周自写时间；许可证 Apache 2.0 与项目兼容；最新版本 1.7.1 (2022) 虽然不算热，但 RFC 6238 算法本身是稳定的，没有 CVE 历史。代价：多一个传递依赖（zxing 用于 QR PNG）—— 但 zxing 也是 Apache 2.0 + Google 维护，可接受。

不选 C 的原因：自写虽然能保证 zero-dep，但 QR PNG 渲染要么再引 zxing 要么前端拿 base32 secret 自己渲染。考虑到前端有 `qrcode` npm 包可直接用，**也可以做 fallback**：后端只返回 `otpauth://totp/...` URI，前端用 `qrcode` 渲染 PNG，省掉后端 zxing 依赖。

**最终方案**：TOTP 算法 + secret 生成走 `dev.samstevens.totp`；QR 二维码渲染让前端负责（用 `qrcode` npm），后端只返回 URI 字符串。这样后端只依赖 `dev.samstevens.totp` 核心 jar，可以 `exclude` 掉 zxing。配置：

```xml
<dependency>
  <groupId>dev.samstevens.totp</groupId>
  <artifactId>totp</artifactId>
  <version>1.7.1</version>
  <exclusions>
    <exclusion>
      <groupId>com.google.zxing</groupId>
      <artifactId>core</artifactId>
    </exclusion>
    <exclusion>
      <groupId>com.google.zxing</groupId>
      <artifactId>javase</artifactId>
    </exclusion>
  </exclusions>
</dependency>
```

### D2: 强制策略 = 组织级强制位（admin/user 默认自愿）

- 选 A：admin/user 默认都自愿；admin 在 console 给某个 `ulp_organization` 节点打 `mfa_enforced=true` 强制位；任一隶属组织开启强制 → 该 user 首次登录 / 解绑后被强拉到绑定流程；admin 账户体系本身不参与组织强制（admin 与组织无业务关系，只走自愿）
- 备选 B：原方案 = admin 强制 / user 自愿（按账户类型分层）
- 备选 C：全部可选
- 备选 D：全部强制

**Rationale**: 选 A 把"是否强制"从静态的账户类型变成**业务侧可配置的组织属性**。理由：
- 现实中"是否需要 MFA"由业务部门定（HR/财务/外包/学生账号合规要求不同），不该让 admin 角色绑死强制策略
- 现有 `ulp_organization` 已经是树结构 + `ulp_organization_member` 已支持 user 多组织归属，加一个 boolean 列即可，零额外建表
- admin 全局只有少数几个账号，强不强 MFA 的差异本来就小；让 admin 也自愿，自己开自己的，反而避免"首次部署 admin 没装 Authenticator app 进不去"的 bootstrap 死锁
- 任一组织开启即强制 = OR 语义，符合"安全策略叠加"的合规直觉（多个合规域只要有一个要求 MFA，整体就必须 MFA）

**4 个边界约束（已与用户确认，2026-06-13）：**

1. **多组织叠加 = OR**：用户隶属多个 org（通过 `ulp_organization_member`），只要**任一**直接归属的 org 的 `mfa_enforced=true`，该用户即被强制。**不沿父链继承**——只看 user → org 的直接归属关系，不沿 `ulp_organization.parentId` 向上向下传播。理由：路径继承复杂度高、admin 调整组织树时易误改强制范围；直接归属 = 简单 + 可预测
2. **强制下的解绑 = 直接 403**：被强制覆盖的 user 调 `POST /api/v1/mfa/unbind` 时 API 立刻拒绝，返回 `403 Forbidden` + `error="unbind_blocked_by_org_policy"`，**不进入 MFA 验证流程**（防止"解绑成功 → 下次登录被强拉重绑"的无意义往返）。理由：解绑被拒是策略级语义，不该浪费 TOTP 验证次数
3. **过渡期 = 0（立即生效）**：admin 在 console 给某个 org 切开强制位的那一刻，该 org 下所有未绑 MFA 的 user 在**下一次登录**时即被强拉到绑定页。**不提供 grace period 配置**。理由：grace 字段会引入"什么时候过期 / 过期了怎么 hard cutover / cutover 当天用户被惊到"的二次复杂度；如果业务需要软推广，由业务侧用站内信 / 邮件提前通知即可
4. **admin 不涉及组织**：admin 账号体系（`ulp_administrator` 表）**不参与组织强制**——admin 与组织无业务关系，永远走自愿。admin 的"强制位检查"逻辑直接 short-circuit 跳过组织查询

**实现细节**：

- DB schema（见 D7）：`ulp_organization` 加 `mfa_enforced BOOLEAN DEFAULT FALSE NOT NULL`；`ulp_user` / `ulp_administrator` 仍各加 `mfa_enabled BOOLEAN`、`totp_secret_cipher`、`backup_codes_json`
- 强制位判定：`OrgMfaPolicyService.isUserEnforced(userId)` —— 查 `ulp_organization_member where userId=? AND deleted=0` 拿到该用户所有直接归属的 orgId 列表 → `ulp_organization where id in (?) AND mfa_enforced=true AND deleted=0 LIMIT 1` 命中即返回 true（短路退出，不需要查全部）
- 登录注入点：复用 D5 的 `MfaAwareAuthenticationSuccessHandler`，但分支扩展为：
  - admin：只看 `mfa_enabled`，true 则走 challenge，false 则正常登录（不查组织）
  - user：先看 `mfa_enabled`，true 则走 challenge（与是否强制无关）；false 时再查 `isUserEnforced(userId)`，true 则 302 `/mfa/setup`（强拉绑定），false 则正常登录
- Admin 配置端点：`POST /api/v1/admin/organizations/{id}/mfa-policy` 接 `{ mfaEnforced: true|false }`，要求调用方 `ADMIN` 角色；操作审计 `ORG_MFA_POLICY_CHANGED`（见 observability spec）
- Console 个人设置页：所有 admin 看到的都是"自愿 MFA + 立即绑定 / 解绑"按钮，没有"组织强制覆盖"的概念干扰个人 UI（admin 的个人 MFA 与他管的组织策略无关）
- Console 组织管理页：每个 org 节点详情新增"强制 MFA"开关（直接复用现有 `ulp_organization` CRUD UI 加一个 boolean field）

**ROPC + 已开 MFA 的用户**：
- 与 D2 旧方案一致，未变化：openapi `/oauth2/token grant_type=password` 收到请求 → 找 user 实体 → 如果 `mfa_enabled = true` → 返回 `400 invalid_grant` + `error_description="MFA is required for this user; password grant is not supported"`
- 注意：ROPC 拒签**只看 `mfa_enabled`**，不查组织强制位——被组织强制但还没绑定的 user，ROPC 仍能拿 token（密码对就发），因为 ROPC 用户根本进不了"强拉绑定"流程。这是 ROPC 设计缺陷而非本期 spec 漏洞；想要彻底堵这条路，必须迁到 Auth Code Flow（见 advisory）

**配置开关红线**：
- MUST NOT 在 `application.yml` 暴露 "全局关闭组织强制" 开关——`mfa_enforced` 列只能通过 admin endpoint 改，配置文件不能旁路
- 不在 `application.yml` 暴露 "强制策略类型" 开关（如 "切回 admin-only 强制"）——保持单一策略模型，避免运维误配让全平台倒退到单因子

### D3: 备份码 = 10 个一次性 + Argon2id 哈希

- 选 A：绑定时一次性下发 10 个 8 位备份码，Argon2id 哈希后存 JSON 列；用一个删一个；全用完后强制重新生成
- 备选 B：滚动 10 个不刷新
- 备选 C：不做备份码

**Rationale**: A 是 OWASP 和 Google Authenticator/Authy 的标准做法。备份码本质是"应急救生圈"——丢手机时用，每用一次都意味着一次紧急救援。10 个数量足够正常用户用一年；用完强制重新生成等于强制用户重新意识到"还在用 MFA"。

**关键安全点**：
- 备份码生成：`SecureRandom` + 8 位 (`[0-9A-Z]`，去掉 0/O/1/I 易混字符) → 用户首次看到一次（页面显示 + 下载 .txt 按钮）
- 存储：Argon2id 哈希（复用 `PasswordEncoderFactories.createDelegatingPasswordEncoder()`，前缀 `{argon2}`），存 `backup_codes_json` 列里的 JSON 数组（每个元素是哈希字符串）
- 验证：用户提交 → 遍历 JSON 数组逐个 `matches()`（最多 10 次 Argon2id 验证，每次 ~30ms，总开销 < 300ms，可接受）
- 消费：验证成功后立刻从数组里删掉那一项并落库（同事务）
- 用完检测：每次消费后看剩余长度，<= 2 时下次登录页面提示用户重新生成；= 0 时强跳到重新生成页

### D4: TOTP 密钥静态加密 = AES-GCM + 部署方 KEK

- 数据库列 `totp_secret_cipher VARCHAR(255)` 存 base64(nonce ‖ ciphertext ‖ tag)
- 加密算法：AES-256-GCM（标准 AEAD，含完整性校验）
- KEK 来源优先级：`ulp.mfa.key-encryption-key` 配置 > `ULP_MFA_KEK` 环境变量
- KEK 格式：base64 编码的 32 字节随机数（256-bit）
- **缺失即拒启动**：应用启动时如果 KEK 不存在/格式错误，抛 `IllegalStateException` 让 Spring Boot 启动失败（不允许 hardcoded fallback，避免开发误配置上线）

**Rationale**: AES-GCM 是 NIST SP 800-38D 推荐 AEAD 模式，单次加密成本约 1µs，毫无性能负担。明文 secret 不出库是基本要求（防御 SQL injection / DBA 看库 / 备份泄露）。让部署方提供 KEK 而非应用自生成的原因：
- 应用自生成 KEK 必须落盘（不然重启丢密钥就所有 MFA 全废），落盘位置在应用所在容器，被攻击者拿到容器同时就拿到 KEK，等于没加密
- 让部署方在 K8s Secret / Vault / AWS KMS 里管理 KEK，攻击者拿到应用容器但拿不到 K8s Secret 才有实质防御价值

**文档要给的命令**：
```bash
# Linux/Mac
openssl rand -base64 32

# Windows PowerShell
[Convert]::ToBase64String([System.Security.Cryptography.RandomNumberGenerator]::GetBytes(32))
```

**Key rotation** (v1 不实现，但 design 留接口)：
- 列 `totp_secret_cipher` 不带版本号，v1 = 单 KEK 周期
- v2 引入时改为 `kek_version:base64(nonce||ct||tag)` 格式，配置变 `ulp.mfa.kek-versions` map，加密时用最新版本、解密时按版本号选

### D5: MFA gate 插入位置 + Challenge state 存储

**Form login (Console + Portal)：**

时序：
```
1. POST /login (username + password)
2. UsernamePasswordAuthenticationFilter 走 DaoAuthenticationProvider
3. DAP 验证密码成功 → authentication.setAuthenticated(true)
4. AuthenticationSuccessHandler 触发（这是注入点）
   a. 查用户 mfa_enabled
   b. 如果 false → 正常 commit session，返回原页（与现状一致）
   c. 如果 true → 把 Authentication 对象暂存到 Redis (key = ULP_MFA_PENDING:{uuid}, TTL 5 min)，
      下发 ulp-mfa-pending cookie（HttpOnly + Secure + SameSite=Strict）= 那个 UUID，
      返回 302 → /mfa/challenge（前端 SPA 路由）
5. 前端 /mfa/challenge 页：用户输 6 位 TOTP 码（或 "use backup code" 切到 8 位备份码输入）
6. POST /api/v1/mfa/challenge { code: "123456" } 或 { backupCode: "ABCD1234" }
   a. 读 cookie 拿 pending UUID
   b. 从 Redis 取暂存的 Authentication（取不到 = 过期/无效 → 401 + 清 cookie）
   c. 取 user TOTP secret 解密 → CodeVerifier.isValidCode(secret, code) constant-time 比对
      或备份码 → 遍历哈希列表 matches → 删消费项
   d. 成功 → 把 Authentication commit 到 SecurityContextHolder + Spring Session（首次出现真正登录态）
      → 清 Redis 暂存 + 清 ulp-mfa-pending cookie
      → 返回 200 + 重定向 URL
   e. 失败 → 失败计数 +1（Redis ULP_MFA_FAIL:{userId}），达 5 → 锁 15 min →
      返回 423 Locked + Retry-After: 900 + 审计 MFA_LOCKED_OUT
```

**Filter 注入实现**：
- 不写新 Filter（避免和 Spring Security 7 默认链冲突）
- 改 `ConsoleSecurityConfiguration` / `PortalSecurityConfiguration` 注入的 `AuthenticationSuccessHandler` —— 把现有 `SimpleUrlAuthenticationSuccessHandler` 包装成 `MfaAwareAuthenticationSuccessHandler`
- `MfaAwareAuthenticationSuccessHandler` 决定走 c 还是走 b

**为什么不直接 commit 然后在另一个 filter 拦截？**
- 那种做法会让 `SecurityContextHolder` 在 MFA 验证前就有完整 Authentication，所有 `@PreAuthorize` 全失效，MFA 等于摆设
- "暂存 Authentication 到 Redis + 短 TTL UUID cookie" 模式是 Spring Security 官方在 WebAuthn second-factor 示例里用的，业界共识

**ROPC (openapi)：**
- 改 `OAuth2AuthorizationResourceOwnerPasswordAuthenticationProvider`（fork in `ulp-protocol-oidc`）
- 拿到 `UserDetails` 后立刻检查 `mfa_enabled`，true 则 `throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_GRANT, "MFA is required for this user; password grant is not supported", ...))`
- 不进 MFA challenge 流程

### D6: 失败锁定 = 独立 counter，15 min，5 次阈值

- Redis key：`ULP_MFA_FAIL:{userType}:{userId}` = 计数 + TTL 15 min（滑动窗口风格）
- 阈值：连续 5 次失败 → 锁 15 min → 返回 423 Locked + Retry-After
- 锁定期间所有 `/api/v1/mfa/challenge` 请求直接拒签，不消耗 challenge UUID（避免攻击者用废一个合法 challenge）
- 成功验证 → 立刻清 counter
- 与密码登录失败锁定**独立**，避免相互影响（不让攻击者通过反复 MFA 失败间接锁死合法用户的密码登录通道）

### D7: DB Schema 改动

**新增列（同 changeset）：**

```xml
<changeSet id="add-mfa-totp-second-factor-1" author="frank">
  <addColumn tableName="ulp_administrator">
    <column name="mfa_enabled" type="boolean" defaultValueBoolean="false">
      <constraints nullable="false"/>
    </column>
    <column name="totp_secret_cipher" type="varchar(255)" />
    <column name="backup_codes_json" type="text" />
  </addColumn>
  <addColumn tableName="ulp_user">
    <column name="mfa_enabled" type="boolean" defaultValueBoolean="false">
      <constraints nullable="false"/>
    </column>
    <column name="totp_secret_cipher" type="varchar(255)" />
    <column name="backup_codes_json" type="text" />
  </addColumn>
  <addColumn tableName="ulp_organization">
    <column name="mfa_enforced" type="boolean" defaultValueBoolean="false">
      <constraints nullable="false"/>
    </column>
  </addColumn>
</changeSet>
```

`ulp_organization.mfa_enforced` 用于 D2 的"组织级强制位"。判定路径 `OrgMfaPolicyService.isUserEnforced(userId)` 走 `ulp_organization_member` 拿到该用户直接归属的所有 orgId → 查 `ulp_organization` 中是否存在任一 `mfa_enforced=true`（OR 语义，不沿父链继承）。不加 `idx_mfa_enforced` 索引——单租户场景下 `ulp_organization` 行数预期 < 10k，全表过滤成本可忽略；如果未来 v2 出现性能问题再加。

**新表（失败锁定计数持久化备份；主路径用 Redis，DB 表只是"重启 Redis 不丢锁"的兜底）：**

实际上 v1 **不要新建表**——Redis 失败计数丢失 = 重置锁定 = 用户重新可以试，对攻击者无利（他依然每 5 次就被锁），对合法用户友好。建表反而引入持久化失败计数的回收复杂度。

**回滚**：
- changeset 用 Liquibase 标准 `<rollback>` 反操作（dropColumn）
- 已绑定 MFA 的用户回滚后会丢 secret，要求出 advisory 提前通知

## Risks / Trade-offs

| 风险 | 缓解 |
|---|---|
| KEK 配错或丢失，所有已绑定 MFA 用户的 secret 无法解密 | 启动时校验 KEK 能解码 32 字节 base64；deployment 文档要求把 KEK 同时写入 K8s Secret 备份 + 团队密码管理器；Liquibase migration 也不要 backfill 任何 secret |
| 用户丢手机 + 也忘记备份码 | 走 admin 重置流程：管理员在 console 把目标用户 `mfa_enabled` 设回 false 并清空 `totp_secret_cipher` + `backup_codes_json`，用户下次登录被强制重新走绑定流程；该操作必须审计 (`ADMIN_RESET_USER_MFA` 事件，本期顺便加） |
| TOTP secret 暂存在 Redis 等待绑定的 5 min 窗口内，攻击者拿到该 UUID 可以替用户完成绑定 | UUID 用 cookie HttpOnly+Secure+SameSite=Strict 传，攻击者拿不到；Redis key 同时绑定 session ID，serverside double-check session 匹配；绑定流程只在已登录态下可用（先登录密码再去设置页绑定 MFA） |
| `dev.samstevens.totp` 上游不活跃（2022 后无 release），将来有 CVE 无人修 | 算法是 RFC 6238 + AES 解密都用 JDK 原生 + zxing 已 exclude，攻击面极小；如果未来 CVE 出现，迁到自写 ~50 行 (D1 备选 C) 工作量可控 |
| `verify` 接口 timing attack 通过 TOTP 验证时间差区分"用户存在/不存在" | TOTP 验证必须 constant-time（用 `MessageDigest.isEqual` 或库内置 `CodeVerifier.isValidCode`，已是 constant-time）；用户不存在时也跑一次假验证消耗等量时间，避免短路返回 |
| ROPC `mfa_required` 拒签会破坏现有用 ROPC 的客户端 | v1 单租户场景下 ROPC 不应该被生产使用（这本来就是 deprecated）；如果有真实使用方，advisory 中提示他们迁到 Auth Code Flow + Refresh Token |
| 备份码 10 次 Argon2id 验证 = 300ms 阻塞 | 单线程；用 ForkJoinPool 并发也行但代码复杂度更高，且 300ms 对 "用了备份码的用户" 是低频路径，可接受 |
| MFA 设置页面用户中途关掉浏览器，TOTP secret 留在 Redis | TTL 5 min 自动过期；下次再来要重新生成 secret |
| AES-GCM 密文长度不可控（base64 后超出 VARCHAR(255)） | 算：12 字节 nonce + ~20 字节 secret 密文 + 16 字节 tag = 48 字节 → base64 ~64 字符 → 远低于 255。安全。 |

## Migration Plan

**部署前置：**
1. 生成 KEK：`openssl rand -base64 32`
2. 写入 K8s Secret / env：`ULP_MFA_KEK=<那个值>`
3. 同时写入团队密码管理器作为灾难恢复备份

**部署：**
1. 三个 deployable 都拿到 `ULP_MFA_KEK` 环境变量后才启动（启动校验失败 fast）
2. Liquibase 自动跑 `add-mfa-totp-second-factor-1` changeset（加 6 列）
3. 老用户 `mfa_enabled` 默认 false，无影响
4. Admin 用户下次登录被强跳绑定页（行业惯例，advisory 通知）

**回滚（极端情况）：**
1. Liquibase `<rollback>` 反操作丢 6 列（已绑定的 secret 全丢）
2. 用户回退到单因子，admin 也回退（这是降级到不安全状态，必须有 emergency change 文档）

## Open Questions

无。所有决策点已闭环。
