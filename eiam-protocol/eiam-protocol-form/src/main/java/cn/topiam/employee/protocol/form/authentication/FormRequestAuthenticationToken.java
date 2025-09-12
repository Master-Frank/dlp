/*
 * eiam-protocol-form - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.form.authentication;

import java.util.ArrayList;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import cn.topiam.employee.application.form.model.FormProtocolConfig;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/8 00:08
 */
public class FormRequestAuthenticationToken extends AbstractAuthenticationToken {
    private final Authentication     principal;

    private final FormProtocolConfig config;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param principal {@link Authentication}
     * @param config {@link FormProtocolConfig}
     */
    public FormRequestAuthenticationToken(Authentication principal, FormProtocolConfig config) {
        super(new ArrayList<>());
        Assert.notNull(principal, "principal must not be null ");
        Assert.notNull(config, "config must not be null ");
        this.principal = principal;
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
        return this.principal;
    }

    public FormProtocolConfig getConfig() {
        return config;
    }
}
