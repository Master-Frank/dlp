/*
 * eiam-protocol-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.code.configurer;

import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import cn.topiam.employee.protocol.code.EndpointMatcher;

/**
 * Base configurer
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/27 21:06
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public abstract class AbstractConfigurer {
    private final ObjectPostProcessor<Object> objectPostProcessor;

    public AbstractConfigurer(ObjectPostProcessor<Object> objectPostProcessor) {
        this.objectPostProcessor = objectPostProcessor;
    }

    /**
     * init
     *
     * @param httpSecurity {@link HttpSecurity}
     */
    public abstract void init(HttpSecurity httpSecurity);

    /**
     * configure
     *
     * @param httpSecurity {@link HttpSecurity}
     */
    public abstract void configure(HttpSecurity httpSecurity);

    /**
     * 获取请求匹配器
     *
     * @return {@link EndpointMatcher}
     */
    public abstract EndpointMatcher getEndpointMatcher();

    public final <T> T postProcess(T object) {
        return (T) this.objectPostProcessor.postProcess(object);
    }

    public final ObjectPostProcessor<Object> getObjectPostProcessor() {
        return this.objectPostProcessor;
    }

}
