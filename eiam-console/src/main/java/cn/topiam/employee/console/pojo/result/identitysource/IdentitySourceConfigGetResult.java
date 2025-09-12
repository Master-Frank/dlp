/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.identitysource;

import java.io.Serial;
import java.io.Serializable;

import cn.topiam.employee.common.entity.identitysource.config.JobConfig;
import cn.topiam.employee.common.entity.identitysource.config.StrategyConfig;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 获取身份源配置
 *
 * @author TopIAM
 */
@Data
@Schema(description = "获取身份源配置")
public class IdentitySourceConfigGetResult implements Serializable {
    @Serial
    private static final long serialVersionUID = -1440230086940289961L;

    /**
     * ID
     */
    @Parameter(description = "ID")
    private String            id;

    /**
     * 是否已配置
     */
    @Parameter(description = "是否已配置")
    private Boolean           configured;

    /**
     * 基础配置
     */
    @Parameter(description = "基础配置")
    private Object            basicConfig;

    /**
     * 策略配置
     */
    @Parameter(description = "策略配置")
    private StrategyConfig    strategyConfig;

    /**
     * 任务配置
     */
    @Parameter(description = "任务配置")
    private JobConfig         jobConfig;
}
