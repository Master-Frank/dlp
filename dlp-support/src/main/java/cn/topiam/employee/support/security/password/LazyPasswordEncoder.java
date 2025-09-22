package cn.topiam.employee.support.security.password;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 懒加载密码编码器
 * 延迟获取Spring上下文中的密码编码器
 */
public class LazyPasswordEncoder implements PasswordEncoder {
   /**
    * 密码编码器
    */
   private PasswordEncoder passwordEncoder;
   
   /**
    * 应用上下文
    */
   private final ApplicationContext applicationContext;

   /**
    * 匹配密码
    *
    * @param rawPassword 原始密码
    * @param encodedPassword 编码后的密码
    * @return 是否匹配
    */
   @Override
   public boolean matches(CharSequence rawPassword, String encodedPassword) {
      return this.getPasswordEncoder().matches(rawPassword, encodedPassword);
   }

   /**
    * 升级编码
    *
    * @param encodedPassword 编码后的密码
    * @return 是否需要升级
    */
   @Override
   public boolean upgradeEncoding(String encodedPassword) {
      return this.getPasswordEncoder().upgradeEncoding(encodedPassword);
   }

   @Override
   public String toString() {
      return this.getPasswordEncoder().toString();
   }

   /**
    * 获取密码编码器
    *
    * @return 密码编码器
    */
   private PasswordEncoder getPasswordEncoder() {
      if (this.passwordEncoder != null) {
         return this.passwordEncoder;
      } else {
         PasswordEncoder encoder;
         if ((encoder = this.getBean(PasswordEncoder.class)) == null) {
            encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
         }

         this.passwordEncoder = encoder;
         return encoder;
      }
   }

   /**
    * 构造函数
    *
    * @param applicationContext 应用上下文
    */
   public LazyPasswordEncoder(ApplicationContext applicationContext) {
      this.applicationContext = applicationContext;
   }

   /**
    * 编码密码
    *
    * @param rawPassword 原始密码
    * @return 编码后的密码
    */
   @Override
   public String encode(CharSequence rawPassword) {
      return this.getPasswordEncoder().encode(rawPassword);
   }

   /**
    * 获取Bean实例
    *
    * @param clazz Bean类型
    * @param <T> Bean类型
    * @return Bean实例
    */
   private <T> T getBean(Class<T> clazz) {
      try {
         return this.applicationContext.getBean(clazz);
      } catch (NoSuchBeanDefinitionException e) {
         return null;
      }
   }
}