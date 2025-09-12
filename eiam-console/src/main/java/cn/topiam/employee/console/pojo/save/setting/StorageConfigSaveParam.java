/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.save.setting;

import java.io.Serial;
import java.io.Serializable;

import com.alibaba.fastjson2.JSONObject;

import cn.topiam.employee.common.storage.enums.StorageProvider;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 保存存储配置入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 21:27
 */
@Data
@Schema(description = "保存存储配置入参")
public class StorageConfigSaveParam implements Serializable {
    @Serial
    private static final long serialVersionUID = -6723117700517052520L;
    /**
     * provider
     */
    @NotNull(message = "存储提供商不能为空")
    @Schema(description = "存储提供商")
    private StorageProvider   provider;
    /**
     * config
     */
    @NotNull(message = "存储提供商配置不能为空")
    @Schema(description = "配置JSON串")
    private JSONObject        config;
}
