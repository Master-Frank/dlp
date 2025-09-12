/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.query.app;

import java.io.Serializable;

import org.springdoc.core.annotations.ParameterObject;

import cn.topiam.employee.common.enums.app.AppCertUsingType;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 查询应用证书列表入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 23:08
 */
@Data
@Schema(description = "查询应用证书列表入参")
@ParameterObject
public class AppCertQuery implements Serializable {

    /**
     * 应用ID
     */
    @NotBlank(message = "应用ID不能为空")
    @Parameter(description = "应用ID")
    private String           appId;

    /**
     * 使用类型
     */
    @Parameter(description = "使用类型")
    private AppCertUsingType usingType;
}
