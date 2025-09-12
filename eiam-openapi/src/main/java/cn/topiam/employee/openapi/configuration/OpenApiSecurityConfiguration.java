/*
 * eiam-openapi - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.openapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import cn.topiam.employee.openapi.authorization.AccessTokenAuthenticationEntryPoint;
import cn.topiam.employee.openapi.authorization.AccessTokenAuthenticationFilter;
import cn.topiam.employee.openapi.authorization.AccessTokenAuthenticationProvider;
import cn.topiam.employee.openapi.authorization.store.AccessTokenStore;
import cn.topiam.employee.openapi.authorization.store.RedisAccessTokenStore;
import cn.topiam.employee.support.web.useragent.UserAgentParser;
import static cn.topiam.employee.common.constant.ConfigBeanNameConstants.DEFAULT_SECURITY_FILTER_CHAIN;
import static cn.topiam.employee.openapi.constant.OpenApiV1Constants.AUTH_PATH;
import static cn.topiam.employee.openapi.constant.OpenApiV1Constants.OPEN_API_V1_PATH;

/**
 * ConsoleSecurityConfiguration
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2019/9/27 22:54
 */
@EnableMethodSecurity
@Configuration
public class OpenApiSecurityConfiguration {

    /**
     * securityFilterChain
     *
     * @param http {@link HttpSecurity}
     * @param accessTokenStore {@link AccessTokenStore}
     * @return {@link SecurityFilterChain}
     */
    @Bean(name = DEFAULT_SECURITY_FILTER_CHAIN)
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AccessTokenStore accessTokenStore) throws Exception {
        ProviderManager providerManager = new ProviderManager(
            new AccessTokenAuthenticationProvider(accessTokenStore));
        http.securityMatcher(OPEN_API_V1_PATH + "/**");
        http.authorizeHttpRequests(registry -> {
            registry.requestMatchers(new AntPathRequestMatcher(AUTH_PATH + "/access_token"))
                .permitAll();
            registry.anyRequest().authenticated();
        });
        //@formatter:off
        //关闭 csrf
        http.csrf(AbstractHttpConfigurer::disable);
        //关闭 cors
        http.cors(AbstractHttpConfigurer::disable);
        //关闭 session
        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //异常处理器
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(new AccessTokenAuthenticationEntryPoint(userAgentParser)));
        http.addFilterBefore(new AccessTokenAuthenticationFilter(providerManager),BasicAuthenticationFilter.class);
        return http.build();
        //@formatter:on
    }

    /**
     * TokenStore
     *
     * @param redisTemplate {@link RedisTemplate}
     * @return {@link AccessTokenStore}
     */
    @Bean
    public AccessTokenStore tokenStore(RedisTemplate<Object, Object> redisTemplate) {
        return new RedisAccessTokenStore(redisTemplate);
    }

    private final UserAgentParser userAgentParser;

    public OpenApiSecurityConfiguration(UserAgentParser userAgentParser) {
        this.userAgentParser = userAgentParser;
    }

}
