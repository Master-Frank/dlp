/*
 * eiam-authentication-gitee - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.gitee;

import java.io.Serial;

import cn.topiam.employee.authentication.common.client.IdentityProviderConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * Gitee 登录
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/9 22:07 21:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GiteeIdentityProviderOAuth2Config extends IdentityProviderConfig {
    @Serial
    private static final long serialVersionUID = -5831048603320371078L;
    /**
     * 客户端id
     */
    @NotBlank(message = "应用clientId不能为空")
    private String            clientId;

    /**
     * 客户端Secret
     */
    @NotBlank(message = "应用clientSecret不能为空")
    private String            clientSecret;
}
