/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.pojo.result;

import java.io.Serial;
import java.io.Serializable;

import cn.topiam.employee.common.enums.app.AppProtocol;
import cn.topiam.employee.common.enums.app.AppType;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 获取应用列表
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/6/8 21:58
 */
@Data
@Schema(description = "获取应用列表")
public class GetAppListResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1263170640092199401L;
    /**
     * 应用ID
     */
    @Parameter(description = "ID")
    private String            id;

    /**
     * 应用code
     */
    @Parameter(description = "CODE")
    private String            code;

    /**
     * 应用类型
     */
    @Parameter(description = "应用类型")
    private AppType           type;

    /**
     * 应用协议
     */
    @Parameter(description = "应用协议")
    private AppProtocol       protocol;

    /**
     * 应用模板
     */
    @Parameter(description = "应用模板")
    private String            template;
    /**
     * 应用名称
     */
    @Parameter(description = "应用名称")
    private String            name;

    /**
     * ICON
     */
    @Parameter(description = "ICON")
    private String            icon;

    /**
     * SSO 发起URL
     */
    @Parameter(description = "SSO 发起URL")
    private String            initLoginUrl;

    /**
     * 应用描述
     */
    @Parameter(description = "应用描述")
    private String            description;
}
