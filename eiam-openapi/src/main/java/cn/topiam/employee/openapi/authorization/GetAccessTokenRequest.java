/*
 * eiam-openapi - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.openapi.authorization;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 获取 access_token 授权
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/23 21:09
 */
@Data
@Schema(description = "获取 access_token 授权")
public class GetAccessTokenRequest implements Serializable {
    /**
     * 客户端ID
     */
    @JsonProperty(value = "client_id")
    @Schema(description = "客户端ID")
    private String clientId;

    /**
     * 客户端秘钥
     */
    @JsonProperty(value = "client_secret")
    @Schema(description = "客户端秘钥")
    private String clientSecret;
}
