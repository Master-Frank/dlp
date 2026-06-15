## ADDED Requirements

### Requirement: TOTP 共享密钥静态加密

ULP SHALL 永不在 DB 持久层存储明文 TOTP 共享密钥。`ulp_administrator.totp_secret_cipher` 与 `ulp_user.totp_secret_cipher` 列 MUST 存 AES-256-GCM 加密后的密文（Base64 编码 `nonce ‖ ciphertext ‖ tag` 拼接结果）。

加密参数：

- 算法 = `AES/GCM/NoPadding`（NIST SP 800-38D 推荐的 AEAD 模式，含完整性校验）
- 密钥（KEK）= **256 bits（32 bytes）**，来源见下方 KEK 管理 Requirement
- Nonce（IV）= 每次加密**新生成** 12 字节随机数（`SecureRandom`）
- Tag 长度 = 128 bits（AES-GCM 默认）

任何接受用户输入或从 DB 读出 secret 的代码路径 MUST 经过统一的 `MfaSecretCipher` 工具（位于 `ulp-support`），不允许各模块自实现 AES 调用。

#### Scenario: DB 中无明文 secret
- **WHEN** 任意用户完成 MFA 绑定后查询 `SELECT totp_secret_cipher FROM ulp_user WHERE id = ?`
- **THEN** 返回字符串是 Base64 编码，解码后长度 ≥ 48 字节（12 nonce + ≥ 20 ct + 16 tag），且**不**能被 Base32 解码为合法 TOTP secret

#### Scenario: 密文 nonce 每次不同
- **WHEN** 对同一明文 secret 调 `MfaSecretCipher.encrypt(secret)` 两次
- **THEN** 两次返回的 Base64 密文不同（nonce 随机），但两次密文都能被 `decrypt` 还原为同一原文

### Requirement: MFA KEK 部署侧管理

应用启动 SHALL 从下列来源加载 MFA KEK（优先级从高到低）：

1. Spring 配置键 `ulp.mfa.key-encryption-key`（推荐 K8s Secret / Vault 注入到 `application.yml` 或 `application-{profile}.yml`）
2. 环境变量 `ULP_MFA_KEK`

KEK 格式 = Base64 编码的 32 字节随机数（256 bits）。

启动期校验 MUST：

- 缺失（两个来源都未提供）或为空 → 抛 `IllegalStateException(message="ulp.mfa.key-encryption-key 未配置，参考 README 'MFA KEK 生成' 段")` 让 Spring Boot 启动失败
- Base64 解码失败 → 抛 `IllegalStateException(message="ulp.mfa.key-encryption-key Base64 解码失败")`
- 解码后字节数 ≠ 32 → 抛 `IllegalStateException(message="ulp.mfa.key-encryption-key 长度必须为 32 字节，当前 N 字节")`

MUST NOT 提供任何 hardcoded fallback KEK（避免开发环境配置遗漏带到生产）。

部署文档（README / CLAUDE.md）SHALL 提供 KEK 生成命令（`openssl rand -base64 32` 或 PowerShell `RandomNumberGenerator.GetBytes(32)`）。

#### Scenario: KEK 缺失启动失败
- **WHEN** 三个部署单元任一启动时 `ulp.mfa.key-encryption-key` 配置和 `ULP_MFA_KEK` 环境变量都未设置
- **THEN** Spring Boot 启动失败，日志含 `IllegalStateException: ulp.mfa.key-encryption-key 未配置`

#### Scenario: KEK 长度错误启动失败
- **WHEN** 配置 `ulp.mfa.key-encryption-key=YWJjZA==`（Base64 解码后 4 字节）
- **THEN** 启动失败，日志含 `长度必须为 32 字节，当前 4 字节`

#### Scenario: 优先级 — 配置覆盖环境变量
- **WHEN** 同时配置 `ulp.mfa.key-encryption-key=KEY_A` 与环境变量 `ULP_MFA_KEK=KEY_B`
- **THEN** 实际使用的 KEK 解密任意密文时与 `KEY_A` 加密结果一致

### Requirement: MFA 备份码哈希存储

备份码 MUST 以 `{argon2}` 前缀的 Argon2id 哈希形式存储在 `ulp_administrator.backup_codes_json` 与 `ulp_user.backup_codes_json` 列。

复用 `cn.frank.ulp.support.security.crypto.password.PasswordEncoderFactories.createDelegatingPasswordEncoder()`，参数下限继承自现有"密码哈希算法基线" Requirement（memory ≥ 19456 KB / iterations ≥ 2 等）。

MUST NOT 存储备份码明文、SHA-256/SHA-512 单轮哈希或任何非 Argon2id 算法。

