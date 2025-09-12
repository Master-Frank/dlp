/*
 * eiam-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.core.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.topiam.employee.core.security.access.SecurityAccessExpression;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/3/22 21:05
 */
@Configuration
public class EiamSecurityConfiguration {

    /**
     * 安全访问表达式
     *
     * @return {@link SecurityAccessExpression}
     */
    @Bean(name = "sae")
    public SecurityAccessExpression securityAccessExpression() {
        return new SecurityAccessExpression();
    }
}
