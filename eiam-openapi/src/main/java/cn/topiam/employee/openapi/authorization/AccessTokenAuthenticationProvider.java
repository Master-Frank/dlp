/*
 * eiam-openapi - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.openapi.authorization;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

import com.alibaba.fastjson2.JSONObject;

import cn.topiam.employee.openapi.authorization.store.AccessTokenStore;
import cn.topiam.employee.openapi.constant.OpenApiStatus;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/25 21:02
 */
public class AccessTokenAuthenticationProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(AccessTokenAuthenticationProvider.class);

    /**
     * Performs authentication with the same contract as
     * {@link AuthenticationManager#authenticate(Authentication)}
     * .
     *
     * @param authentication the authentication request object.
     * @return a fully authenticated object including credentials. May return
     * <code>null</code> if the <code>AuthenticationProvider</code> is unable to support
     * authentication of the passed <code>Authentication</code> object. In such a case,
     * the next <code>AuthenticationProvider</code> that supports the presented
     * <code>Authentication</code> class will be tried.
     * @throws AuthenticationException if authentication fails.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = ((AccessTokenAuthenticationToken) authentication).getToken();
        AccessToken accessToken = accessTokenStore.findByToken(token);
        if (!Objects.isNull(accessToken)) {
            logger.info("根据 access_token [{}] 查询到会话信息 [{}]", token,
                JSONObject.toJSONString(accessToken));
            return new AccessTokenAuthenticationToken(accessToken.getClientId(),
                accessToken.getValue(), true);
        }
        logger.info("根据 access_token [{}] 未查询到会话信息", token);
        throw new InvalidBearerTokenException(OpenApiStatus.INVALID_ACCESS_TOKEN.getDesc());
    }

    /**
     * Returns <code>true</code> if this <Code>AuthenticationProvider</code> supports the
     * indicated <Code>Authentication</code> object.
     * <p>
     * Returning <code>true</code> does not guarantee an
     * <code>AuthenticationProvider</code> will be able to authenticate the presented
     * instance of the <code>Authentication</code> class. It simply indicates it can
     * support closer evaluation of it. An <code>AuthenticationProvider</code> can still
     * return <code>null</code> from the {@link #authenticate(Authentication)} method to
     * indicate another <code>AuthenticationProvider</code> should be tried.
     * </p>
     * <p>
     * Selection of an <code>AuthenticationProvider</code> capable of performing
     * authentication is conducted at runtime the <code>ProviderManager</code>.
     * </p>
     *
     * @param authentication {@link Class}
     * @return <code>true</code> if the implementation can more closely evaluate the
     * <code>Authentication</code> class presented
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return AccessTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private final AccessTokenStore accessTokenStore;

    public AccessTokenAuthenticationProvider(AccessTokenStore accessTokenStore) {
        Assert.notNull(accessTokenStore, "token cannot be empty");
        this.accessTokenStore = accessTokenStore;
    }
}
