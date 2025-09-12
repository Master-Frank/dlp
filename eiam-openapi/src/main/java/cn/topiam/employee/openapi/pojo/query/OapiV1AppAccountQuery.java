/*
 * eiam-openapi - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.openapi.pojo.query;

import org.springdoc.core.annotations.ParameterObject;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * AppAccountCreateParam 应用账户查询入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/24 22:13
 */
@Data
@Schema(description = "应用账户查询入参")
@ParameterObject
public class OapiV1AppAccountQuery {

    /**
     * appId
     */
    @Parameter(description = "appId")
    private String appId;
    /**
     * 用户ID
     */
    @Parameter(description = "用户ID")
    private String userId;
    /**
     * 用户名
     */
    @Parameter(description = "用户名")
    private String username;
    /**
     * 账户名称
     */
    @Parameter(description = "账户名称")
    private String account;

    /**
     * 应用名称
     */
    @Parameter(description = "应用名称")
    private String appName;
}
