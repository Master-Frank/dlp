/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.other;

import java.io.Serial;
import java.io.Serializable;

import org.springdoc.core.annotations.ParameterObject;

import com.alibaba.fastjson2.JSONObject;

import cn.topiam.employee.common.enums.identitysource.IdentitySourceProvider;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 身份源配置验证器入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/4/13 23:01
 */
@Data
@Schema(description = "身份源配置验证器入参")
@ParameterObject
public class IdentitySourceConfigValidatorParam implements Serializable {

    @Serial
    private static final long      serialVersionUID = -360733000329499789L;

    /**
     * 身份源提供商
     */
    @NotNull(message = "身份源提供商不能为空")
    @Parameter(description = "身份源提供商")
    private IdentitySourceProvider provider;

    /**
     * 配置
     */
    @NotNull(message = "验证配置不能为空")
    @Parameter(description = "配置")
    private JSONObject             config;

}
