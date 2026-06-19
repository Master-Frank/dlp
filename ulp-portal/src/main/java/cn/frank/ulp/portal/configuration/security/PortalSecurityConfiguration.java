/*
 * ulp-portal - United Login Platform
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
package cn.frank.ulp.portal.configuration.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import cn.frank.ulp.audit.event.AuditEventPublish;
import cn.frank.ulp.authentication.alipay.configurer.AlipayAuthenticationConfigurer;
import cn.frank.ulp.authentication.common.IdentityProviderAuthenticationService;
import cn.frank.ulp.authentication.common.client.RegisteredIdentityProviderClientRepository;
import cn.frank.ulp.authentication.common.configurer.IdentityProviderBindAuthenticationConfigurer;
import cn.frank.ulp.authentication.dingtalk.configurer.DingTalkAuthenticationConfigurer;
import cn.frank.ulp.authentication.feishu.configurer.FeiShuAuthenticationConfigurer;
import cn.frank.ulp.authentication.gitee.configurer.GiteeAuthenticationConfigurer;
import cn.frank.ulp.authentication.github.configurer.GithubAuthenticationConfigurer;
import cn.frank.ulp.authentication.otp.mail.configurer.MailOtpAuthenticationConfigurer;
import cn.frank.ulp.authentication.otp.sms.configurer.SmsOtpAuthenticationConfigurer;
import cn.frank.ulp.authentication.qq.configurer.QqAuthenticationConfigurer;
import cn.frank.ulp.authentication.wechat.configurer.WeChatAuthenticationConfigurer;
import cn.frank.ulp.authentication.wechatwork.configurer.WeChatWorkAuthenticationConfigurer;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.common.repository.setting.SettingRepository;
import cn.frank.ulp.common.security.mfa.OrgMfaPolicyService;
import cn.frank.ulp.core.message.mail.MailMsgEventPublish;
import cn.frank.ulp.core.message.sms.SmsMsgEventPublish;
import cn.frank.ulp.core.security.otp.OtpContextHelp;
import cn.frank.ulp.core.security.password.task.PasswordExpireTask;
import cn.frank.ulp.core.security.password.task.impl.PasswordExpireLockTask;
import cn.frank.ulp.core.security.password.task.impl.PasswordExpireWarnTask;
import cn.frank.ulp.core.security.task.UserExpireLockTask;
import cn.frank.ulp.core.security.task.UserUnlockTask;
import cn.frank.ulp.portal.authentication.*;
import cn.frank.ulp.portal.security.mfa.OrgMfaEnforcementFilter;
import cn.frank.ulp.portal.service.security.UserMfaService;
import cn.frank.ulp.support.geo.GeoLocationParser;
import cn.frank.ulp.support.security.authentication.WebAuthenticationDetailsSource;
import cn.frank.ulp.support.security.configurer.FormLoginConfigurer;
import cn.frank.ulp.support.security.mfa.MfaAwareAuthenticationSuccessHandler;
import cn.frank.ulp.support.security.mfa.MfaBackupCodeService;
import cn.frank.ulp.support.security.mfa.MfaBackupCodeStore;
import cn.frank.ulp.support.security.mfa.MfaChallengeService;
import cn.frank.ulp.support.security.mfa.MfaCodeVerifier;
import cn.frank.ulp.support.security.mfa.MfaDecision;
import cn.frank.ulp.support.security.mfa.MfaLockoutService;
import cn.frank.ulp.support.security.mfa.MfaMetrics;
import cn.frank.ulp.support.security.mfa.MfaPendingAuthenticationStore;
import cn.frank.ulp.support.security.mfa.MfaSecretCipher;
import cn.frank.ulp.support.security.mfa.MfaService;
import cn.frank.ulp.support.security.mfa.MfaTriggerStrategy;
import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.userdetails.UserType;
import cn.frank.ulp.support.web.useragent.UserAgentParser;

import io.micrometer.core.instrument.MeterRegistry;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;

import static cn.frank.ulp.authentication.alipay.configurer.AlipayAuthenticationConfigurer.alipayOauth;
import static cn.frank.ulp.authentication.dingtalk.configurer.DingTalkAuthenticationConfigurer.dingTalkOAuth2;
import static cn.frank.ulp.authentication.feishu.configurer.FeiShuAuthenticationConfigurer.feiShuOAuth2;
import static cn.frank.ulp.authentication.gitee.configurer.GiteeAuthenticationConfigurer.giteeOauth;
import static cn.frank.ulp.authentication.github.configurer.GithubAuthenticationConfigurer.githubOAuth2;
import static cn.frank.ulp.authentication.otp.mail.configurer.MailOtpAuthenticationConfigurer.mailOtp;
import static cn.frank.ulp.authentication.otp.sms.configurer.SmsOtpAuthenticationConfigurer.smsOtp;
import static cn.frank.ulp.authentication.qq.configurer.QqAuthenticationConfigurer.qqOAuth2;
import static cn.frank.ulp.authentication.wechat.configurer.WeChatAuthenticationConfigurer.weChatOauth;
import static cn.frank.ulp.authentication.wechatwork.configurer.WeChatWorkAuthenticationConfigurer.weChatWorkOAuth2;
import static cn.frank.ulp.common.constant.AuthnConstants.LOGIN_CONFIG;
import static cn.frank.ulp.common.constant.ConfigBeanNameConstants.*;
import static cn.frank.ulp.common.constant.SessionConstants.CURRENT_STATUS;
import static cn.frank.ulp.core.security.PublicSecretEndpoint.PUBLIC_SECRET_PATH;
import static cn.frank.ulp.portal.constant.PortalConstants.*;
import static cn.frank.ulp.protocol.code.configurer.AuthenticationUtils.getAuthenticationDetailsSource;
import static cn.frank.ulp.support.constant.UlpConstants.API_PATH;

/**
 * PortalSecurityConfiguration
 *
 * @author Frank Zhang
 */
