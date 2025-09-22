package cn.topiam.employee.support.hibernate;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Hibernate配置类
 * 用于启用JPA审计功能
 */
@Configuration
@EnableJpaAuditing
public class HibernateConfiguration {
}