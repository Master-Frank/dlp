/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.configuration;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

/**
 * 控制台前端配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on  2022/12/4 21:49
 */
@Configuration
public class PortalFrontendConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射静态资源根目录到  frontend
        registry.addResourceHandler("/**").addResourceLocations("classpath:/fe/")
            .resourceChain(true).addResolver(new PathResourceResolver() {
                // 后端匹配不到路由时转给前端
                @Override
                protected Resource getResource(@NotNull String resourcePath,
                                               @NotNull Resource location) throws IOException {
                    Resource resource = super.getResource(resourcePath, location);
                    if (resource == null) {
                        resource = super.getResource("index.html", location);
                    }
                    return resource;
                }
            });
    }
}