@EnableMethodSecurity
@Configuration(proxyBeanMethods = false)
public class PortalSecurityConfiguration extends AbstractSecurityConfiguration
                                         implements BeanClassLoaderAware {

    private final AuthenticationFailureHandler failureHandler = new PortalAuthenticationFailureHandler();

    /**
     * webSecurityCustomizer
     *
     * @return {@link WebSecurityCustomizer} WebSecurityCustomizer
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
            PathPatternRequestMatcher.pathPattern(HttpMethod.GET, "/css/**"),
            PathPatternRequestMatcher.pathPattern(HttpMethod.GET, "/js/**"),
            PathPatternRequestMatcher.pathPattern(HttpMethod.GET, "/webjars/**"),
            PathPatternRequestMatcher.pathPattern(HttpMethod.GET, "/images/**"),
            PathPatternRequestMatcher.pathPattern(HttpMethod.GET, "/favicon.ico"));
    }

    /**
     * Actuator 专用 SecurityFilterChain：与 IDP / OIDC / FORM / JWT / 默认 /api/** chain 全部解耦。
     *
     * <p>放行 health / info / prometheus；其余 actuator 端点 {@code denyAll()} ——
     * portal 是终端用户面向的，没有任何"admin"语义，actuator 全部敏感端点都不该被任何 portal 用户访问。
     * 需要运维拉指标的场景在 console 走 hasRole("ADMIN")，或者后续走独立 management 端口隔离。
     *
     * <p>{@code @Order(HIGHEST_PRECEDENCE)}：portal 有 5 条 chain（IDP / OIDC / FORM / JWT / 默认），
     * 显式声明 actuator chain 最高优先级，避免 IDP 那种 OrRequestMatcher 误捕 /actuator/* 路径。
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
                .anyRequest().denyAll())
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // @formatter:on
        return http.build();
    }

    /**
     * IDP SecurityFilterChain
     *
     * @param httpSecurity {@link  HttpSecurity}
     * @return {@link  SecurityFilterChain}
     * @throws Exception Exception
     */
    @Bean(name = IDP_SECURITY_FILTER_CHAIN)
    public SecurityFilterChain idpAuthenticationSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // @formatter:off
        WebAuthenticationDetailsSource authenticationDetailsSource = getAuthenticationDetailsSource(httpSecurity);
        AuthenticationSuccessHandler successHandler = new PortalAuthenticationSuccessHandler(userRepository,  auditEventPublish );
        List<RequestMatcher> requestMatchers = new ArrayList<>();

        //QQ
        QqAuthenticationConfigurer qq = qqOAuth2(registeredIdentityProviderClientRepository ,identityProviderAuthenticationService)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        requestMatchers.add(qq.getRequestMatcher());
        httpSecurity.with(qq,configurer-> {});

        //微信扫码
        WeChatAuthenticationConfigurer chatScanCode = weChatOauth(registeredIdentityProviderClientRepository ,identityProviderAuthenticationService)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        requestMatchers.add(chatScanCode.getRequestMatcher());
        httpSecurity.with(chatScanCode,configurer-> {});

        //GITHUB
        GithubAuthenticationConfigurer github = githubOAuth2(registeredIdentityProviderClientRepository ,identityProviderAuthenticationService)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        requestMatchers.add(github.getRequestMatcher());
        httpSecurity.with(github,configurer-> {});

        //企业微信
        WeChatWorkAuthenticationConfigurer weChatWork = weChatWorkOAuth2(registeredIdentityProviderClientRepository ,identityProviderAuthenticationService)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        requestMatchers.add(weChatWork.getRequestMatcher());
        httpSecurity.with(weChatWork,configurer-> {});

        //钉钉OAuth2
        DingTalkAuthenticationConfigurer dingtalkOauth2 = dingTalkOAuth2(registeredIdentityProviderClientRepository ,identityProviderAuthenticationService)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        requestMatchers.add(dingtalkOauth2.getRequestMatcher());
        httpSecurity.with(dingtalkOauth2,configurer-> {});

        //飞书
        FeiShuAuthenticationConfigurer feiShuScanCode = feiShuOAuth2(registeredIdentityProviderClientRepository ,identityProviderAuthenticationService)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        requestMatchers.add(feiShuScanCode.getRequestMatcher());
        httpSecurity.with(feiShuScanCode,configurer-> {});


        //Gitee
        GiteeAuthenticationConfigurer giteeCode = giteeOauth(registeredIdentityProviderClientRepository ,identityProviderAuthenticationService)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        requestMatchers.add(giteeCode.getRequestMatcher());
        httpSecurity.with(giteeCode,configurer-> {});

        //支付宝
        AlipayAuthenticationConfigurer alipayOauth = alipayOauth(registeredIdentityProviderClientRepository ,identityProviderAuthenticationService)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        requestMatchers.add(alipayOauth.getRequestMatcher());
        httpSecurity.with(alipayOauth,configurer-> {});

        //RequestMatcher
        OrRequestMatcher requestMatcher = new OrRequestMatcher(requestMatchers);
        //社交授权请求重定向匹配器
        httpSecurity
            .securityMatcher(requestMatcher)
            .authorizeHttpRequests(registry -> registry.anyRequest().authenticated())
            //安全上下文
            .securityContext(securityContext())
            //异常处理器
            .exceptionHandling(withExceptionConfigurerDefaults())
            //CSRF
            .csrf(withCsrfConfigurerDefaults(requestMatcher))
            //headers
            .headers(withHeadersConfigurerDefaults())
            //cors
            .cors(withCorsConfigurerDefaults())
            //会话管理器
            .sessionManagement(withSessionManagementConfigurerDefaults());
        return httpSecurity.build();
        // @formatter:on
    }

    /**
     * SecurityFilterChain
     *
     * @param httpSecurity {@link  HttpSecurity}
     * @return {@link  SecurityFilterChain}
     * @throws Exception Exception
     */
    @Bean(name = DEFAULT_SECURITY_FILTER_CHAIN)
    @DependsOn({ IDP_SECURITY_FILTER_CHAIN, OIDC_PROTOCOL_SECURITY_FILTER_CHAIN,
                 FORM_PROTOCOL_SECURITY_FILTER_CHAIN, JWT_PROTOCOL_SECURITY_FILTER_CHAIN })
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   MfaTriggerStrategy mfaTriggerStrategy,
                                                   MfaPendingAuthenticationStore mfaPendingStore,
                                                   UserMfaService userMfaService,
                                                   OrgMfaPolicyService orgMfaPolicyService) throws Exception {
        // @formatter:off
        WebAuthenticationDetailsSource authenticationDetailsSource = getAuthenticationDetailsSource(httpSecurity);
        AuthenticationSuccessHandler successHandler = new PortalAuthenticationSuccessHandler(userRepository,auditEventPublish);
        // 系统配置
        httpSecurity
                .securityMatcher(API_PATH+"/**")
                //认证请求
                .authorizeHttpRequests(withHttpAuthorizeRequests())
                //安全上下文
                .securityContext(securityContext())
                //请求缓存
                .requestCache(withRequestCacheConfigurer())
                //x509
                .x509(withDefaults())
                //异常处理
                .exceptionHandling(withExceptionConfigurerDefaults())
                //记住我
                .rememberMe(withRememberMeConfigurerDefaults())
                //CSRF
                .csrf(withCsrfConfigurerDefaults())
                //headers
                .headers(withHeadersConfigurerDefaults())
                //cors
                .cors(withCorsConfigurerDefaults())
                //退出配置
                .logout(withLogoutConfigurerDefaults())
                //会话管理器
                .sessionManagement(withSessionManagementConfigurerDefaults())
                .addFilterBefore(new OrgMfaEnforcementFilter(userMfaService, orgMfaPolicyService),
                    AuthorizationFilter.class)
                .with(withFormLoginConfigurer(mfaTriggerStrategy, mfaPendingStore),configurer-> {});
        //邮件验证码登录认证
        MailOtpAuthenticationConfigurer mailOtpAuthenticationConfigurer = mailOtp(userRepository, userDetailsService, otpContextHelp)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        httpSecurity.with(mailOtpAuthenticationConfigurer,configurer-> {});
        //短信验证码登录认证
        SmsOtpAuthenticationConfigurer smsAuthenticationConfigurer = smsOtp(userRepository, userDetailsService, otpContextHelp)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        httpSecurity.with(smsAuthenticationConfigurer,configurer-> {});
        //IDP 绑定用户
        IdentityProviderBindAuthenticationConfigurer identityProviderBindAuthenticationConfigurer = IdentityProviderBindAuthenticationConfigurer.idpBind(identityProviderAuthenticationService, passwordEncoder)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .authenticationDetailsSource(authenticationDetailsSource);
        httpSecurity.with(identityProviderBindAuthenticationConfigurer,configurer-> {});
        // @formatter:on
        return httpSecurity.build();
    }

    /**
     * 使用 Http 授权请求
     *
     * @return {@link AuthorizeHttpRequestsConfigurer}
     */
    public Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> withHttpAuthorizeRequests() {
        //@formatter:off
        return registry -> {
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(HttpMethod.GET, LOGIN_CONFIG)).permitAll();
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(HttpMethod.GET, PUBLIC_SECRET_PATH)).permitAll();
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(HttpMethod.GET, CURRENT_STATUS)).permitAll();
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(HttpMethod.POST, ACCOUNT_PATH + PREPARE_FORGET_PASSWORD)).permitAll();
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(HttpMethod.GET, ACCOUNT_PATH + FORGET_PASSWORD_CODE)).permitAll();
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(HttpMethod.PUT, ACCOUNT_PATH + FORGET_PASSWORD)).permitAll();
            // MFA 第二因子端点处于"已主认证、未二次"中间态：MfaAwareAuthenticationSuccessHandler
            // 已经把 SecurityContext 从 session 清空，anyRequest().authenticated() 会拒绝。必须显式放行；
            // 安全性由 cookie 一次性 UUID + 5min TTL + Redis pending /24 IP 绑定共同兜底。
            registry.requestMatchers(PathPatternRequestMatcher.pathPattern(HttpMethod.POST, "/api/v1/mfa/challenge")).permitAll();
            registry.anyRequest().authenticated();
        };
    }



    /**
     * 身份验证成功事件监听器
     *
     * @return {@link  PortalAuthenticationSuccessEventListener}
     */
    @Bean
    @ConditionalOnMissingBean
    public PortalAuthenticationSuccessEventListener authenticationSuccessEventListener() {
        return new PortalAuthenticationSuccessEventListener();
    }

    /**
     * 身份验证失败事件监听器
     *
     * @return {@link  PortalAuthenticationFailureEventListener}
     */
    @Bean
    @ConditionalOnMissingBean
    public PortalAuthenticationFailureEventListener authenticationFailureEventListener() {
        return new PortalAuthenticationFailureEventListener();
    }

    /**
     * 退出成功事件监听器
     *
     * @return {@link  PortalLogoutSuccessEventListener}
     */
    @Bean
    @ConditionalOnMissingBean
    public PortalLogoutSuccessEventListener logoutSuccessEventListener() {
        return new PortalLogoutSuccessEventListener();
    }

    /**
     * 表单登录。
     *
     * <p>登录成功后由 {@link MfaAwareAuthenticationSuccessHandler} 在
     * {@link PortalAuthenticationSuccessHandler} 之前做 MFA 三分支判定：未绑且组织未强制
     * 直接走原 handler 提交 session；已绑则把 Authentication 暂存到 Redis 并下发
     * {@code mfa_required} + cookie；未绑且组织强制则提交 session 后下发
     * {@code mfa_setup_required}，等前端跳 {@code /mfa/setup}。
     *
     * <p>策略 bean 由 {@link #portalMfaTriggerStrategy(UserMfaService, OrgMfaPolicyService)}
     * 提供——只对 {@code userType=user} 的 principal 生效，admin / 其他类型一律直放。
     */
    public FormLoginConfigurer<HttpSecurity> withFormLoginConfigurer(MfaTriggerStrategy mfaTriggerStrategy,
                                                                     MfaPendingAuthenticationStore mfaPendingStore) {
        // @formatter:off
        AuthenticationSuccessHandler delegateSuccess = new PortalAuthenticationSuccessHandler(userRepository,  auditEventPublish );
        AuthenticationSuccessHandler mfaAware = new MfaAwareAuthenticationSuccessHandler(delegateSuccess, mfaTriggerStrategy, mfaPendingStore);
        FormLoginConfigurer<HttpSecurity> configurer=new FormLoginConfigurer<>();
        configurer.successHandler(mfaAware)
                .failureHandler(new PortalAuthenticationFailureHandler());
        return configurer;
        // @formatter:on
    }

    /**
     * withRequestCacheConfigurer
     *
     * @return {@link RequestCacheConfigurer}
     */
    public static Customizer<RequestCacheConfigurer<HttpSecurity>> withRequestCacheConfigurer() {
        return configurer -> {
        };
    }

    /**
     * 密码过期锁定任务
     *
     * @param settingRepository {@link  SettingRepository}
     * @param userRepository    {@link  UserRepository}
     * @return {@link  PasswordExpireTask}
     */
    @Bean
    public PasswordExpireTask passwordExpireLockTask(SettingRepository settingRepository,
                                                     UserRepository userRepository) {
        return new PasswordExpireLockTask(settingRepository, userRepository);
    }

    /**
     * 密码过期警告任务
     *
     * @param settingRepository   {@link  SettingRepository}
     * @param userRepository      {@link  UserRepository}
     * @param mailMsgEventPublish {@link  MailMsgEventPublish}
     * @param smsMsgEventPublish {@link  SmsMsgEventPublish}
     * @return {@link  PasswordExpireTask}
     */
    @Bean
    public PasswordExpireTask passwordExpireWarnTask(SettingRepository settingRepository,
                                                     UserRepository userRepository,
                                                     MailMsgEventPublish mailMsgEventPublish,
                                                     SmsMsgEventPublish smsMsgEventPublish) {
        return new PasswordExpireWarnTask(settingRepository, userRepository, mailMsgEventPublish,
            smsMsgEventPublish);
    }

    /**
     * 密码过期锁定任务
     *
     * @param userRepository    {@link  UserRepository}
     * @return {@link  PasswordExpireTask}
     */
    @Bean
    public UserUnlockTask userUnlockTask(UserRepository userRepository) {
        return new UserUnlockTask(userRepository);
    }

    /**
     * 用户过期锁定任务
     *
     * @param userRepository    {@link  UserRepository}
     * @return {@link  PasswordExpireTask}
     */
    @Bean
    public UserExpireLockTask userExpireLockTask(UserRepository userRepository) {
        return new UserExpireLockTask(userRepository);
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

    /**
     * UserRepository
     */
    private final UserRepository                             userRepository;

    /**
     * UserDetailsService
     */
    private final UserDetailsService                         userDetailsService;

    /**
     * OtpContextHelp
     */
    private final OtpContextHelp                             otpContextHelp;

    /**
     * PasswordEncoder
     */
    private final PasswordEncoder                            passwordEncoder;

    /**
     * UserDetailsPasswordService —— 用于 bcrypt → argon2 自动 rehash 写回
     */
    private final UserDetailsPasswordService                 userDetailsPasswordService;

    /**
     * AuditEventPublish
     */
    private final AuditEventPublish                          auditEventPublish;

    private final RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository;
    private final IdentityProviderAuthenticationService      identityProviderAuthenticationService;

    private ClassLoader                                      loader;

    @Override
    public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
        this.loader = classLoader;
    }

    public PortalSecurityConfiguration(UserAgentParser userAgentParser,
                                       UserRepository userRepository,
                                       UserDetailsService userDetailsService,
                                       OtpContextHelp otpContextHelp,
                                       PasswordEncoder passwordEncoder,
                                       UserDetailsPasswordService userDetailsPasswordService,
                                       AuditEventPublish auditEventPublish,
                                       SettingRepository settingRepository,
                                       RegisteredIdentityProviderClientRepository registeredIdentityProviderClientRepository,
                                       IdentityProviderAuthenticationService identityProviderAuthenticationService) {
        super(userAgentParser, settingRepository);
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.otpContextHelp = otpContextHelp;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsPasswordService = userDetailsPasswordService;
        this.auditEventPublish = auditEventPublish;
        this.registeredIdentityProviderClientRepository = registeredIdentityProviderClientRepository;
        this.identityProviderAuthenticationService = identityProviderAuthenticationService;
    }

    /**
     * 显式装配 DaoAuthenticationProvider，并挂载 UserDetailsPasswordService —— 这是 bcrypt
     * 密文登录成功后自动 rehash 到 Argon2id 的入口。Spring Security 7 的
     * {@link org.springframework.security.config.annotation.authentication.configuration.InitializeUserDetailsManagerConfigurer}
     * 仅会自动创建 DAP 并 set UserDetailsService + PasswordEncoder，**不会** set
     * UserDetailsPasswordService；因此必须显式声明本 Bean，否则 {@code upgradeEncoding=true} 也不会触发 rehash。
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsPasswordService(userDetailsPasswordService);
        return provider;
    }

    /**
     * Redis-backed pending-challenge store。复用 PortalSessionConfiguration 已声明的
     * {@code springSessionDefaultRedisSerializer}（Jackson 3 + AuthenticationJacksonModule）
     * 做序列化——保证 pending Authentication 的 round-trip 与 Spring Session 写入 live
     * session 完全一致 schema。
     */
    @Bean
    public MfaPendingAuthenticationStore mfaPendingAuthenticationStore(RedisConnectionFactory connectionFactory,
                                                                       RedisSerializer<Object> springSessionDefaultRedisSerializer) {
        return new MfaPendingAuthenticationStore(connectionFactory,
            springSessionDefaultRedisSerializer);
    }

    /**
     * Brute-force throttle for MFA challenge：默认 5 次失败 / 15 分钟窗口，counter 写在
     * {@code ULP_MFA_FAIL:user:{userId}}。阈值对齐 spec.md，未来灰度可通过
     * {@code @ConfigurationProperties} 抽出。
     */
    @Bean
    public MfaLockoutService mfaLockoutService(StringRedisTemplate redisTemplate) {
        return new MfaLockoutService(redisTemplate);
    }

    /**
     * 第二因子 verify-and-commit 引擎。{@link Collection} 注入会自动包含所有
     * {@link MfaService} bean —— portal 当前只有 {@link UserMfaService}，未来若新增
     * 其他 subject 类型，新 bean 注册即可，本配置无需改动。
     */
    @Bean
    public MfaChallengeService mfaChallengeService(MfaPendingAuthenticationStore mfaPendingStore,
                                                   MfaLockoutService mfaLockoutService,
                                                   MfaCodeVerifier codeVerifier,
                                                   MfaSecretCipher secretCipher,
                                                   Collection<MfaService> mfaServices,
                                                   MfaBackupCodeService mfaBackupCodeService) {
        return new MfaChallengeService(mfaPendingStore, mfaLockoutService, codeVerifier,
            secretCipher, mfaServices, mfaBackupCodeService);
    }

    /**
     * Backup-code consume 引擎。{@link Collection} 注入会自动包含所有
     * {@link MfaBackupCodeStore} bean —— portal 当前只有
     * {@link cn.frank.ulp.portal.service.security.UserBackupCodeStore}，按 subject type 路由。
     * {@link PasswordEncoder} 复用全局 {@code DelegatingPasswordEncoder}（默认 Argon2id）做
     * 明文 → 已存储 hash 的恒定时间比较；与 bind confirm 用同一个 encoder 才能 matches。
     */
    @Bean
    public MfaBackupCodeService mfaBackupCodeService(Collection<MfaBackupCodeStore> stores,
                                                     PasswordEncoder passwordEncoder) {
        return new MfaBackupCodeService(stores, passwordEncoder);
    }

    /**
     * Micrometer wrapper for MFA counters + pending-gauge. Cached Redis SCAN (≥30s) keeps
     * Prometheus scrape cost bounded; controllers / services pass the same bean for outcome
     * tagging.
     */
    @Bean
    public MfaMetrics mfaMetrics(MeterRegistry meterRegistry, StringRedisTemplate redisTemplate) {
        return new MfaMetrics(meterRegistry, redisTemplate);
    }

    /**
     * Portal 侧 MFA 触发策略：三分支判定，仅对 {@code userType=user} 的 principal 生效。
     *
     * <ul>
     *   <li>已绑定（{@code loadActiveCipher != null}） → {@link MfaDecision#CHALLENGE_REQUIRED}：
     *       走 TOTP / 备份码二次验证流。
     *   <li>未绑定 + 组织开启 {@code mfa_enforced} → {@link MfaDecision#SETUP_REQUIRED}：
     *       提交 session 但同时下发 {@code mfa_setup_required}，前端跳 {@code /mfa/setup}。
     *   <li>未绑定 + 组织未强制 → {@link MfaDecision#DIRECT_LOGIN}：自愿模式，直接登录。
     * </ul>
     *
     * <p>非 user principal（admin / 缺 id / 不是 {@link UserDetails}）一律
     * {@link MfaDecision#DIRECT_LOGIN} 兜底放行 —— org-level enforcement 与 admin 无关；
     * MFA 是叠加层，不应把异常 principal 升级为 500。
     */
    @Bean
    public MfaTriggerStrategy portalMfaTriggerStrategy(UserMfaService userMfaService,
                                                       OrgMfaPolicyService orgMfaPolicyService) {
        return authentication -> {
            if (authentication == null
                || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
                return MfaDecision.DIRECT_LOGIN;
            }
            UserType userType = userDetails.getUserType();
            if (userType == null || !UserType.USER.getType().equals(userType.getType())) {
                return MfaDecision.DIRECT_LOGIN;
            }
            String userId = userDetails.getId();
            if (userId == null || userId.isBlank()) {
                return MfaDecision.DIRECT_LOGIN;
            }
            String cipher = userMfaService.loadActiveCipher(userId);
            if (cipher != null) {
                return MfaDecision.CHALLENGE_REQUIRED;
            }
            return orgMfaPolicyService.isUserEnforced(userId) ? MfaDecision.SETUP_REQUIRED
                : MfaDecision.DIRECT_LOGIN;
        };
    }

}
