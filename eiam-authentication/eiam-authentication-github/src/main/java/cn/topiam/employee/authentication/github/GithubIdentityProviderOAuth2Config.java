/*
 * eiam-authentication-github - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.github;

import java.io.Serial;

import cn.topiam.employee.authentication.common.client.IdentityProviderConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * GITHUB 认证配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/4/4 23:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GithubIdentityProviderOAuth2Config extends IdentityProviderConfig {
    @Serial
    private static final long serialVersionUID = -6850223527422243176L;

    /**
     * Client ID
     */
    @NotBlank(message = "Client ID 不能为空")
    private String            clientId;

    /**
     * Client Secret
     */
    @NotBlank(message = "Client Secret 不能为空")
    private String            clientSecret;
}
