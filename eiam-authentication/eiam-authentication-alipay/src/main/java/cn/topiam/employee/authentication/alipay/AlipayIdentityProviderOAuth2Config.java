/*
 * eiam-authentication-alipay - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.alipay;

import cn.topiam.employee.authentication.common.client.IdentityProviderConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * 支付宝 登录配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/19 16:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AlipayIdentityProviderOAuth2Config extends IdentityProviderConfig {

    /**
     * 商户ID
     */
    @NotBlank(message = "商户ID不能为空")
    private String appId;

    /**
     * 应用私钥
     */
    @NotBlank(message = "应用私钥")
    private String appPrivateKey;

    /**
     * 支付宝公钥
     */
    @NotBlank(message = "支付宝公钥")
    private String alipayPublicKey;

}
