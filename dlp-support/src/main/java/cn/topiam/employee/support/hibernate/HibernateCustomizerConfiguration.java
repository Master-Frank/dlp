package cn.topiam.employee.support.hibernate;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Hibernate自定义配置类
 * 用于导入Hibernate属性自定义器
 */
@Configuration
@Import({CustomHibernatePropertiesCustomizer.class})
public class HibernateCustomizerConfiguration {
}