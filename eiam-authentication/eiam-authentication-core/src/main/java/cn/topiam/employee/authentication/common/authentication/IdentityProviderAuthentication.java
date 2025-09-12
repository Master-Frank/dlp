/*
 * eiam-authentication-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.common.authentication;

import java.io.Serial;
import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import cn.topiam.employee.authentication.common.IdentityProviderType;

import lombok.Getter;

/**
 * Idp Authentication
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/12/16 21:34
 */
public class IdentityProviderAuthentication extends AbstractAuthenticationToken
                                            implements java.io.Serializable {

    // ~ Instance fields

    @Serial
    private static final long          serialVersionUID = -1506897701981698420L;

    private final Object               principal;

    /**
     * 提供商类型
     */
    @Getter
    private final IdentityProviderType providerType;

    /**
     * 提供商ID
     */
    @Getter
    private final String               providerId;

    // ~ Constructors

    /**
     * This constructor can be safely used by any code that wishes to create a
     * <code>UsernamePasswordAuthenticationToken</code>, as the {@link #isAuthenticated()}
     * will return <code>false</code>.
     */
    public IdentityProviderAuthentication(Object principal, IdentityProviderType providerType,
                                          String providerId,
                                          Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.providerId = providerId;
        this.providerType = providerType;
        super.setAuthenticated(true);
    }

    // ~ Methods

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }

}
