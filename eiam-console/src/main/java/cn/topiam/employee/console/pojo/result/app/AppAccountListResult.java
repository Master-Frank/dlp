/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.app;

import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * AppAccountCreateParam 应用账户查询结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/24 22:13
 */
@Data
@Schema(description = "应用账户列表查询响应")
public class AppAccountListResult {

    /**
     * id
     */
    @Schema(description = "id")
    private String        id;

    /**
     * 应用ID
     */
    @Schema(description = "应用ID")
    private String        appId;

    /**
     * 应用名称
     */
    @Schema(description = "应用名称")
    private String        appName;

    /**
     * 模板
     */
    @Schema(description = "应用模版")
    private String        appTemplate;

    /**
     * 协议
     */
    @Schema(description = "应用协议")
    private String        appProtocol;

    /**
     * 应用类型
     */
    @Schema(description = "应用类型")
    private String        appType;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private String        userId;

    /**
     * 用户名称
     */
    @Schema(description = "用户名称")
    private String        username;

    /**
     * 账户名称
     */
    @Schema(description = "账户名称")
    private String        account;

    /**
     * 添加时间
     */
    @Schema(description = "添加时间")
    private LocalDateTime createTime;

    /**
     * 是否默认
     */
    @Schema(description = "是否默认")
    private Boolean       defaulted;
}
