/*
 * eiam-protocol-oidc - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.eiam.protocol.oidc.authentication;

import java.util.Map;

import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthenticationContext;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.util.Assert;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2022/11/9 22:44
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public final class OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext implements
                                                                                 OAuth2AuthenticationContext {
    private final Map<Object, Object> context;

    private OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext(Map<Object, Object> context) {
        this.context = Map.copyOf(context);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <V> V get(Object key) {
        return hasKey(key) ? (V) this.context.get(key) : null;
    }

    @Override
    public boolean hasKey(Object key) {
        Assert.notNull(key, "key cannot be null");
        return this.context.containsKey(key);
    }

    /**
     * Returns the {@link RegisteredClient registered client}.
     *
     * @return the {@link RegisteredClient}
     */
    public RegisteredClient getRegisteredClient() {
        return get(RegisteredClient.class);
    }

    /**
     * Constructs a new {@link Builder} with the provided {@link OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken}.
     *
     * @param authentication the {@link OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken}
     * @return the {@link Builder}
     */
    public static Builder with(OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken authentication) {
        return new Builder(authentication);
    }

    /**
     * A builder for {@link OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext}.
     */
    public static final class Builder extends
                                      AbstractBuilder<OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext, Builder> {

        private Builder(OAuth2AuthorizationResourceOwnerPasswordAuthenticationToken authentication) {
            super(authentication);
        }

        /**
         * Sets the {@link RegisteredClient registered client}.
         *
         * @param registeredClient the {@link RegisteredClient}
         * @return the {@link Builder} for further configuration
         */
        public Builder registeredClient(RegisteredClient registeredClient) {
            return put(RegisteredClient.class, registeredClient);
        }

        /**
         * Builds a new {@link OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext}.
         *
         * @return the {@link OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext}
         */
        @Override
        public OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext build() {
            Assert.notNull(get(RegisteredClient.class), "registeredClient cannot be null");
            return new OAuth2AuthorizationResourceOwnerPasswordAuthenticationContext(getContext());
        }

    }

}
