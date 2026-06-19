# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

ULP (United Login Platform) is an IAM/IDaaS platform that issues identities and brokers SSO via OIDC / OAuth2 / SAML2 / JWT / CAS / form-fill, with pluggable identity sources (DingTalk, Feishu, WeCom, LDAP/AD) and social authenticators (WeChat, QQ, Gitee, GitHub, Alipay, mail, SMS).

GroupId / version: `cn.frank.ulp` / `1.1.0`. Java package root: `cn.frank.ulp`.

## Runtime baseline (locked by `openspec/specs/runtime-baseline/spec.md`)

These versions are not aspirational — they are part of a written spec. Any downgrade must update that spec in the same PR.

- JDK 21 LTS (Temurin recommended) — `<maven.compiler.source/target>` and `<java.version>` all 21
- Spring Boot **4.0.x** (parent = `spring-boot-starter-parent` 4.0.7), Spring Framework 7, Spring Security 7, Spring Authorization Server 1.5+, Spring Session 4, Hibernate 7, Liquibase 5, Jakarta EE 11 / Servlet 6.1
- Jackson **3** (`tools.jackson.*`). `com.fasterxml.jackson.databind.*` / `core.*` / `module.*` imports are forbidden; only `com.fasterxml.jackson.annotation.*` survives
- MySQL 8.0+, Redis 7+. Docker only required for integration tests.

The repo's last big migration (3.2 → 4.0) is documented in `openspec/changes/archive/2026-06-11-upgrade-spring-boot-4/`. Read its `design.md` / `phase-2-compile-errors.md` before touching Jackson/Security/Session/Hibernate plumbing — most "weird" config in this repo is a deliberate workaround from that work.

## Module layout

Maven multi-module. Three deployable Spring Boot services on top of a layered library stack.

```
ulp-support        test-jar of shared IT infra (AbstractIntegrationTest, SharedContainers); also runtime helpers (security, cache, repository, jackson, hibernate)
ulp-core           domain entities, repositories, core services
ulp-common         shared DTOs/utils  +  src/main/resources/db/ulp-changelog-master.xml  ← single Liquibase root for all services
ulp-audit          audit logging
ulp-protocol       SSO protocol layer (-core / -oidc / -jwt / -form / -all)
ulp-application    application registry layer mirroring protocol (-core / -oidc / -jwt / -form / -all)
ulp-authentication identity provider integrations (-core + -alipay/-dingtalk/-feishu/-gitee/-github/-mail/-qq/-sms/-wechat/-wechatwork/-all)
ulp-identity-source identity sync sources (-core / -dingtalk / -feishu / -all)
ulp-openapi        deployable: public REST + SCIM 2.0 + OIDC AS endpoints   (port 1988, UlpOpenApiApplication)
ulp-portal         deployable: end-user portal + login UI                  (port 1989, UlpPortalApplication; FE in src/main/portal-fe)
ulp-console        deployable: admin console                                (port 1898, UlpConsoleApplication; FE in src/main/console-fe)
ulp-synchronizer   identity-source sync runner
```

The `-all` submodules are aggregator JARs that depend on every sibling (e.g. `ulp-application-all` pulls oidc + jwt + form). Deployable services usually depend on the `-all` flavors, not individual protocol modules.

`ulp-portal/src/main/java/cn/topiam/` is leftover empty scaffolding from the original TopIAM fork — real code lives under `cn/frank/ulp/portal/`. Don't put new code in `cn/topiam`.

## Build & run

The repo ships a Maven wrapper. On Windows use `./mvnw.cmd`, on Git Bash / Unix use `./mvnw`. Local Maven, if installed, must be 3.9+.

```bash
# Compile everything (license + formatter + impsort run on compile phase — see "Code style" below)
./mvnw.cmd clean compile

# Unit tests only — no containers, fast
./mvnw.cmd clean test -DskipTests=false

# Unit + integration tests — boots MySQL 8 + Redis 7 via Testcontainers (~2 min per module cold)
./mvnw.cmd clean verify -DskipTests=false

# Run a single deployable service from its module dir
cd ulp-console && ../mvnw.cmd spring-boot:run
cd ulp-portal  && ../mvnw.cmd spring-boot:run
cd ulp-openapi && ../mvnw.cmd spring-boot:run

# Run a single test class / method
./mvnw.cmd -pl ulp-support test -DskipTests=false -Dtest=SomeTest
./mvnw.cmd -pl ulp-portal verify -DskipTests=false -Dit.test=OidcAuthorizationCodeFlowIT
```

