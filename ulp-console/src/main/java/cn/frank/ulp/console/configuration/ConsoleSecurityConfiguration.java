/*
 * ulp-console - United Login Platform
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
package cn.frank.ulp.console.configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;

import cn.frank.ulp.audit.event.AuditEventPublish;
import cn.frank.ulp.authentication.common.jackjson.AuthenticationJacksonModule;
import cn.frank.ulp.common.entity.setting.SettingEntity;
import cn.frank.ulp.common.repository.setting.AdministratorRepository;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.console.authentication.*;
import cn.frank.ulp.console.service.security.AdministratorMfaService;
import cn.frank.ulp.support.geo.GeoLocationParser;
import cn.frank.ulp.support.jackjson.SupportJackson2Module;
import cn.frank.ulp.support.security.authentication.WebAuthenticationDetailsSource;
import cn.frank.ulp.support.security.configurer.FormLoginConfigurer;
import cn.frank.ulp.support.security.csrf.SpaCsrfTokenRequestHandler;
import cn.frank.ulp.support.security.mfa.MfaAwareAuthenticationSuccessHandler;
import cn.frank.ulp.support.security.mfa.MfaChallengeService;
import cn.frank.ulp.support.security.mfa.MfaCodeVerifier;
import cn.frank.ulp.support.security.mfa.MfaDecision;
import cn.frank.ulp.support.security.mfa.MfaLockoutService;
import cn.frank.ulp.support.security.mfa.MfaPendingAuthenticationStore;
import cn.frank.ulp.support.security.mfa.MfaSecretCipher;
import cn.frank.ulp.support.security.mfa.MfaService;
import cn.frank.ulp.support.security.mfa.MfaTriggerStrategy;
import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.userdetails.UserType;
import cn.frank.ulp.support.web.useragent.UserAgentParser;

import lombok.RequiredArgsConstructor;

import tools.jackson.databind.ObjectMapper;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.header.writers.XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK;

import static cn.frank.ulp.common.constant.ConfigBeanNameConstants.DEFAULT_SECURITY_FILTER_CHAIN;
import static cn.frank.ulp.common.constant.SessionConstants.CURRENT_STATUS;
import static cn.frank.ulp.common.constant.SynchronizerConstants.EVENT_RECEIVE_PATH;
import static cn.frank.ulp.core.security.PublicSecretEndpoint.PUBLIC_SECRET_PATH;
import static cn.frank.ulp.core.setting.SecuritySettingConstants.*;
import static cn.frank.ulp.support.constant.UlpConstants.*;
import static cn.frank.ulp.support.security.constant.SecurityConstants.LOGOUT_PATH;
import static cn.frank.ulp.support.security.constant.SecurityConstants.RESET_PASSWORD_PATH;

/**
 * ConsoleSecurityConfiguration
 *
 * @author Frank Zhang
 */
