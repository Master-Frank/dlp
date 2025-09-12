/*
 * eiam-authentication-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.common.authentication;

import java.io.Serial;
import java.util.ArrayList;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import lombok.Getter;

/**
 * Idp Authentication
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/12/16 21:34
 */
@Getter
public class IdentityProviderBindAuthentication extends AbstractAuthenticationToken
                                                implements java.io.Serializable {

    // ~ Instance fields

    @Serial
    private static final long serialVersionUID = -1506897701981698420L;

    private final String      username;

    private final String      password;

    public IdentityProviderBindAuthentication(String username, String password) {
        super(new ArrayList<>());
        this.username = username;
        this.password = password;
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
        return username;
    }

}
