package cn.topiam.employee.support.cache;

import org.redisson.config.DefaultAutoConfigurationCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Bean;

/**
 * 缓存配置类
 * 用于配置缓存相关的Bean
 */
public class CacheConfiguration {
   
   /**
    * 默认自动配置自定义器Bean
    * 
    * @param cacheProperties 缓存属性
    * @return 默认自动配置自定义器
    */
   @Bean
   public DefaultAutoConfigurationCustomizer defaultAutoConfigurationCustomizer(CacheProperties cacheProperties) {
      return new DefaultAutoConfigurationCustomizer(cacheProperties);
   }
}