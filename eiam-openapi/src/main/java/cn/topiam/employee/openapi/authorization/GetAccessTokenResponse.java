/*
 * eiam-openapi - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.openapi.authorization;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/23 21:19
 */
@Data
@Schema(description = "获取 access_token 响应")
public class GetAccessTokenResponse {

    /**
     * access_token
     */
    @JsonProperty(value = "access_token")
    @Schema(name = "access_token")
    private String  accessToken;

    /**
     * expires_in
     */
    @JsonProperty(value = "expires_in")
    @Schema(name = "expires_in")
    private Integer expiresIn;

    /**
     * code
     */
    @JsonProperty(value = "code")
    @Schema(name = "code")
    private String  code;

    /**
     * msg
     */
    @JsonProperty(value = "msg")
    @Schema(name = "msg")
    private String  msg;
}
