/*
 * eiam-openapi - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.openapi.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import cn.topiam.employee.EiamOpenApiApplication;
import cn.topiam.employee.support.util.VersionUtils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import static cn.topiam.employee.openapi.constant.OpenApiV1Constants.OPEN_API_NAME;
import static cn.topiam.employee.openapi.constant.OpenApiV1Constants.OPEN_API_V1_PATH;

/**
 * OpenAPI 文档
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/9/5 21:00
 */
@Configuration
public class OpenApiConfiguration {

    /**
     * 权限管理 RestAPI
     *
     * @return {@link GroupedOpenApi}
     */
    @Bean
    public GroupedOpenApi permissionRestApi() {
        return GroupedOpenApi.builder().group(OPEN_API_NAME).pathsToMatch(OPEN_API_V1_PATH + "/**")
            .build();
    }

    /**
     * API INFO
     *
     * @return {@link Info}
     */
    private Info info() {
        Contact contact = new Contact();
        contact.setEmail("support@topiam.cn");
        contact.setName("TopIAM");
        contact.setUrl("https://eiam.topiam.cn");
        return new Info()
            //title
            .title(environment.getProperty("spring.application.name"))
            //描述
            .description("REST API 文档")
            //服务条款网址
            .termsOfService("https://eiam.topiam.cn")
            //内容
            .contact(contact)
            //版本
            .version(VersionUtils.getVersion(EiamOpenApiApplication.class));
    }

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(info());
    }

    private final Environment environment;

    public OpenApiConfiguration(Environment environment) {
        this.environment = environment;
    }

}
