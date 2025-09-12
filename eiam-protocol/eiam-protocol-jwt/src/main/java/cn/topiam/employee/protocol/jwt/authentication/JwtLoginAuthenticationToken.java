/*
 * eiam-protocol-jwt - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.jwt.authentication;

import java.util.ArrayList;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;

import cn.topiam.employee.application.jwt.model.JwtProtocolConfig;

import lombok.Getter;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/8 00:08
 */
public class JwtLoginAuthenticationToken extends AbstractAuthenticationToken {
    /**
     * 主体
     */
    private final Authentication    principal;

    /**
     * 目标URL
     */
    @Getter
    private String                  targetUrl;

    /**
     * 协议配置
     */
    @Getter
    private final JwtProtocolConfig config;

    public JwtLoginAuthenticationToken(Authentication principal, String targetUrl,
                                       JwtProtocolConfig config) {
        super(new ArrayList<>());
        this.principal = principal;
        this.targetUrl = targetUrl;
        this.config = config;
    }

    /**
     * The credentials that prove the principal is correct. This is usually a password,
     * but could be anything relevant to the <code>AuthenticationManager</code>. Callers
     * are expected to populate the credentials.
     *
     * @return the credentials that prove the identity of the <code>Principal</code>
     */
    @Override
    public Object getCredentials() {
        return "";
    }

    /**
     * The identity of the principal being authenticated. In the case of an authentication
     * request with username and password, this would be the username. Callers are
     * expected to populate the principal for an authentication request.
     * <p>
     * The <tt>AuthenticationManager</tt> implementation will often return an
     * <tt>Authentication</tt> containing richer information as the principal for use by
     * the application. Many of the authentication providers will create a
     * {@code UserDetails} object as the principal.
     *
     * @return the <code>Principal</code> being authenticated or the authenticated
     * principal after authentication.
     */
    @Override
    public Object getPrincipal() {
        return principal;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

}
