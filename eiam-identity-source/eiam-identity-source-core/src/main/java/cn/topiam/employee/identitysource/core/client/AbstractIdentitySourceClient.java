/*
 * eiam-identity-source-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.core.client;

import org.springframework.util.Assert;

import cn.topiam.employee.identitysource.core.IdentitySourceConfig;

import lombok.Getter;

/**
 * AbstractIdentityDataProcessor
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/28 03:40
 */
@Getter
public abstract class AbstractIdentitySourceClient<T extends IdentitySourceConfig>
                                                  implements IdentitySourceClient {

    public static String ACCESS_KEY = "ACCESS_KEY";

    protected AbstractIdentitySourceClient(T config) {
        Assert.notNull(config, "IdentityProviderConfig Not Null");
        this.config = config;
    }

    /**
     * 配置
     */
    private final T config;
}
