package cn.topiam.employee.support.security.configurer;

import cn.topiam.employee.support.security.password.PasswordPolicyManager;
import cn.topiam.employee.support.security.userdetails.UserDetailsService;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 自定义安全配置类
 * 用于配置自定义的安全过滤器
 *
 * @param <H> HttpSecurityBuilder的子类
 */
public final class CustomSecurityConfiguration<H extends HttpSecurityBuilder<H>> extends AbstractHttpConfigurer<CustomSecurityConfiguration<H>, H> {
   
   /**
    * 从应用上下文中获取指定类型的Bean
    * 
    * @param builder HttpSecurity构建器
    * @param clazz Bean类型
    * @param <T> 泛型类型
    * @return Bean实例或null
    */
   private <T> T getBeanFromContext(H builder, Class<T> clazz) {
      ApplicationContext applicationContext = (ApplicationContext) builder.getSharedObject(ApplicationContext.class);
      if (applicationContext == null) {
         return null;
      } else {
         try {
            return (T) applicationContext.getBean(clazz);
         } catch (NoSuchBeanDefinitionException e) {
            return null;
         }
      }
   }

   /**
    * 配置方法
    * 
    * @param builder HttpSecurity构建器
    */
   public void configure(H builder) {
      UserDetailsService userDetailsService = this.getBean(builder, UserDetailsService.class);
      PasswordPolicyManager passwordPolicyManager = this.getBean(builder, PasswordPolicyManager.class);
      SessionRegistry sessionRegistry = this.getBean(builder, SessionRegistry.class);
      AsyncConfigurer asyncConfigurer = this.getBean(builder, AsyncConfigurer.class);
      
      CustomAuthenticationFilter filter = new CustomAuthenticationFilter(
              userDetailsService, passwordPolicyManager, sessionRegistry, asyncConfigurer);
      builder.addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
   }

   /**
    * 获取指定类型的Bean实例
    * 
    * @param builder HttpSecurity构建器
    * @param clazz Bean类型
    * @param <C> 泛型类型
    * @return Bean实例
    */
   private <C> C getBean(H builder, Class<C> clazz) {
      Object sharedObject = builder.getSharedObject(clazz);
      return (C) (sharedObject != null ? sharedObject : this.getBeanFromContext(builder, clazz));
   }
}