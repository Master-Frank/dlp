/*
 * eiam-identity-source-feishu - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.feishu.client;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import cn.topiam.employee.identitysource.core.client.AbstractIdentitySourceClient;
import cn.topiam.employee.identitysource.core.exception.ApiCallException;
import cn.topiam.employee.identitysource.feishu.FeiShuConfig;
import cn.topiam.employee.identitysource.feishu.domain.request.GetAccessTokenRequest;
import cn.topiam.employee.identitysource.feishu.domain.response.GetAccessTokenResponse;

import lombok.extern.slf4j.Slf4j;
import static cn.topiam.employee.identitysource.feishu.FeiShuConstant.ACCESS_TOKEN_URL;
import static cn.topiam.employee.identitysource.feishu.FeiShuConstant.LOGGER_NAME;

/**
 * AbstractDingTalkDataProcessor
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/28 03:40
 */
@SuppressWarnings("DuplicatedCode")
@Slf4j(topic = LOGGER_NAME)
public abstract class AbstractFeiShuClient extends AbstractIdentitySourceClient<FeiShuConfig> {

    /**
     * 缓存
     */
    private Cache<String, String> cache;

    protected RestOperations      restOperations;

    protected AbstractFeiShuClient(FeiShuConfig config) {
        super(config);
        this.restOperations = new RestTemplate();
        String token = getAccessToken();
        cache.put(ACCESS_KEY, token);
    }

    /**
     * 获取访问令牌
     *
     * @return {@link String}
     */
    protected String getAccessToken() {
        if (ObjectUtils.isNotEmpty(cache)
            && ObjectUtils.isNotEmpty(cache.getIfPresent(ACCESS_KEY))) {
            return cache.getIfPresent(ACCESS_KEY);
        }
        GetAccessTokenRequest request = new GetAccessTokenRequest(getConfig().getAppId(),
            getConfig().getAppSecret());
        log.debug("获取飞书 Access Token 入参: {}", JSON.toJSONString(request));
        GetAccessTokenResponse response = postToken(request);
        Assert.notNull(response, "获取Token返回结果为空");
        log.debug("获取飞书 Access Token 返回: {}", JSON.toJSONString(response));
        if (response.isSuccess()) {
            cache = CacheBuilder.newBuilder()
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .expireAfterWrite(response.getExpire(), TimeUnit.SECONDS).build();
            cache.put(ACCESS_KEY, response.getTenantAccessToken());
            return cache.getIfPresent(ACCESS_KEY);
        }
        log.error("获取飞书 Access Token 失败: {}", JSON.toJSONString(response));
        throw new ApiCallException("获取飞书 Access Token 失败");
    }

    /**
     * 获取token
     * @param json {@link Object}
     * @return {@link GetAccessTokenResponse}
     */
    private GetAccessTokenResponse postToken(Object json) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(JSON.toJSONString(json), headers);
        ResponseEntity<GetAccessTokenResponse> response = restOperations.exchange(ACCESS_TOKEN_URL,
            HttpMethod.POST, requestEntity, GetAccessTokenResponse.class);
        return response.getBody();
    }

    /**
     * 设置 RestOperations
     */
    public void setRestOperations(RestOperations restOperations) {
        Assert.notNull(restOperations, "restOperations cannot be null");
        this.restOperations = restOperations;
    }
}
