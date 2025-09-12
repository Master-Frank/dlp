/*
 * eiam-protocol-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.code.util;

import java.util.Optional;

import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/11/25 14:03
 */
public final class SpringSecurityEndpointUtils<T> {

    /**
     * The Oauth 2 endpoint filter.
     */
    private final T oauth2EndpointFilter;

    /**
     * Instantiates a new Spring doc security o auth 2 endpoint utils.
     *
     * @param oauth2EndpointFilter the oauth 2 endpoint filter
     */
    public SpringSecurityEndpointUtils(T oauth2EndpointFilter) {
        this.oauth2EndpointFilter = oauth2EndpointFilter;
    }

    /**
     * Find endpoint object.
     *
     * @param filterChain the filter chain
     * @return the object
     */
    public Object findEndpoint(SecurityFilterChain filterChain) {
        Optional<?> oAuth2EndpointFilterOptional = filterChain.getFilters().stream()
            .filter(((Class<?>) oauth2EndpointFilter)::isInstance)
            .map(((Class<?>) oauth2EndpointFilter)::cast).findAny();
        return oAuth2EndpointFilterOptional.orElse(null);
    }
}
