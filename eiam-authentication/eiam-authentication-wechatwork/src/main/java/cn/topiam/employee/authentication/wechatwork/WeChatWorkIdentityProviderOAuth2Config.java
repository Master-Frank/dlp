/*
 * eiam-authentication-wechatwork - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.wechatwork;

import java.io.Serial;

import cn.topiam.employee.authentication.common.client.IdentityProviderConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * 企业微信扫码配置参数
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/8 21:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WeChatWorkIdentityProviderOAuth2Config extends IdentityProviderConfig {
    @Serial
    private static final long serialVersionUID = -6850223527422243076L;

    /**
     * 企业ID
     */
    @NotBlank(message = "企业ID不能为空")
    private String            corpId;

    /**
     * 应用AgentID
     */
    @NotBlank(message = "应用AgentID不能为空")
    private String            agentId;

    /**
     * 应用Secret
     */
    @NotBlank(message = "应用Secret不能为空")
    private String            appSecret;
}
