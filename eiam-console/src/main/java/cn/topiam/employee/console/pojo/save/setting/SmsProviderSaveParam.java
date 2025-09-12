/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.save.setting;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson2.JSONObject;

import cn.topiam.employee.common.entity.setting.config.SmsConfig;
import cn.topiam.employee.common.enums.Language;
import cn.topiam.employee.common.message.enums.SmsProvider;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 保存短信服务商创建请求入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/7/31 21:34
 */
@Data
@Schema(description = "保存短信验证服务入参")
public class SmsProviderSaveParam implements Serializable {
    @Serial
    private static final long              serialVersionUID = 4125843198392920166L;

    /**
     * 平台
     */
    @Schema(description = "提供商")
    @NotNull(message = "短信提供商不能为空")
    private SmsProvider                    provider;

    /**
     * 配置JSON串
     */
    @Schema(description = "配置JSON串")
    @NotNull(message = "配置不能为空")
    private JSONObject                     config;

    /**
     * 场景语言
     */
    @Schema(description = "场景语言")
    @NotNull(message = "场景语言不能为空")
    private Language                       language;

    /**
     * 短信模板配置
     */
    @Schema(description = "短信模板配置")
    @NotNull(message = "短信模板配置不能为空")
    private List<SmsConfig.TemplateConfig> templates;
}
