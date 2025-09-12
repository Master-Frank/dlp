/*
 * eiam-authentication-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.common.authentication;

import java.util.ArrayList;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn  on  2023/8/7 21:47
 */
public class IdentityProviderNotBindAuthentication extends AbstractAuthenticationToken
                                                   implements java.io.Serializable {

    /**
     * 提供商ID
     */
    private final IdentityProviderUserDetails principal;

    public IdentityProviderNotBindAuthentication(IdentityProviderUserDetails principal) {
        super(new ArrayList<>());
        this.principal = principal;
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
}
