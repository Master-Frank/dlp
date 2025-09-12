/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.app;

import java.time.LocalDateTime;

import cn.topiam.employee.common.enums.app.AppPolicySubjectType;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 应用访问授权策略结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/9/27 21:29
 */
@Data
@Schema(description = "应用访问授权策略响应")
public class AppAccessPolicyResult {

    /**
     * id
     */
    @Schema(description = "id")
    private String               id;

    /**
     * 应用ID
     */
    @Schema(description = "应用ID")
    private String               appId;

    /**
     * 应用名称
     */
    @Schema(description = "应用名称")
    private String               appName;

    /**
     * 应用图标
     */
    @Schema(description = "应用图标")
    private String               appIcon;

    /**
     * 模板
     */
    @Schema(description = "应用模版")
    private String               appTemplate;

    /**
     * 协议
     */
    @Schema(description = "应用协议")
    private String               appProtocol;

    /**
     * 应用类型
     */
    @Schema(description = "应用类型")
    private String               appType;

    /**
     * 主体ID（用户、分组、组织机构）
     */
    @Schema(description = "主体ID")
    private String               subjectId;

    /**
     * 授权主体
     */
    @Schema(description = "授权主体")
    private String               subjectName;

    /**
     * 主体类型（用户、分组、组织机构）
     */
    @Schema(description = "主体类型")
    private AppPolicySubjectType subjectType;

    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    private Boolean              enabled;

    /**
     * 添加时间
     */
    @Schema(description = "添加时间")
    private LocalDateTime        createTime;

}
