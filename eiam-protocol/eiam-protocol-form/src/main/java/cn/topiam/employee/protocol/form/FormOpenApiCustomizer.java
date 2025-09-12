/*
 * eiam-protocol-form - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.form;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.SpecVersion;

/**
 * Form openapi 定制器
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/11/25 13:53
 */
public class FormOpenApiCustomizer implements GlobalOpenApiCustomizer, ApplicationContextAware {

    private static final Logger LOGGER       = LoggerFactory.getLogger(FormOpenApiCustomizer.class);

    /**
     * Tag
     */
    private static final String ENDPOINT_TAG = "FORM API";

    /**
     * The Context.
     */
    private ApplicationContext  applicationContext;

    @Override
    public void customise(OpenAPI openApi) {
        FilterChainProxy filterChainProxy = applicationContext.getBean(
            AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME, FilterChainProxy.class);
        boolean openapi31 = SpecVersion.V31 == openApi.getSpecVersion();
        for (SecurityFilterChain filterChain : filterChainProxy.getFilterChains()) {

        }
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
