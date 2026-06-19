<div align="center">

<img src="images/logo-wide.svg" alt="logo" height="100" width="426"/>

## IAM / IDaaS 身份管理平台

[![](https://img.shields.io/badge/JDK-21+-orange?style=flat-square)](https://adoptium.net/temurin/releases/?version=21)
[![](https://img.shields.io/badge/Spring%20Boot-4.0-6db33f?style=flat-square)](https://spring.io/projects/spring-boot)
[![](https://img.shields.io/badge/MySQL-8.0%2B-brightgreen?style=flat-square)](https://www.mysql.com/downloads/)
[![](https://img.shields.io/badge/Redis-7+-dc382d?style=flat-square)](https://redis.io/download/)
[![](https://img.shields.io/badge/Maven-3.9+-brightgreen.svg?style=flat-square)](https://maven.apache.org)

</div>

## 项目介绍

ULP（United Login Platform），是一个统一登录系统，支持 OIDC、OAuth2、SAML2、JWT、CAS 等主流认证协议，并能够集成钉钉、企业微信、飞书、LDAP、AD 等多种身份源，轻松实现用户全生命周期管理与数据同步。在认证方面，支持用户名密码、短信/邮箱验证码等常规认证方式，并能集成钉钉、飞书、微信、企业微信、QQ 等社交平台登录，让用户能够通过常见平台便捷登录，从而显著提升用户体验。在安全性方面，提供多因素认证、防暴力破解、会话管理、密码策略等能力，提升系统安全性。在审计方面，提供全面的行为审计功能，详尽记录用户行为，发现潜在安全风险并及时采取防范措施，确保合规性和安全性。通过 ULP，企业和团队能够快速实现统一的内外部身份认证，并集成各类应用，实现“一个账号、一次认证、多点通行”的效果，强化企业安全体系，提高组织管理效率，提升用户体验，助力企业数字化升级转型。


## 核心特性

+ 提供统一组织信息管理，多维度建立对应关系，实现在一个平台对企业人员、组织架构、应用信息的高效统一管理。
+ 支持钉钉、飞书、企业微信等身份源集成能力，实现系统和企业OA平台数据联动，以用户为管理基点，结合入职、离职、调岗、兼职等人事事件，关联其相关应用权限变化而变化，保证应用访问权限的安全控制。
+ 支持多因素认证，行为验证码、社交认证，融合认证等机制，保证用户认证安全可靠。
+ 支持微信、钉钉、飞书QQ等社交认证集成，使企业具有快速纳入互联网化认证能力。
+ 支持 `SAML2`，`OAuth2`，`OIDC`，`CAS`，`JWT`,`表单代填`等认证协议及机制，实现单点登录功能，预配置大量 SaaS 应用及传统应用模板，开箱即用。
+ 完善的安全审计，详尽记录每一次用户行为，使每一步操作有据可循，实时记录企业信息安全状况，精准识别企业异常访问和潜在威胁的源头。
+ 提供标准`RESTAPI`、`SCIM2.0`接口轻松完成机构用户同步，提供`HTTP`、`MQ`事件通知，实现企业对于账号生命周期的精细化管理。


## 功能描述

| 功能模块 | 功能项      | 功能描述                                                                                                                                                  |
|------|----------|-------------------------------------------------------------------------------------------------------------------------------------------------------|
| 账户管理 | 组织与用户    | 组织与用户用于解决企业组织、用户等实体的管理问题。这些实体您可以直接在ULP中进行维护，也可以通过配置身份源后，同步身份源的数据。                                                                                     |
|      | 用户组管理    | 用户组是用户的集合，将具有相同职能的用户添加到一起，即形成用户组。将权限授权到用户组后，用户组内的用户都将继承该权限，因此，用户组十分适合用于批量授权。                                                                          |
|      | 身份源管理    | 支持企业以多种身份源途径同步用户和组织信息到系统，在高级配置中可以对导入的处理逻辑进行灵活配置 ，实现从多个数据源的汇聚为一个完整的用户目录，部分身份源还可以通过回调的方式支持实时同步。                                                         |
| 认证管理 | 认证提供商    | 支持设置多种身份提供商，企业用户即可通过不同方式登录门户。系统默认的认证源为用户密码和短信快捷认证，您还可添加钉钉、微信、企业微信、QQ等作为认证源。                                                                           |
| 应用管理 | OIDC协议应用 | OIDC是OpenID Connect的简称，OIDC=(Identity, Authentication) + OAuth 2.0。它在OAuth2上构建了一个身份层，是一个基于OAuth2协议的身份认证标准协议。OIDC是一个协议族，提供很多的标准协议，包括Core核心协议和一些扩展协议。   |
|      | JWT协议应用  | JWT（JSON Web Token）是在网络应用环境声明的一种基于 JSON 的开放标准。ULP 使用 JWT 进行分布式站点的单点登录 （SSO）。JWT 单点登录基于非对称加密，由 ULP 将用户状态和信息使用私钥加密，传递给应用后，应用使用公钥解密并进行验证。使用场景非常广泛，集成简单。  |
|      | 表单代填应用   | 表单代填可以模拟用户在登录页输入用户名和密码，再通过表单提交的一种登录方式。应用的账号密码在 ULP 中使用 AES256 加密算法本地加密存储。很多旧系统、不支持标准认证协议的系统或不支持改造的系统可以使用表单代填实现统一身份管理。表单中有图片验证码、CSRF token、动态参数的场景不适用。 |
|      | 应用分组     | 应用分组管理是一种将多个应用程序或功能按照一定的逻辑或相关性进行组织和管理的方法。通过应用分组，可以更有效地管理大量应用程序，提供更好的用户体验。                                                                             |
| 行为审计 | 用户行为     | 审计日志记录了所有平台用户进行的关键操作，以对某次改变提供充分的溯源数据。                                                                                                                 |
|      | 管理员行为    | 审计日志记录了所有平台管理员进行的关键操作，以对某次改变提供充分的溯源数据。                                                                                                                |
| 安全设置 | 通用安全     | 支持通用安全配置，及安全防御策略。                                                                                                                                     |
|      | 密码策略     | 系统全局安全配置、如密码策略、会话策略。 在密码策略中可以设置相应的密码复杂度、相应的锁定解锁策略，还可以设置是否允许与历史密码重复等高级策略。同时，可以通过开启弱密码字典库来检查密码的安全强度。                                                    |
|      | 系统管理员    | 系统管理员负责保证系统的安全性、稳定性和合规性。                                                                                                                              |
| 系统设置 | 消息设置     | 消息服务设置包括邮件服务配置和短信服务配置。邮件服务配置是指将消息发送到指定的电子邮件地址，需要配置SMTP服务器地址、端口号、用户名、密码等信息。短信服务配置是指将消息以短信的形式发送到指定的手机号码，需要配置短信服务提供商的API接口地址、账号、密码等信息。                   |
|      | IP地理库    | 配置IP地理库根据访问者的IP地址迅速识别出其所在的地理位置。                                                                                                                       |
|      | 存储配置     | 支持配置云存储服务，如阿里云、腾讯云、MinIO等。                                                                                                                            |
| 系统监控 | 会话管理     | 会话管理可以查看所有当前仍然有效的会话列表，并且可以强制注销某个用户的会话。注销后，该用户凭证即刻失效，之后的所有操作都需要重新认证。                                                                                   |

## 技术架构

- **后端**：[Spring Boot](https://spring.io/projects/spring-boot/) 4.0、[Spring Security](https://spring.io/projects/spring-security/) 7、[Spring Framework](https://spring.io/projects/spring-framework) 7、[Hibernate](https://hibernate.org/) 7、[Liquibase](https://www.liquibase.org/) 5
- **前端**：[React.js](https://react.dev/) 、[Ant Design](https://ant.design)
- **中间件**：[MySQL](https://www.mysql.com/) 、[Redis](https://redis.io/)
- **基础设施**：[Docker](https://www.docker.com/)

## 运行时要求

| 组件 | 最低版本 | 说明 |
|---|---|---|
| **JDK** | 21 LTS | Spring Boot 4 / Hibernate 7 / Spring Framework 7 均要求 Java 21；推荐 Temurin。Java 17 已不支持 |
| **Maven** | 3.9+ | 用仓库自带 `./mvnw.cmd` / `./mvnw` 可免装；本机若装 Maven 需 3.9 以上 |
| **MySQL** | 8.0+ | 数据库 schema 由 Liquibase 5 自动迁移；建表前先建空库 `ulp`（utf8mb4） |
| **Redis** | 7+ | 用于 Spring Session（indexed）、缓存、限流；客户端为 Lettuce |
| **Docker** | 20.10+ | 仅集成测试需要（Testcontainers 拉起 MySQL/Redis）。运行生产服务本身不强依赖 Docker |

> 旧 fork 从 Spring Boot 3.2.x 升 4.0.x，主要破坏性变更：Jackson 包名 `com.fasterxml.jackson.databind` → `tools.jackson.databind`、`spring.session.redis.flush-mode` 配置键作废（改在代码里设 `FlushMode.IMMEDIATE`）、`spring.data.jpa.repositories.bootstrap-mode=deferred` 与 Hibernate 7 异步装配死锁需改 `default`、Spring Security 7 移除大量旧 DSL（lambda 风格强制 + `requestMatchers(RegexRequestMatcher.regexMatcher(...))` 改 `PathPatternRequestMatcher`）。本仓库的 [openspec 变更记录](openspec/changes/upgrade-spring-boot-4/) 含完整迁移步骤可参考。

## 健康检查 / 指标端点

三个部署服务都接入了 Spring Boot Actuator，对外端点统一在 `/actuator/**`。

| 服务 | 端口 | 健康（公开） | 探针（公开） | 指标（公开） | 信息（公开） | 敏感端点（鉴权） |
|---|---|---|---|---|---|---|
| ulp-console | 1898 | `/actuator/health` | `/actuator/health/liveness`<br/>`/actuator/health/readiness` | `/actuator/prometheus` | `/actuator/info` | `/actuator/env`、`/actuator/loggers`、`/actuator/metrics`、`/actuator/mappings` 需 ADMIN |
| ulp-portal | 1989 | 同上 | 同上 | 同上 | 同上 | 同上敏感端点 `denyAll`（终端用户入口无诊断角色） |
| ulp-openapi | 1988 | 同上 | 同上 | 同上 | 同上 | 同上敏感端点 `denyAll`（OAS 鉴权体系内不暴露 actuator） |

- `/actuator/health` 含 `components.db` / `components.redis` / `liquibaseChangelogDrift` 三个聚合指标；外部依赖瞬断会反映在整体 status 上
- `/actuator/health/liveness` 仅反映应用自身存活（与 DB/Redis 解耦），供 k8s liveness probe 与 Docker `HEALTHCHECK` 使用
- `/actuator/info` 暴露 `build.*` / `git.*` / `runtime.*`（Spring Boot / Spring Framework / Java / Jackson 版本），用于线上排查"这个 commit 跑的是哪个 SB 版本"
- `/actuator/prometheus` 已含 Micrometer JVM + HTTP server requests + DB 连接池等基本指标；网关层若用 nginx，建议加 rate limit 防爬

容器编排已配置健康检查：三个 `Dockerfile` 都带 `HEALTHCHECK` 指令（30s 间隔，60s start-period，3 次失败标记 unhealthy），`deploy/docker/docker-compose.yml` 中 `nginx-web` 通过 `depends_on: service_healthy` 等三后端就绪后再启动。

## 运行集成测试

集成测试用 [Testcontainers](https://www.testcontainers.org/) 启动真实的 MySQL 8 + Redis 7 实例，运行前需要本机有可用的 Docker Engine（Docker Desktop 或等价方案）。

```bash
# 跑所有单元测试（不启动任何容器，零外部依赖）
./mvnw.cmd clean test

# 跑所有单元测试 + 集成测试（启动 MySQL + Redis 容器，约 2 分钟一个模块）
./mvnw.cmd clean verify -DskipTests=false
```

测试命名约定：

- `*Test.java` —— Surefire 在 `test` 阶段执行，单元测试，不启容器
- `*IT.java` —— Failsafe 在 `verify` 阶段执行，集成测试，每个 JVM 启动一次 MySQL + Redis 容器

注：根 `pom.xml` 默认 `<skipTests>true</skipTests>`，CI 与本地都需在 CLI 显式传 `-DskipTests=false`。

### 容器复用加速（可选）

每次 `verify` 默认重新拉起一对新容器，约 30 秒冷启动开销。开发期可以打开 Testcontainers reuse 让容器在多次运行间保持存活：

```bash
# 在用户主目录写一次即可
echo "testcontainers.reuse.enable=true" >> ~/.testcontainers.properties
```

开启后第一次 `verify` 仍要 30 秒冷启动，后续运行会复用已存在的容器，单模块 IT 套件耗时从 ~2 分钟降到 ~30 秒级。CI 上不建议启用（每次跑都是干净环境）。

### 给新模块加集成测试

`ulp-support` 把共享测试基础设施（`AbstractIntegrationTest`、`SharedContainers` 等）打成 `test-jar`，下游模块只需在 `pom.xml` 加：

```xml
<dependency>
    <groupId>cn.frank.ulp</groupId>
    <artifactId>ulp-support</artifactId>
    <version>${project.version}</version>
    <type>test-jar</type>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<!-- 如需 Spring Security 的 mock —— 比如 SecurityMockMvcRequestPostProcessors -->
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```

然后写一个 `XxxControllerIT extends AbstractIntegrationTest` 即可，容器与 Spring Boot 测试上下文都由基类托管。

## MFA 部署前置

启用 TOTP 第二因子前，三个部署单元（`ulp-console` / `ulp-portal` / `ulp-openapi`）都需要注入同一把 **KEK**（Key Encryption Key），用于 AES-256-GCM 加密 `ulp_user.totp_secret_cipher` / `ulp_administrator.totp_secret_cipher`。KEK 缺失、长度不对、非合法 Base64 都会让 `MfaSecretCipher.validateKek()` 在 `@PostConstruct` 阶段抛 `IllegalStateException`，应用启动直接失败 —— 这是有意为之的 fail-fast。

### KEK 生成

```bash
# Linux / macOS / WSL / Git Bash
openssl rand -base64 32

# Windows PowerShell（无 openssl 时）
[Convert]::ToBase64String([System.Security.Cryptography.RandomNumberGenerator]::GetBytes(32))
```

输出形如 `aB3xY...Q==`，**必须**是 Base64 编码的 32 字节随机串（解码后正好 256 bit）。本地开发可写 `~/.bashrc` 或项目 `.env`：

```bash
export ULP_MFA_KEK="aB3xY...Q=="
```

### K8s Secret 示例

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: ulp-mfa-kek
type: Opaque
stringData:
  ULP_MFA_KEK: aB3xY...Q==  # openssl rand -base64 32
---
# Deployment 引用（三个服务都需要）
spec:
  template:
    spec:
      containers:
        - name: ulp-portal
          envFrom:
            - secretRef:
                name: ulp-mfa-kek
```

### 灾备备份（强烈建议）

KEK 一旦丢失，**所有已绑定的 TOTP secret 都无法解密** —— 用户必须重新走 `prepare → confirm` 绑定流程，已发的备份码也全部作废。务必：

- 把 KEK 同步存进团队密码管理器（1Password / Bitwarden / Vault 等）的"基础设施密钥"分组，至少两个负责人有访问权
- 轮换 KEK 是破坏性操作，需要先把所有 secret 解密 → 用新 KEK 加密回写 → 翻 env var；当前版本未提供自动迁移工具，规划在专门变更中处理
- `ULP_MFA_KEK` 仅用于加密 TOTP secret，与 ROPC / Session / DB 密码 hash 完全无关，不要复用其它服务的密钥

### ⚠️ ROPC（OAuth2 Password Grant）行为变更

启用 MFA 后，**任何已开启 `mfa_enabled` 的用户都无法继续走 ROPC 密码模式**——`POST /oauth2/token grant_type=password` 会返回：

```
HTTP/1.1 400 Bad Request
{
  "error": "invalid_grant",
  "error_description": "mfa_required_use_authorization_code_flow"
}
```

原因：ROPC 协议本身没有携带第二因子的标准字段，按规范无法承载 OTP / 备份码挑战。OpenAPI 集成方有 MFA 需求时，**必须迁移到 Authorization Code Flow（推荐 PKCE）**——OIDC `/oauth2/authorize` 端点会按需触发 `/mfa/challenge` 中转页，完成挑战后正常下发 code，token 互换路径不变。未启用 MFA 的用户仍可继续 ROPC，本次变更对其无影响。

## 参与贡献

欢迎有兴趣的开发者参与到项目建设中，欢迎大家对项目提出宝贵意见建议和功能需求。

强烈推荐阅读 [《提问的智慧》](https://github.com/ryanhanwu/How-To-Ask-Questions-The-Smart-Way)、[《如何向开源社区提问题》](https://github.com/seajs/seajs/issues/545)
和 [《如何有效地报告 Bug》](http://www.chiark.greenend.org.uk/%7Esgtatham/bugs-cn.html)、[《如何向开源项目提交无法解答的问题》](https://zhuanlan.zhihu.com/p/25795393)
，更好的问题更容易获得帮助。
