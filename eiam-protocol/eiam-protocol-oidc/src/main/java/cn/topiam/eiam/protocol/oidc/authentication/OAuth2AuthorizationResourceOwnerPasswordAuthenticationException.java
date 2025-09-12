/*
 * eiam-protocol-oidc - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.eiam.protocol.oidc.authentication;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * This exception is thrown by {@link OAuth2AuthorizationResourceOwnerPasswordAuthenticationProvider}
 * when an attempt to authenticate the OAuth 2.0 Authorization Request (or Consent) fails.
 *
 * @author TopIAM
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class OAuth2AuthorizationResourceOwnerPasswordAuthenticationException extends
                                                                             OAuth2AuthenticationException {
    private final OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken authorizationPasswordAuthenticationToken;

    /**
     * Constructs an {@code OAuth2AuthorizationPasswordAuthenticationException} using the provided parameters.
     *
     * @param error                                    the {@link OAuth2Error OAuth 2.0 Error}
     * @param authorizationPasswordAuthenticationToken the {@link Authentication} instance of the OAuth 2.0 Authorization Request (or Consent)
     */
    public OAuth2AuthorizationResourceOwnerPasswordAuthenticationException(OAuth2Error error,
                                                                           @Nullable OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken authorizationPasswordAuthenticationToken) {
        super(error);
        this.authorizationPasswordAuthenticationToken = authorizationPasswordAuthenticationToken;
    }

    /**
     * Constructs an {@code OAuth2AuthorizationPasswordAuthenticationException} using the provided parameters.
     *
     * @param error                                  the {@link OAuth2Error OAuth 2.0 Error}
     * @param cause                                  the root cause
     * @param authorizationCodeRequestAuthentication the {@link Authentication} instance of the OAuth 2.0 Authorization Request (or Consent)
     */
    public OAuth2AuthorizationResourceOwnerPasswordAuthenticationException(OAuth2Error error,
                                                                           Throwable cause,
                                                                           @Nullable OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken authorizationCodeRequestAuthentication) {
        super(error, cause);
        this.authorizationPasswordAuthenticationToken = authorizationCodeRequestAuthentication;
    }

    /**
     * Returns the {@link Authentication} instance of the OAuth 2.0 Authorization Request (or Consent), or {@code null} if not available.
     *
     * @return the {@link OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken}
     */
    @Nullable
    public OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken getAuthorizationCodeRequestAuthentication() {
        return this.authorizationPasswordAuthenticationToken;
    }

}
