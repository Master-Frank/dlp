/*
 * eiam-authentication-feishu - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.feishu;

import java.io.Serial;

import cn.topiam.employee.authentication.common.client.IdentityProviderConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * 飞书扫码 认证配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/12/19 22:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FeiShuIdentityProviderOAuth2Config extends IdentityProviderConfig {
    @Serial
    private static final long serialVersionUID = -6850223527422243076L;

    /**
     * APP ID
     */
    @NotBlank(message = "APP ID 不能为空")
    private String            appId;

    /**
     * APP Secret
     */
    @NotBlank(message = "APP Secret 不能为空")
    private String            appSecret;
}
