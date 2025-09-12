/*
 * eiam-authentication-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.common.client;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * 已注册的身份提供程序客户端
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2024/3/24 18:37
 */
@Data
@Builder
public class RegisteredIdentityProviderClient<T extends IdentityProviderConfig>
                                             implements Serializable {

    /**
     * id
     */
    private String id;

    /**
     * code
     */
    private String code;

    /**
     * name
     */
    private String name;

    /**
     * 配置
     */
    private T      config;
}
