/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.save.setting;

import java.io.Serial;
import java.io.Serializable;

import com.alibaba.fastjson2.JSONObject;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 保存地理位置服务商配置入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 21:27
 */
@Data
@Schema(description = "保存地理位置服务商配置入参")
public class GeoIpProviderSaveParam implements Serializable {
    @Serial
    private static final long serialVersionUID = -6723117700517052520L;
    /**
     * 地理位置服务商
     */
    @Schema(description = "地理位置提供商")
    @NotNull(message = "地理位置提供商不能为空")
    private String            provider;

    /**
     * 配置信息
     */
    @Schema(description = "配置信息")
    private JSONObject        config;
}