> **Root `pom.xml` defaults `<skipTests>true</skipTests>`** — both locally and in CI you must pass `-DskipTests=false` to actually run anything. This is intentional, not a bug.

### Optional: container reuse for faster IT loops

Each `verify` boots fresh containers (~30 s cold start). To reuse across runs on a dev box:

```bash
echo "testcontainers.reuse.enable=true" >> ~/.testcontainers.properties
```

Never enable this in CI.

### Docker Engine 29 testcontainers shim

`pom.xml` sets `API_VERSION=1.41` env var + `api.version=1.41` system prop on Failsafe. docker-java's `DefaultDockerClientConfig` would otherwise downgrade to API 1.32 and Docker Engine 29 rejects it with `400 BadRequest`. Don't remove these.

## Tests

- `*Test.java` → Surefire, `test` phase, no containers
- `*IT.java` → Failsafe, `verify` phase, real MySQL + Redis via Testcontainers

To add ITs to a module, depend on `ulp-support`'s test-jar plus the standard Boot test starters, then extend `AbstractIntegrationTest` (see README "给新模块加集成测试" for the exact dependency block). The test-jar is published from `ulp-support` via `maven-jar-plugin` goal `test-jar`.

Per-test isolation rules (from `openspec/specs/integration-testing/spec.md`):
- Spring-managed transactions roll back at end of each method — SQL must not leak between tests
- Redis-using tests **must** clean up in `@AfterEach`
- H2 / embedded-redis are forbidden — use the containerized base class

## Configuration that's easy to get wrong

The Boot 4 migration left a handful of non-obvious wirings. Don't "fix" these without reading `runtime-baseline` first.

- **Spring Session** (`ConsoleSessionConfiguration` / `PortalSessionConfiguration`): Boot 4 removed `spring-boot-session` auto-config. `spring.session.redis.flush-mode` and `repository-type` keys no longer bind — they live in code now (`@EnableRedisIndexedHttpSession` + a `SessionRepositoryCustomizer` bean calling `setFlushMode(FlushMode.IMMEDIATE)`). Yml keeps `spring.session.redis.namespace` only as a placeholder our own code reads.
- **JPA bootstrap**: `spring.data.jpa.repositories.bootstrap-mode: default` (not `deferred`). Hibernate 7 async EMF bootstrap deadlocks against `MultiTenancy.getTenantIdentifierResolver` → `SpringBeanContainer.getBean` on the BeanFactory lock.
- **Web server**: Boot 4 dropped the Undertow starter (Undertow upstream archived). The deployables ship on Tomcat. Some `server.undertow.*` keys still live in `application.yml` and are pending migration to `server.tomcat.*`.
- **Liquibase**: Single root changelog at `ulp-common/src/main/resources/db/ulp-changelog-master.xml`. All services consume it transitively via `ulp-common`. Pre-create an empty `ulp` schema (utf8mb4) before first boot.
- **Security DSL**: must be Spring Security 7 lambda form (`http.csrf(c -> c.disable())`). `.antMatchers(...)` is banned; use `requestMatchers(...)` (and for regex routes, `PathPatternRequestMatcher`, not `RegexRequestMatcher`).
- **Actuator authorization** lives in a SEPARATE `actuatorSecurityFilterChain` `@Bean` in each of the three services (`ConsoleSecurityConfiguration` / `PortalSecurityConfiguration` / `OpenApiSecurityConfiguration`). The main API chain uses `.securityMatcher(API_PATH+"/**")` and won't match `/actuator/**` — adding rules to it has no effect. Use `@Order(Ordered.HIGHEST_PRECEDENCE)` on the actuator chain; public-list `health/info/prometheus` and gate the rest (`/actuator/env`, `/actuator/loggers`, `/actuator/metrics`, `/actuator/mappings`) with `denyAll()` on portal/openapi or `hasRole("ADMIN")` on console. Any new actuator endpoint MUST be reviewed against all three SecurityConfigurations — leaking `env`/`loggers` would expose secrets and runtime control.
- **Password encoder & auto-upgrade**: `PasswordEncoderFactories` (in `ulp-support`) is a `DelegatingPasswordEncoder` with default id `argon2` (Argon2id, OWASP 2024 baseline: `m=19456 KiB, t=2, p=1, saltLen=16, hashLen=32`). `{bcrypt}` and `{noop}` remain registered for legacy verification. Auto-rehash on login requires **explicit** `@Bean DaoAuthenticationProvider` + `provider.setUserDetailsPasswordService(...)` in **every** form-login SecurityConfiguration — Spring Security 7's `InitializeUserDetailsManagerConfigurer` only auto-wires a DAP when no `AuthenticationProvider` bean exists, and even when it does, it does NOT inject `UserDetailsPasswordService`. Current wiring:
  - `ConsoleSecurityConfiguration#daoAuthenticationProvider` (admin lib) + `UserDetailsPasswordServiceImpl` writing `ulp_administrator`
  - `PortalSecurityConfiguration#daoAuthenticationProvider` (user lib) + `UserDetailsPasswordServiceImpl` writing `ulp_user`
  - ROPC `OAuth2AuthorizationResourceOwnerPasswordAuthenticationProvider` — accept-`@Nullable UserDetailsPasswordService` ctor wired via `HttpSecurityConfigUtils.getOptionalBean(...)` in `OAuth2TokenEndpointConfigurer.createDefaultAuthenticationProviders`. Omitting any of these breaks the rehash silently — `upgradeEncoding=true` won't matter without the password-service handle. ulp-openapi has no form login / ROPC, so it (correctly) declares no DAP. The 2-IT contract for this lives at `AbstractPasswordUpgradeIT` (ulp-support test-jar) with `ConsolePasswordUpgradeIT` / `PortalPasswordUpgradeIT`; copy this template for any future deployable that introduces form login.
