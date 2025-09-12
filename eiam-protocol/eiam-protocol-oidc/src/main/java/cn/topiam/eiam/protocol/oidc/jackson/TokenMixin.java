/*
 * eiam-protocol-oidc - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.eiam.protocol.oidc.jackson;

import java.util.Map;

import org.springframework.security.oauth2.core.OAuth2Token;

import com.fasterxml.jackson.annotation.*;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/30 21:10
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
abstract class TokenMixin {

    @JsonCreator
    TokenMixin(@JsonProperty("token") OAuth2Token token,
               @JsonProperty("metadata") Map<String, Object> metadata) {
    }

}
