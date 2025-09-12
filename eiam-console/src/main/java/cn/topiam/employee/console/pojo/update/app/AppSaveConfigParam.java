/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.update.app;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 更新应用配置入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/7/18 23:26
 */
@Data
@Schema(description = "更新应用配置入参")
public class AppSaveConfigParam implements Serializable {

    /**
     * id
     */
    @Schema(description = "应用id")
    @NotNull(message = "ID不能为空")
    private String              id;

    /**
     * 模版
     */
    @Schema(description = "模版")
    @NotNull(message = "模版不能为空")
    private String              template;

    /**
     * 配置
     */
    @Schema(description = "配置")
    @NotNull(message = "配置不能为空")
    private Map<String, Object> config;
}
