package cn.topiam.employee.support.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 审计配置类
 * 用于启用JPA审计功能并配置审计员感知器
 */
@Configuration
@EnableJpaAuditing
public class AuditorConfiguration {
   
   /**
    * 审计员感知器Bean
    * 
    * @return 审计员感知器
    */
   @Bean
   public AuditorAware<String> auditorAware() {
      return new CustomAuditorAware();
   }
}