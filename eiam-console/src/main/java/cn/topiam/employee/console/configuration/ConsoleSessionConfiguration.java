/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.configuration;

import org.springframework.boot.autoconfigure.session.RedisSessionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;

import cn.topiam.employee.core.security.session.ClusterSessionRegistryImpl;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/22 21:43
 */
@Configuration
public class ConsoleSessionConfiguration {

    @Bean
    public SessionRegistry sessionRegistry(FindByIndexNameSessionRepository<? extends Session> sessionRepository,
                                           RedisSessionProperties redisSessionProperties) {
        return new ClusterSessionRegistryImpl<>(sessionRepository,
            redisSessionProperties.getNamespace());
    }
}
