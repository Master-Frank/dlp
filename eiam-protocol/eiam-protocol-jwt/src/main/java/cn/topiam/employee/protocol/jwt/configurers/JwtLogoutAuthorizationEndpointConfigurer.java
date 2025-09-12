/*
 * eiam-protocol-jwt - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.jwt.configurers;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import cn.topiam.employee.protocol.code.EndpointMatcher;
import cn.topiam.employee.protocol.code.configurer.AbstractConfigurer;
import cn.topiam.employee.protocol.code.configurer.AuthenticationUtils;
import cn.topiam.employee.protocol.jwt.authentication.JwtLogoutAuthenticationProvider;
import cn.topiam.employee.protocol.jwt.endpoint.JwtLogoutAuthenticationEndpointFilter;
import static cn.topiam.employee.common.constant.ProtocolConstants.JwtEndpointConstants.JWT_SLO_PATH;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/5 21:58
 */
public class JwtLogoutAuthorizationEndpointConfigurer extends AbstractConfigurer {

    private RequestMatcher requestMatcher;

    public JwtLogoutAuthorizationEndpointConfigurer(ObjectPostProcessor<Object> objectPostProcessor) {
        super(objectPostProcessor);
    }

    /**
     * init
     *
     * @param httpSecurity {@link HttpSecurity}
     */
    @Override
    public void init(HttpSecurity httpSecurity) {
        requestMatcher = new OrRequestMatcher(
            new AntPathRequestMatcher(JWT_SLO_PATH, HttpMethod.GET.name()),
            new AntPathRequestMatcher(JWT_SLO_PATH, HttpMethod.POST.name()));
        httpSecurity.authenticationProvider(new JwtLogoutAuthenticationProvider());
    }

    /**
     * configure
     *
     * @param httpSecurity {@link HttpSecurity}
     */
    @Override
    public void configure(HttpSecurity httpSecurity) {
        AuthenticationManager authenticationManager = httpSecurity
            .getSharedObject(AuthenticationManager.class);
        JwtLogoutAuthenticationEndpointFilter logoutAuthenticationEndpointFilter = new JwtLogoutAuthenticationEndpointFilter(
            requestMatcher, authenticationManager);
        logoutAuthenticationEndpointFilter.setAuthenticationDetailsSource(
            AuthenticationUtils.getAuthenticationDetailsSource(httpSecurity));
        httpSecurity.addFilterBefore(postProcess(logoutAuthenticationEndpointFilter),
            LogoutFilter.class);
    }

    @Override
    public EndpointMatcher getEndpointMatcher() {
        return new EndpointMatcher(this.requestMatcher, false);
    }
}
