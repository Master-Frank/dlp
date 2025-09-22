package cn.topiam.employee.support.trace;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 跟踪配置类
 * 用于配置跟踪相关的Bean
 */
@Configuration
public class TraceConfiguration {
   
   /**
    * 跟踪工具Bean
    * 
    * @return 跟踪工具
    */
   @Bean
   public TraceUtils traceUtils() {
      return new TraceUtils();
   }
}