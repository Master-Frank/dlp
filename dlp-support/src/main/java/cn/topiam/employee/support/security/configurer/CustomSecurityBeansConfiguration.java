package cn.topiam.employee.support.security.configurer;

import cn.topiam.employee.support.security.crypto.password.PasswordEncoderFactories;
import cn.topiam.employee.support.util.AesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义安全Bean配置类
 * 用于配置安全相关的Bean
 */
public class CustomSecurityBeansConfiguration {
   
   /**
    * 日志记录器
    */
   private final Logger logger = LoggerFactory.getLogger(CustomSecurityBeansConfiguration.class);

   /**
    * 密码编码器Bean
    * 
    * @return 密码编码器
    */
   @Bean
   public PasswordEncoder passwordEncoder() {
      return (new PasswordEncoderFactories()).createDelegatingPasswordEncoder();
   }

   /**
    * 安全上下文持有者策略Bean
    * 
    * @return 安全上下文持有者策略
    */
   @Bean
   public SecurityContextHolderStrategy securityContextHolderStrategy() {
      return SecurityContextHolder.getContextHolderStrategy();
   }
}