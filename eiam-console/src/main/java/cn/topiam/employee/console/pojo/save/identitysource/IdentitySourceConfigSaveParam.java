/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.save.identitysource;

import java.io.Serial;
import java.io.Serializable;

import com.alibaba.fastjson2.JSONObject;

import cn.topiam.employee.common.entity.identitysource.config.JobConfig;
import cn.topiam.employee.common.entity.identitysource.config.StrategyConfig;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * 身份源保存配置入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/25 23:01
 */
@Data
@Schema(description = "保存身份源配置入参")
public class IdentitySourceConfigSaveParam implements Serializable {
    @Serial
    private static final long serialVersionUID = -1440230086940289961L;
    /**
     * ID
     */
    @Parameter(description = "ID")
    @NotEmpty(message = "ID不能为空")
    private String            id;

    /**
     * 提供商配置
     */
    @NotNull(message = "提供商配置不能为空")
    @Parameter(description = "提供商配置")
    private JSONObject        basicConfig;

    /**
     * 策略配置
     */
    @NotNull(message = "策略配置不能为空")
    @Parameter(description = "策略配置")
    private StrategyConfig    strategyConfig;

    /**
     * 任务配置
     */
    @Valid
    @NotNull(message = "任务配置不能为空")
    @Parameter(description = "任务配置")
    private JobConfig         jobConfig;
}
