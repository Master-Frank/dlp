/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.account;

import java.time.LocalDateTime;

import cn.topiam.employee.audit.enums.EventStatus;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户登录日志返回结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022年11月13日21:49:35
 */
@Data
@Schema(description = "用户登录日志返回响应")
public class UserLoginAuditListResult {
    /**
     * ID
     */
    @Parameter(description = "ID")
    private String        id;

    /**
     * 应用名称
     */
    @Parameter(description = "应用名称")
    private String        appName;

    /**
     * 客户端IP
     */
    @Parameter(description = "客户端IP")
    private String        clientIp;

    /**
     * 操作系统
     */
    @Parameter(description = "操作系统")
    private String        platform;

    /**
     * 登录结果
     */
    @Parameter(description = "浏览器")
    private String        browser;

    /**
     * 位置
     */
    @Parameter(description = "位置")
    private String        location;

    /**
     * 事件状态
     */
    @Parameter(description = "事件状态")
    private EventStatus   eventStatus;

    /**
     * 事件时间
     */
    @Parameter(description = "事件时间")
    private LocalDateTime eventTime;
}
