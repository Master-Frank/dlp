/*
 * eiam-identity-source-feishu - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.feishu.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import cn.topiam.employee.identitysource.feishu.domain.BaseResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * access token响应
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022-02-17 23:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class GetAccessTokenResponse extends BaseResponse {
    /**
     * 访问 token
     */
    @JsonProperty("tenant_access_token")
    private String tenantAccessToken;

    /**
     * token 过期时间，单位: 秒
     */
    private int    expire;
}