@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class ConsoleSecurityConfiguration implements BeanClassLoaderAware {

    /**
     * webSecurityCustomizer
     *
     * @return {@link WebSecurityCustomizer} WebSecurityCustomizer
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
        };
    }

    /**
     * SecurityFilterChain
     *
     * @param httpSecurity {@link  HttpSecurity}
     * @return {@link  SecurityFilterChain}
     * @throws Exception Exception
     */
    @Bean(name = DEFAULT_SECURITY_FILTER_CHAIN)
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   MfaTriggerStrategy mfaTriggerStrategy,
                                                   MfaPendingAuthenticationStore mfaPendingStore) throws Exception {
        // @formatter:off
        // 系统配置
        httpSecurity
                .securityMatcher(API_PATH+"/**")
                //认证请求
                .authorizeHttpRequests(authorizeHttpRequests())
                //安全上下文
                .securityContext(securityContext())
                //x509
                .x509(withDefaults())
                //异常处理
                .exceptionHandling(withExceptionConfigurerDefaults())
                //记住我
                .rememberMe(withRememberMeConfigurerDefaults(settingRepository))
                //CSRF
                .csrf(withCsrfConfigurerDefaults(
                    PathPatternRequestMatcher.pathPattern(HttpMethod.OPTIONS, EVENT_RECEIVE_PATH+"/{code}"),
                    PathPatternRequestMatcher.pathPattern(HttpMethod.GET, EVENT_RECEIVE_PATH+"/{code}")))
                //headers
                .headers(withHeadersConfigurerDefaults(settingRepository))
                //cors
                .cors(withCorsConfigurerDefaults())
                //退出配置
                .logout(withLogoutConfigurerDefaults())
                //会话管理器
                .sessionManagement(withSessionManagementConfigurerDefaults(settingRepository))
                .with(withFormLoginConfigurer(mfaTriggerStrategy, mfaPendingStore),configurer-> {});
        // @formatter:on
        return httpSecurity.build();
    }

    /**
     * Actuator 专用 SecurityFilterChain：与 /api/** 主链路解耦，独立处理 /actuator/**。
     *
     * <p>放行 health / info / prometheus 给运维探针和监控系统拉取；其余端点
     * （env / loggers / metrics / mappings 等）要求 ROLE_ADMIN，未鉴权请求返回 403。
     * 当前 console 没有把 actuator 暴露给 admin 登录的路径，hasRole 在实践中等同 deny；
     * 写成 hasRole 而非 denyAll 是为后续运维平台用 admin token 拉指标保留接入点。
     *
     * <p>{@code @Order(HIGHEST_PRECEDENCE)}：securityMatcher 已经把 actuator 路径与
     * 主 chain 的 /api/** 隔开，理论上顺序无所谓，但显式声明避免未来扩展时踩坑。
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http.securityMatcher("/actuator/**")
            .authorizeHttpRequests(registry -> registry
                .requestMatchers(
                    PathPatternRequestMatcher.pathPattern("/actuator/health"),
                    PathPatternRequestMatcher.pathPattern("/actuator/health/**"),
                    PathPatternRequestMatcher.pathPattern("/actuator/info"),
                    PathPatternRequestMatcher.pathPattern("/actuator/prometheus")
                ).permitAll()
                .anyRequest().hasRole("ADMIN"))
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // @formatter:on
        return http.build();
    }

    /**
     * 认证请求
     *
     * @return {@link AuthorizeHttpRequestsConfigurer}
     */
    public Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeHttpRequests() {
        //@formatter:off
        return registry -> {
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(EVENT_RECEIVE_PATH+"/{code}")).permitAll();
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(CURRENT_STATUS)).permitAll();
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(HttpMethod.GET, PUBLIC_SECRET_PATH)).permitAll();
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(HttpMethod.POST, RESET_PASSWORD_PATH)).permitAll();
            // MFA 第二因子挑战端点：处于"已通过主认证但未提交 TOTP"的中间态。SecurityContext 在
            // MfaAwareAuthenticationSuccessHandler 里已经被清空，此时 anyRequest().authenticated()
            // 会直接 401，所以必须显式放行。安全性靠 cookie 一次性 UUID + 5 分钟 TTL + Redis
            // pending /24 IP 绑定共同兜底，端点自身内部做完整 outcome 校验。
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(HttpMethod.POST, "/api/v1/mfa/challenge")).permitAll();
            registry.anyRequest().authenticated();
        };
        //@formatter:on
    }

    /**
     *  安全上下文
     *
     * @return {@link SecurityContextConfigurer}
     */
    public Customizer<SecurityContextConfigurer<HttpSecurity>> securityContext() {
        return configurer -> {
        };
    }

    /**
     * session 管理器
     *
     * @return {@link SessionManagementConfigurer}
     */
    public Customizer<SessionManagementConfigurer<HttpSecurity>> withSessionManagementConfigurerDefaults(SettingRepository settingRepository) {
        SettingEntity setting = settingRepository.findByName(SECURITY_SESSION_MAXIMUM);
        return configurer -> {
            configurer.sessionFixation().changeSessionId();
            //用户并发
            String defaultSessionMaximum = SECURITY_BASIC_DEFAULT_SETTINGS
                .get(SECURITY_SESSION_MAXIMUM);
            String sessionMaximum = Objects.isNull(setting) ? defaultSessionMaximum
                : "0".equals(setting.getValue()) ? defaultSessionMaximum : setting.getValue();
            configurer.maximumSessions(Integer.parseInt(sessionMaximum))
                .expiredSessionStrategy(new ConsoleSessionInformationExpiredStrategy());
        };
    }

    /**
     * session 退出过滤器
     *
     * @return {@link LogoutConfigurer}
     */
    public Customizer<LogoutConfigurer<HttpSecurity>> withLogoutConfigurerDefaults() {
        return configurer -> {
            configurer.logoutUrl(LOGOUT_PATH)
                .logoutSuccessHandler(new ConsoleLogoutSuccessHandler()).permitAll();
        };
    }

    /**
     * headers 过滤器
     *
     * @param settingRepository {@link SettingRepository}
     * @return {@link HeadersConfigurer}
     */
    public Customizer<HeadersConfigurer<HttpSecurity>> withHeadersConfigurerDefaults(SettingRepository settingRepository) {
        List<SettingEntity> list = settingRepository.findByNameIn(SECURITY_DEFENSE_POLICY_KEY);
        // 转MAP
        Map<String, String> map = list.stream().collect(Collectors.toMap(SettingEntity::getName,
            SettingEntity::getValue, (key1, key2) -> key2));
        //内容安全策略
        String contentSecurityPolicy = map
            .containsKey(SECURITY_DEFENSE_POLICY_CONTENT_SECURITY_POLICY)
                ? map.get(SECURITY_DEFENSE_POLICY_CONTENT_SECURITY_POLICY).replace("\n", "")
                    .replace("\r\n", "")
                : SECURITY_DEFENSE_POLICY_DEFAULT_SETTINGS
                    .get(SECURITY_DEFENSE_POLICY_CONTENT_SECURITY_POLICY);

        //@formatter:off
        return configurer -> {
            configurer.xssProtection(xssProtection -> xssProtection.headerValue(ENABLED_MODE_BLOCK))
            .contentSecurityPolicy(config-> config.policyDirectives(contentSecurityPolicy))
            .referrerPolicy(referrerPolicyConfig -> referrerPolicyConfig.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
            .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
            .contentTypeOptions(contentTypeOptionsConfig-> {})
            .permissionsPolicy(permissionsPolicyConfig -> permissionsPolicyConfig.policy("camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()"));
        };
        //@formatter:on
    }

    /**
     * CORS filter.
     * <p>
     * Reads cross-origin allow-list from {@code ulp.security.cors.allowed-origins}.
     * If the list is empty (the default for same-origin deployments where the
     * bundled SPA is served from the same host as the API), CORS is disabled —
     * same-origin requests bypass CORS checks entirely.
     * <p>
     * When non-empty, only the explicitly listed origins are allowed; wildcards
     * are rejected. Credentials are permitted because the SPA relies on cookies
     * for the session and CSRF token. Wildcard origins together with credentials
     * would let any website on the internet read authenticated responses, so
     * that combination is forbidden here.
     *
     * @return CORS configurer customizer
     */
    public Customizer<CorsConfigurer<HttpSecurity>> withCorsConfigurerDefaults() {
        List<String> origins = sanitizedAllowedOrigins();
        if (origins.isEmpty()) {
            return CorsConfigurer::disable;
        }
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(origins);
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Lists.newArrayList("Content-Type", "Accept", "Origin",
            DEFAULT_CSRF_HEADER_NAME, "X-Requested-With"));
        configuration.setAllowedMethods(Lists.newArrayList(HttpMethod.GET.name(),
            HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name()));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return configurer -> configurer.configurationSource(source);
    }

    private List<String> sanitizedAllowedOrigins() {
        if (corsAllowedOrigins == null || corsAllowedOrigins.length == 0) {
            return List.of();
        }
        return Arrays.stream(corsAllowedOrigins)
            .filter(s -> s != null && !s.isBlank() && !"*".equals(s.trim())).map(String::trim)
            .toList();
    }

    /**
     * 异常处理器
     *
     * @return {@link ExceptionHandlingConfigurer}
     */
    public Customizer<ExceptionHandlingConfigurer<HttpSecurity>> withExceptionConfigurerDefaults() {
        return configurer -> {
            configurer
                .authenticationEntryPoint(new ConsoleAuthenticationEntryPoint(userAgentParser));
            configurer.accessDeniedHandler(new ConsoleAccessDeniedHandler());
            configurer
                .withObjectPostProcessor(new ObjectPostProcessor<ExceptionTranslationFilter>() {
                    @Override
                    public <O extends ExceptionTranslationFilter> O postProcess(O filter) {
                        filter
                            .setAuthenticationTrustResolver(new AuthenticationTrustResolverImpl());
                        return filter;
                    }
                });
        };
    }

    /**
     * withRememberMeConfigurerDefaults
     *
     * @return {@link RememberMeConfigurer}
     */
    public Customizer<RememberMeConfigurer<HttpSecurity>> withRememberMeConfigurerDefaults(SettingRepository settingRepository) {
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(false);
        SettingEntity setting = settingRepository.findByName(SECURITY_BASIC_REMEMBER_ME_VALID_TIME);
        String rememberMeValiditySeconds = Objects.isNull(setting)
            ? SECURITY_BASIC_DEFAULT_SETTINGS.get(SECURITY_BASIC_REMEMBER_ME_VALID_TIME)
            : setting.getValue();
        rememberMeServices.setValiditySeconds(Integer.parseInt(rememberMeValiditySeconds));
        return configurer -> configurer.rememberMeServices(rememberMeServices);
    }

    /**
     * csrf
     *
     * @return {@link CsrfConfigurer}
     */
    public Customizer<CsrfConfigurer<HttpSecurity>> withCsrfConfigurerDefaults(RequestMatcher... ignoringRequestMatchers) {
        return csrf -> {
            CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
            repository.setCookieName(DEFAULT_CSRF_COOKIE_NAME);
            repository.setHeaderName(DEFAULT_CSRF_HEADER_NAME);
            csrf.csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler());
            csrf.ignoringRequestMatchers(ignoringRequestMatchers).csrfTokenRepository(repository);
        };
    }

    /**
     * 表单登录。
     *
     * <p>登录成功后由 {@link MfaAwareAuthenticationSuccessHandler} 在 {@link ConsoleAuthenticationSuccessHandler}
     * 之前判定是否需要二次因子：未绑定 MFA 直接走原 handler 提交 session；已绑定则把
     * {@link org.springframework.security.core.Authentication} 暂存到 Redis 并下发
     * {@code mfa_required} + cookie，等待 {@code POST /api/v1/mfa/challenge} 完成 OTP 验证。
     *
     * <p>console 侧策略由 {@link #consoleMfaTriggerStrategy(AdministratorMfaService)} 提供，
     * 永远不会发出 {@link MfaDecision#SETUP_REQUIRED}（管理员 MFA 是自愿、非强制）。
     *
     * @return {@link FormLoginConfigurer}
     */
    public FormLoginConfigurer<HttpSecurity> withFormLoginConfigurer(MfaTriggerStrategy mfaTriggerStrategy,
                                                                     MfaPendingAuthenticationStore mfaPendingStore) {
        // @formatter:off
        AuthenticationSuccessHandler delegateSuccess = new ConsoleAuthenticationSuccessHandler(administratorRepository,  auditEventPublish );
        AuthenticationSuccessHandler mfaAware = new MfaAwareAuthenticationSuccessHandler(delegateSuccess, mfaTriggerStrategy, mfaPendingStore);
        FormLoginConfigurer<HttpSecurity> configurer=new FormLoginConfigurer<>();
        configurer.successHandler(mfaAware);
        configurer.failureHandler(new ConsoleAuthenticationFailureHandler());
        return configurer;
        // @formatter:on
    }

    /**
     * 显式声明 {@link DaoAuthenticationProvider} 并注入 {@link UserDetailsPasswordService}，
     * 以支撑 security-baseline spec 中"老 {bcrypt} 密文登录成功后自动 rehash 到 {argon2}"的要求。
     *
     * <p>Spring Security 7 的 {@code InitializeUserDetailsManagerConfigurer} 只会自动装配
     * {@code UserDetailsService} + {@code PasswordEncoder} 给 auto-DAP，<b>不会</b>注入
     * {@code UserDetailsPasswordService}。这意味着即使切到 Argon2id 作为默认 encoder，
     * {@code DelegatingPasswordEncoder.upgradeEncoding(...)} 返回 true 时也只会是 silent no-op。
     *
     * <p>显式声明一个 {@code AuthenticationProvider} bean 会让 Spring Security 跳过 auto-DAP，
     * 优先用本 bean 装配进 {@code AuthenticationManager}。
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService,
                                                               PasswordEncoder passwordEncoder,
                                                               UserDetailsPasswordService userDetailsPasswordService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsPasswordService(userDetailsPasswordService);
        return provider;
    }

    /**
     * 身份验证成功事件监听器
     *
     * @return {@link  ConsoleAuthenticationSuccessEventListener}
     */
    @Bean
    @ConditionalOnMissingBean
    public ConsoleAuthenticationSuccessEventListener authenticationSuccessEventListener() {
        return new ConsoleAuthenticationSuccessEventListener();
    }

    /**
     * 身份验证失败事件监听器
     *
     * @return {@link  ConsoleAuthenticationFailureEventListener}
     */
    @Bean
    @ConditionalOnMissingBean
    public ConsoleAuthenticationFailureEventListener authenticationFailureEventListener() {
        return new ConsoleAuthenticationFailureEventListener();
    }

    /**
     * 退出成功事件监听器
     *
     * @return {@link  ConsoleLogoutSuccessEventListener}
     */
    @Bean
    @ConditionalOnMissingBean
    public ConsoleLogoutSuccessEventListener logoutSuccessEventListener() {
        return new ConsoleLogoutSuccessEventListener();
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        ObjectMapper mapper = SupportJackson2Module.objectMapperBuilder(this.loader)
            .addModule(new AuthenticationJacksonModule())
            .changeDefaultPropertyInclusion(v -> v.withValueInclusion(JsonInclude.Include.NON_NULL))
            .build();
        return new GenericJacksonJsonRedisSerializer(mapper);
    }

    /**
     * Redis-backed pending-challenge store。复用 {@link #springSessionDefaultRedisSerializer()}
     * 的 Jackson 3 + {@code AuthenticationJacksonModule} 序列化器，让 pending
     * {@link org.springframework.security.core.Authentication} 的 round-trip 与 Spring Session
     * 写入 live session 的 schema 完全一致——一次配置覆盖两条路径，避免 deserializer drift。
     */
    @Bean
    public MfaPendingAuthenticationStore mfaPendingAuthenticationStore(RedisConnectionFactory connectionFactory,
                                                                       RedisSerializer<Object> springSessionDefaultRedisSerializer) {
        return new MfaPendingAuthenticationStore(connectionFactory,
            springSessionDefaultRedisSerializer);
    }

    /**
     * Brute-force throttle for MFA challenge：默认 5 次失败 / 15 分钟窗口，counter 写在
     * {@code ULP_MFA_FAIL:admin:{adminId}}。窗口 / 阈值在生产需要灰度时可改成
     * {@code @ConfigurationProperties} 注入；当前默认值已对齐 spec.md。
     */
    @Bean
    public MfaLockoutService mfaLockoutService(StringRedisTemplate redisTemplate) {
        return new MfaLockoutService(redisTemplate);
    }

    /**
     * 第二因子 verify-and-commit 引擎。{@link Collection} 注入会自动包含所有
     * {@link MfaService} bean —— console 当前只有 {@link AdministratorMfaService}，未来若新增
     * 其他 subject 类型，新 bean 注册即可，本配置无需改动。
     */
    @Bean
    public MfaChallengeService mfaChallengeService(MfaPendingAuthenticationStore mfaPendingStore,
                                                   MfaLockoutService mfaLockoutService,
                                                   MfaCodeVerifier codeVerifier,
                                                   MfaSecretCipher secretCipher,
                                                   Collection<MfaService> mfaServices) {
        return new MfaChallengeService(mfaPendingStore, mfaLockoutService, codeVerifier,
            secretCipher, mfaServices);
    }

    /**
     * Console 侧 MFA 触发策略：principal 为管理员且已绑定（{@code totp_secret_cipher != null}）
     * 时返回 {@link MfaDecision#CHALLENGE_REQUIRED}，否则 {@link MfaDecision#DIRECT_LOGIN}。
     *
     * <p>实现注意：
     * <ul>
     *   <li>非 {@link UserDetails} principal / userType 不是 admin / 缺 id —— 一律
     *       {@link MfaDecision#DIRECT_LOGIN} 兜底放行。MFA 是叠加层，不应把异常 principal 升级
     *       为 500；如果有其他主体类型走到 console form login（理论上不会），让原链路自然拒绝。
     *   <li>判定依据用 {@link AdministratorMfaService#loadActiveCipher(String)} 的 nullability，
     *       而不是 {@code mfa_enabled} 标志位。{@code mfa_enabled=true} 但 cipher 缺失（admin reset
     *       后的边界态）下走 challenge 会让用户卡死，不如直接放行让其重新绑定。
     *   <li><b>永远不返回 {@link MfaDecision#SETUP_REQUIRED}</b>—— 管理员 MFA 自愿，org-level
     *       enforcement 仅对 portal 用户生效（Phase 4）。
     * </ul>
     */
    @Bean
    public MfaTriggerStrategy consoleMfaTriggerStrategy(AdministratorMfaService administratorMfaService) {
        return authentication -> {
            if (authentication == null
                || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
                return MfaDecision.DIRECT_LOGIN;
            }
            UserType userType = userDetails.getUserType();
            if (userType == null || !UserType.ADMIN.getType().equals(userType.getType())) {
                return MfaDecision.DIRECT_LOGIN;
            }
            String userId = userDetails.getId();
            if (userId == null) {
                return MfaDecision.DIRECT_LOGIN;
            }
            return administratorMfaService.loadActiveCipher(userId) != null
                ? MfaDecision.CHALLENGE_REQUIRED
                : MfaDecision.DIRECT_LOGIN;
        };
    }

    /**
     * WebAuthenticationDetailsSource
     *
     * @param geoLocationParser {@link GeoLocationParser}
     * @return {@link WebAuthenticationDetailsSource}
     */
    @Bean
    public WebAuthenticationDetailsSource authenticationDetailsSource(GeoLocationParser geoLocationParser,
                                                                      UserAgentParser userAgentParser) {
        return new WebAuthenticationDetailsSource(geoLocationParser, userAgentParser);
    }

    private ClassLoader loader;

    @Override
    public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
        this.loader = classLoader;
    }

    /**
     * AdministratorRepository
     */
    private final AdministratorRepository administratorRepository;

    /**
     * SettingRepository
     */
    private final SettingRepository       settingRepository;

    /**
     * AuditEventPublish
     */
    private final AuditEventPublish       auditEventPublish;

    /**
     * UserAgentParser
     */
    private final UserAgentParser         userAgentParser;

    /**
     * CORS allow-list. Empty (default) disables CORS — appropriate for
     * same-origin deployments where the SPA is bundled with the API.
     */
    @Value("${ulp.security.cors.allowed-origins:}")
    private String[]                      corsAllowedOrigins;

}
