/*
 * eiam-authentication-qq - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.qq;

import java.io.Serial;

import cn.topiam.employee.authentication.common.client.IdentityProviderConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * QQ 认证配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/4/4 23:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QqIdentityProviderOAuth2Config extends IdentityProviderConfig {
    @Serial
    private static final long serialVersionUID = -6850223527422243076L;

    /**
     * APP ID
     */
    @NotBlank(message = "APP ID 不能为空")
    private String            appId;

    /**
     * APP Key
     */
    @NotBlank(message = "APP KEY 不能为空")
    private String            appKey;
}
