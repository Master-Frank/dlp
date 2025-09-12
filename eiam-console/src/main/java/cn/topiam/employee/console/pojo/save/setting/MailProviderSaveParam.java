/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.save.setting;

import java.io.Serial;
import java.io.Serializable;

import cn.topiam.employee.common.message.enums.MailProvider;
import cn.topiam.employee.common.message.enums.MailSafetyType;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 保存邮件服务商配置入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 21:27
 */
@Data
@Schema(description = "保存邮件服务商配置入参")
public class MailProviderSaveParam implements Serializable {
    @Serial
    private static final long serialVersionUID = -6723117700517052520L;
    /**
     * 平台
     */
    @Schema(description = "提供商")
    @NotNull(message = "邮件提供商不能为空")
    private MailProvider      provider;
    /**
     * smtp地址
     */
    @Schema(description = "smtp地址")
    private String            smtpUrl;

    /**
     * 端口
     */
    @Schema(description = "端口")
    private Integer           port;

    /**
     * 安全验证
     */
    @Schema(description = "安全验证")
    private MailSafetyType    safetyType;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String            username;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String            secret;

}
