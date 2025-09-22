package cn.topiam.employee.support.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * 自定义本地化配置类
 * 用于配置国际化相关的组件
 */
public class CustomLocaleConfiguration implements WebMvcConfigurer {
   
   /**
    * 语言参数名称
    */
   public static final String LANGUAGE_NAME = "topiam_language";

   /**
    * 添加拦截器
    * 
    * @param registry 拦截器注册表
    */
   public void addInterceptors(InterceptorRegistry registry) {
      LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
      localeChangeInterceptor.setParamName("topiam_language");
      registry.addInterceptor(localeChangeInterceptor);
   }

   /**
    * 本地化解析器Bean
    * 
    * @return 本地化解析器
    */
   @Bean
   public LocaleResolver localeResolver() {
      return new CookieLocaleResolver("topiam_language");
   }
}