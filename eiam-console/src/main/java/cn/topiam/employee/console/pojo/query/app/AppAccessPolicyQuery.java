/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.query.app;

import org.springdoc.core.annotations.ParameterObject;

import cn.topiam.employee.common.enums.app.AppPolicySubjectType;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 应用授权策略查询参数
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/9/27 21:29
 */
@Data
@Schema(description = "应用授权策略查询参数")
@ParameterObject
public class AppAccessPolicyQuery {

    /**
     * 应用id
     */
    @Parameter(description = "应用ID")
    private String               appId;

    /**
     * 授权主体
     */
    @Parameter(description = "授权主体名称")
    private String               subjectName;

    /**
     * 授权主体ID
     */
    @Parameter(description = "授权主体ID")
    private String               subjectId;

    /**
     * 主体类型（用户、分组、组织机构）
     */
    @Parameter(description = "主体类型")
    private AppPolicySubjectType subjectType;

    /**
     * 应用名称
     */
    @Parameter(description = "应用名称")
    private String               appName;
}
