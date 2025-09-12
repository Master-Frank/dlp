/*
 * eiam-protocol-oidc - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.eiam.protocol.oidc.authentication;

import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * OAuth2 简化模式授权请求身份验证异常
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/26 22:54
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class OAuth2AuthorizationImplicitRequestAuthenticationException extends
                                                                       OAuth2AuthenticationException {
    private final OAuth2AuthorizationImplicitRequestAuthenticationToken authorizationImplicitRequestAuthenticationToken;

    public OAuth2AuthorizationImplicitRequestAuthenticationException(OAuth2Error error,
                                                                     @Nullable OAuth2AuthorizationImplicitRequestAuthenticationToken authorizationImplicitRequestAuthenticationToken) {
        super(error);
        this.authorizationImplicitRequestAuthenticationToken = authorizationImplicitRequestAuthenticationToken;
    }

    public OAuth2AuthorizationImplicitRequestAuthenticationException(OAuth2Error error,
                                                                     Throwable cause,
                                                                     @Nullable OAuth2AuthorizationImplicitRequestAuthenticationToken authorizationImplicitRequestAuthenticationToken) {
        super(error, cause);
        this.authorizationImplicitRequestAuthenticationToken = authorizationImplicitRequestAuthenticationToken;
    }

    @Nullable
    public OAuth2AuthorizationImplicitRequestAuthenticationToken getAuthorizationImplicitRequestAuthenticationToken() {
        return this.authorizationImplicitRequestAuthenticationToken;
    }

}
