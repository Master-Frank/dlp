/*
 * eiam-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.core.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static cn.topiam.employee.support.constant.EiamConstants.CONTEXT_ENDPOINT;
import static cn.topiam.employee.support.constant.EiamConstants.CONTEXT_ENDPOINT_GROUP_NAME;

/**
 * ApiConfiguration
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2019/5/16 21:28
 */
@Configuration
public class EiamApiConfiguration {

    /**
     * 系统上下文
     *
     * @return {@link GroupedOpenApi}
     */
    @Bean
    public GroupedOpenApi contextRestApi() {
        return GroupedOpenApi.builder().group(CONTEXT_ENDPOINT_GROUP_NAME)
            .pathsToMatch(CONTEXT_ENDPOINT + "/**").build();
    }

}
