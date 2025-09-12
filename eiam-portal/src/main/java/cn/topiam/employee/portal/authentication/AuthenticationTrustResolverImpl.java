/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.authentication;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;

import cn.topiam.employee.authentication.common.authentication.IdentityProviderNotBindAuthentication;
import cn.topiam.employee.support.security.password.authentication.NeedChangePasswordAuthenticationToken;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/3/22 21:02
 */
public class AuthenticationTrustResolverImpl implements AuthenticationTrustResolver {
    private final AuthenticationTrustResolver delegate = new org.springframework.security.authentication.AuthenticationTrustResolverImpl();

    @Override
    public boolean isAnonymous(Authentication authentication) {
        return this.delegate.isAnonymous(authentication)
               || (authentication instanceof NeedChangePasswordAuthenticationToken)
               //  IDP 提供商，未认证为匿名
               || (authentication instanceof IdentityProviderNotBindAuthentication);
    }

    @Override
    public boolean isRememberMe(Authentication authentication) {
        return this.delegate.isRememberMe(authentication);
    }
}
