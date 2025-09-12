/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.app;

import java.io.Serializable;

import cn.topiam.employee.common.enums.app.AppProtocol;
import cn.topiam.employee.common.enums.app.AppType;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 应用列表返回
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/9/27 21:29
 */
@Data
@Schema(description = "应用列表返回")
public class AppListResult implements Serializable {
    /**
     * ID
     */
    @Parameter(description = "ID")
    private String      id;
    /**
     * 应用名称
     */
    @Parameter(description = "应用名称")
    private String      name;

    /**
     * 应用类型
     */
    @Parameter(description = "应用类型")
    private AppType     type;

    /**
     * 应用图标
     */
    @Parameter(description = "应用图标")
    private String      icon;

    /**
     * 模板
     */
    @Parameter(description = "模板")
    private String      template;

    /**
     * 协议
     */
    @Parameter(description = "协议")
    private AppProtocol protocol;

    /**
     * 是否启用
     */
    @Parameter(description = "是否启用")
    private Boolean     enabled;

    /**
     * 备注
     */
    @Parameter(description = "备注")
    private String      remark;
}