- **MFA success handler & enforcement filter wiring**: `MfaAwareAuthenticationSuccessHandler` (in `ulp-support`) is NOT picked up by autoconfig — every form-login SecurityConfiguration must explicitly inject it via `.formLogin(form -> form.successHandler(mfaAwareSuccessHandler))`, same way Argon2id `DaoAuthenticationProvider` is wired. Current wiring: `ConsoleSecurityConfiguration` (admin branch only — `mfa_enabled=true` → challenge, else direct login) and `PortalSecurityConfiguration` (user branch — three forks per `mfa_enabled` × `OrgMfaPolicyService.isUserEnforced`). `OrgMfaEnforcementFilter` is wired into `PortalSecurityConfiguration` ONLY — admins are NEVER subject to organisational MFA enforcement (admin MFA stays voluntary). Don't register the filter on `ConsoleSecurityConfiguration` or `OpenApiSecurityConfiguration`. The cross-deployable contract lives at `AbstractMfaIntegrationTest` (ulp-support test-jar); 15 IT classes (7 console + 8 portal) cover bind / unbind / challenge / lockout / org-enforcement / ROPC-reject / audit / Prometheus.

## MFA 第二因子

TOTP-based MFA is shipped behind a per-subject `mfa_enabled` flag on `ulp_user` / `ulp_administrator` plus an organisation-scope `mfa_enforced` flag on `ulp_organization`. Library bits live in `cn.frank.ulp.support.security.mfa` (cipher, generator, verifier, abstract service, success handler, enforcement filter); per-subject wiring is in `ulp-console` (administrator path) and `ulp-portal` (user path).

