/*
 * eiam-protocol-oidc - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.eiam.protocol.oidc.authentication;

import java.util.Objects;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

import cn.topiam.employee.application.context.ApplicationContextHolder;

/**
 * RedisOAuth2AuthorizationService
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/10/31 21:41
 */
@SuppressWarnings({ "AlibabaServiceOrDaoClassShouldEndWithImpl",
                    "AlibabaClassNamingShouldBeCamel" })
public class RedisOAuth2AuthorizationServiceWrapper extends RedisOAuth2AuthorizationService {

    public RedisOAuth2AuthorizationServiceWrapper(RedisOperations<String, String> redisOperations,
                                                  RegisteredClientRepository clientRepository) {
        super(redisOperations, clientRepository);
    }

    /**
     * Saves the {@link OAuth2Authorization}.
     *
     * @param authorization the {@link OAuth2Authorization}
     */
    @Override
    public void save(OAuth2Authorization authorization) {
        String appId = ApplicationContextHolder.getApplicationContext().getAppId();
        if (authorization.getRegisteredClientId().equals(String.valueOf(appId))) {
            super.save(authorization);
        }
    }

    /**
     * Removes the {@link OAuth2Authorization}.
     *
     * @param authorization the {@link OAuth2Authorization}
     */
    @Override
    public void remove(OAuth2Authorization authorization) {
        String appId = ApplicationContextHolder.getApplicationContext().getAppId();
        if (authorization.getRegisteredClientId().equals(String.valueOf(appId))) {
            super.remove(authorization);
        }
    }

    /**
     * Returns the {@link OAuth2Authorization} identified by the provided {@code id},
     * or {@code null} if not found.
     *
     * @param id the authorization identifier
     * @return the {@link OAuth2Authorization} if found, otherwise {@code null}
     */
    @Override
    public OAuth2Authorization findById(String id) {
        OAuth2Authorization authorization = super.findById(id);
        if (!Objects.isNull(authorization)) {
            String appId = ApplicationContextHolder.getApplicationContext().getAppId();
            if (authorization.getRegisteredClientId().equals(String.valueOf(appId))) {
                return authorization;
            }
        }
        return null;
    }

    /**
     * Returns the {@link OAuth2Authorization} containing the provided {@code token},
     * or {@code null} if not found.
     *
     * @param token     the token credential
     * @param tokenType the {@link OAuth2TokenType token type}
     * @return the {@link OAuth2Authorization} if found, otherwise {@code null}
     */
    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        OAuth2Authorization authorization = super.findByToken(token, tokenType);
        if (!Objects.isNull(authorization)) {
            String appId = ApplicationContextHolder.getApplicationContext().getAppId();
            if (authorization.getRegisteredClientId().equals(String.valueOf(appId))) {
                return authorization;
            }
        }
        return null;
    }
}
