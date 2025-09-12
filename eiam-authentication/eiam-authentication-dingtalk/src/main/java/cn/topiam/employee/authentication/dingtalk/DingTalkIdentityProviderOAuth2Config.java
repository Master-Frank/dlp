/*
 * eiam-authentication-dingtalk - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.dingtalk;

import java.io.Serial;

import cn.topiam.employee.authentication.common.client.IdentityProviderConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * 钉钉Oauth配置参数
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/8 21:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DingTalkIdentityProviderOAuth2Config extends IdentityProviderConfig {
    @Serial
    private static final long serialVersionUID = -6850223527422243076L;

    /**
     * 应用 key
     */
    @NotBlank(message = "应用AppKey不能为空")
    private String            appKey;

    /**
     * 应用 Secret
     */
    @NotBlank(message = "应用AppSecret不能为空")
    private String            appSecret;

    /**
     * 用于指定用户需要选择的组织
     */
    private String            corpId;
}