- **KEK is mandatory in all 3 deployables**: every `application.yml` reads `ulp.mfa.key-encryption-key: ${ULP_MFA_KEK:}`. Missing / wrong length / non-Base64 → `MfaSecretCipher.validateKek()` fails fast at `@PostConstruct`. Use the SAME KEK across `ulp-console` / `ulp-portal` / `ulp-openapi` — they all read/write the same cipher columns. See README "MFA 部署前置" for generation + K8s Secret + DR backup.
- **Enforcement model is org-level + admin-voluntary**: `OrgMfaPolicyService.isUserEnforced(userId)` (in `ulp-common`) returns true iff the user belongs to ANY organisation with `mfa_enforced=true`. Enforcement does NOT inherit along the org parent chain — it's flat OR over `OrganizationMemberRepository.findOrgIdsByUserId(userId)`. Admins are NEVER subject to this; the admin `MfaService` path skips the policy service entirely.
- **Unbind blocked by org policy**: when an end-user calls `POST /api/v1/mfa/unbind`, the portal controller MUST run `isUserEnforced(userId)` BEFORE any TOTP validation. If true, return `403 {"error":"unbind_blocked_by_org_policy"}` and DO NOT consume a failure counter (else a self-DoS becomes trivial). The admin controller (`ulp-console`) has no such pre-check.
- **ROPC password grant is rejected for MFA-enabled users**: `OAuth2AuthorizationResourceOwnerPasswordAuthenticationProvider` checks `mfa_enabled` after password verification and throws `OAuth2AuthenticationException(invalid_grant, "mfa_required_use_authorization_code_flow")` for any user with MFA on. ROPC clients integrating with MFA-enabled users MUST migrate to Authorization Code Flow — there is no `mfa_otp` parameter for ROPC. Covered by `RopcMfaRejectIT`.
- **Lockout counter is Redis-only**: `MfaChallengeService` increments `ULP_MFA_FAIL:{userType}:{userId}` (TTL 15 min, threshold 5). At 5 failures the next `/mfa/challenge` returns `423 Locked` + `Retry-After`. Counter resets on successful TOTP, on Redis key expiry, or after admin reset. Counter is NEVER persisted to DB — restarting Redis clears it (acceptable trade-off, documented in design.md).
- **Backup codes**: 10 × 8-char `[2-9A-HJ-NP-Z]`, hashed with Argon2id (`PasswordEncoder.encode`) into `backup_codes_json` (JSON array of hashes). Plain codes returned ONLY in the `bind/confirm` response — never retrievable again. Codes are consumed by `POST /api/v1/mfa/challenge { "backupCode": "..." }`; one-shot — a matched hash is removed from the array. Unbind path does NOT accept backup codes (TOTP only).
- **Admin reset is destructive**: `POST /api/v1/admin/users/{id}/reset-mfa` and `POST /api/v1/admin/administrators/{id}/reset-mfa` clear `mfa_enabled` / `totp_secret_cipher` / `backup_codes_json` together. No partial reset, no audit-only path — the user must re-bind from scratch.

## Code style enforcement (runs at compile time, not separate)

The `compile` phase runs three plugins automatically:

- `license-maven-plugin` — applies the Apache header from `tools/codestyle/HEADER` to `.java`, `.ts`, `.tsx`, `.js`. Skip with `-Dlicense.skip=true` when you need a fast compile.
- `formatter-maven-plugin` — Eclipse formatter using `tools/codestyle/Formatter.xml`. Validates and rewrites.
- `impsort-maven-plugin` — import order `java., javax., org., com., cn., lombok.`, removes unused, same-package treated as unused.

If a build fails on license/format/imports, re-run `./mvnw.cmd license:remove license:format` or just compile again — they auto-rewrite.

## Frontend

Each frontend lives **inside** its module under `src/main/<name>-fe/` and is built independently:

- `ulp-console/src/main/console-fe/` — admin console
- `ulp-portal/src/main/portal-fe/` — end-user portal

Both are UmiJS Max + Ant Design Pro + pnpm projects. Common scripts: `pnpm dev` (or `npm run start:dev`), `pnpm build`, `pnpm lint`, `pnpm openapi` (regenerate API client). Husky hooks live under each FE's `.husky/`.

The backend serves the prebuilt FE assets at runtime — `pnpm build` output is wired into the Spring Boot jar via module-level Maven resource config.

## OpenSpec workflow

This repo uses OpenSpec to gate non-trivial changes:

- `openspec/specs/` — currently-true behavior (do not edit directly except via archive)
- `openspec/changes/` — in-flight proposals; archived under `openspec/changes/archive/<date>-<name>/` once shipped
- `.claude/commands/opsx/` — slash commands `propose / explore / apply / archive` drive the flow

If you're changing the runtime stack (framework versions, JVM, Jackson, Security DSL, test infrastructure rules), write an openspec proposal first — the existing `runtime-baseline` and `integration-testing` specs explicitly block silent regressions.

## Deploy artifacts

`deploy/docker/docker-compose.yml` brings up MySQL/Redis/Nginx/ES/RabbitMQ for local infra. The three deployables each have a `Dockerfile` (still on `azul/zulu-openjdk:17-jre` — known stale, migration to JDK 21 base image is pending; if rebuilding images, bump the base). `deploy/helm/` is reserved but empty.

## Notes for future Claude

- `.claude/settings.local.json` is gitignored; project-level non-secret settings go in `.claude/settings.json`.
- The `cn/topiam` package paths are fork residue — keep new code under `cn/frank/ulp/...`.
- When you see surprising `pom.xml` comments like the `API_VERSION` Failsafe block or the Jackson 2/3 dual-import note, they were left intentionally as workaround documentation. Move the comment if you move the code; don't silently delete it.
