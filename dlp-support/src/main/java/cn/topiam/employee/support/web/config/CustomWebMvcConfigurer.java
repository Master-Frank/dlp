package cn.topiam.employee.support.web.config;

import cn.topiam.employee.support.util.PhoneUtils;
import cn.topiam.employee.support.util.VersionUtils;
import cn.topiam.employee.support.web.converter.CustomEnumConverterFactory;
import cn.topiam.employee.support.web.filter.VersionHeaderFilter;
import cn.topiam.employee.support.web.useragent.DefaultUserAgentParser;
import cn.topiam.employee.support.web.useragent.UserAgentParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 自定义Web MVC配置器
 * 用于配置Web相关的组件和过滤器
 */
public class CustomWebMvcConfigurer implements WebMvcConfigurer {
   
   /**
    * 当前版本过滤器注册Bean
    * 
    * @return 过滤器注册Bean
    */
   @Bean
   public FilterRegistrationBean<VersionHeaderFilter> currentVersionFilterRegistration() {
      FilterRegistrationBean<VersionHeaderFilter> filterRegistration = new FilterRegistrationBean<>();
      filterRegistration.setFilter(new VersionHeaderFilter());
      String[] urlPatterns = new String[1];
      urlPatterns[0] = VersionUtils.decryptString("\u0007z");
      filterRegistration.addUrlPatterns(urlPatterns);
      filterRegistration.setName(PhoneUtils.decryptString("\u0001g0`'|6D'`1{-|\u0004{.f'`"));
      filterRegistration.setOrder(-100);
      return filterRegistration;
   }

   /**
    * 用户代理解析器Bean
    * 
    * @return 用户代理解析器
    */
   @Bean
   public UserAgentParser userAgentParser() {
      return new DefaultUserAgentParser();
   }

   /**
    * 添加格式化器
    * 
    * @param registry 格式化器注册表
    */
   public void addFormatters(FormatterRegistry registry) {
      registry.addConverterFactory(new CustomEnumConverterFactory());
   }
}