DB 列类型 = `TEXT`（容纳 10 条 Argon2id 字符串，单条约 100 字符，总约 1KB）。

#### Scenario: 备份码列含 Argon2id 前缀
- **WHEN** 用户完成 MFA 绑定后查询 `SELECT backup_codes_json FROM ulp_user WHERE id = ?`
- **THEN** 该列值是 JSON 数组，每元素以 `{argon2}` 开头

#### Scenario: 不接受非 Argon2id 哈希写入
- **WHEN** 评审 `ulp-support` 中备份码生成与持久化代码
- **THEN** 备份码写库前**必须**经过 `passwordEncoder.encode(...)`，且 `passwordEncoder` 是 `DelegatingPasswordEncoder` 实例（默认 id = `argon2`）

### Requirement: MFA Challenge 暂存安全约束

MFA Challenge 流程使用的 Redis 暂存 Authentication 对象 MUST 满足下列安全约束：

- Key 格式 = `ULP_MFA_PENDING:{uuid}`，`{uuid}` MUST 由 `java.util.UUID.randomUUID()` 生成（122-bit 熵）
- TTL ≤ **5 分钟**（300 秒）
- 关联 cookie `ulp-mfa-pending` 属性 = `HttpOnly=true; Secure=true; SameSite=Strict; Path=/`
- Cookie value = UUID（不携带其他信息）
- 同一个 UUID 验证成功或显式取消（用户切换账号、退出登录）后 MUST 立刻 DEL Redis key + Max-Age=0 清 cookie
- Challenge 端点 MUST 校验 cookie UUID 与请求来源 IP 在同一 `/24` 网段（IPv4）或同一 `/64` 网段（IPv6），不一致返回 401 + `error="challenge_session_invalid"`（防御 cookie 窃取后异地利用）

绑定流程的 Redis 暂存 `ULP_BIND_MFA_SECRET:{userType}:{userId}` MUST：

- TTL ≤ **5 分钟**
- 仅在已通过密码登录（`SecurityContextHolder` 已有 Authentication）的会话内可读 / 可消费
- 绑定 confirm 验证成功或失败 5 次（防滥用）后 MUST 立刻 DEL

#### Scenario: pending cookie 属性完整
- **WHEN** 已开 MFA 用户密码登录后，浏览器收到 `Set-Cookie: ulp-mfa-pending=...` header
- **THEN** 该 cookie 含 `HttpOnly`、`Secure`、`SameSite=Strict`、`Path=/` 四个属性

#### Scenario: pending key TTL ≤ 5 分钟
- **WHEN** 已开 MFA 用户密码登录后，立即在 Redis 上 `TTL ULP_MFA_PENDING:{uuid}` 查询
- **THEN** 返回值 ≤ 300 秒

#### Scenario: pending 跨网段使用被拒
- **WHEN** 攻击者偷到合法用户的 `ulp-mfa-pending` cookie 并从不同 `/24` 网段发起 challenge
- **THEN** 返回 401 + `error="challenge_session_invalid"`，Redis pending key 未被消费

### Requirement: TOTP 验证 constant-time

`MfaCodeVerifier.verify(secret, code)` 实现 MUST：

- 使用 `dev.samstevens.totp.code.CodeVerifier.isValidCode(...)` 或等价 constant-time 比对（基于 `MessageDigest.isEqual` 或 `Arrays.equals` 替代为 `MessageDigest.isEqual`）
- 用户不存在 / `mfa_enabled = false` / `totp_secret_cipher = NULL` 等场景 MUST 仍走一次"假验证"消耗等量 CPU 时间（约 1ms），避免 timing attack 区分账户状态
- 备份码 `matches()` 遍历 MUST 不短路（即使第一条匹配也跑完所有 10 条；或保证每次失败的总耗时与成功一致 ± 1ms 内）

#### Scenario: 不存在的用户 challenge 耗时与正常用户一致
- **WHEN** 攻击者用合法 `ulp-mfa-pending` cookie 触发 challenge，但 Redis 中关联的用户 ID 实际不存在 DB
- **THEN** 响应耗时与"用户存在但 TOTP 错误"耗时差 ≤ 5ms（10 次采样均值，简单 sanity check 即可）

#### Scenario: 比对走 constant-time API
- **WHEN** 评审 `MfaCodeVerifier` 实现代码
- **THEN** 使用 `dev.samstevens.totp` 内置 `DefaultCodeVerifier` 或显式 `MessageDigest.isEqual(expected, actual)`，**不**使用 `String.equals` / `Arrays.equals(byte[],byte[])` 进行 OTP 字节级比对
