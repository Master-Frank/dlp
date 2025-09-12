/*
 * eiam-openapi - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.openapi.authorization;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Authorization
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/25 21:53
 */
@Data
public class AccessToken {
    /**
     * 客户端ID
     */
    @JsonProperty(value = "client_id")
    private String  clientId;

    /**
     * token 值
     */
    @JsonProperty(value = "value")
    private String  value;

    /**
     * 过期时间
     */
    @JsonProperty(value = "expires_in")
    private Integer expiresIn;

    public AccessToken() {
    }

    public AccessToken(String clientId, String value, Integer expiresIn) {
        this.clientId = clientId;
        this.value = value;
        this.expiresIn = expiresIn;
    }
}
