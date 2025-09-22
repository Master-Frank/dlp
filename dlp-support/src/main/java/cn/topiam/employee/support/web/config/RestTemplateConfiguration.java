package cn.topiam.employee.support.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Rest模板配置类
 * 用于配置RestTemplate Bean
 */
@Configuration
public class RestTemplateConfiguration {
   
   /**
    * Rest模板Bean
    * 
    * @return Rest模板
    */
   @Bean
   public RestTemplate restTemplate() {
      return new RestTemplate();
   }
}