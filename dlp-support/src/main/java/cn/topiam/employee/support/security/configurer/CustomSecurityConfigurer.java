package cn.topiam.employee.support.security.configurer;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.context.SecurityContextHolderFilter;

/**
 * 自定义安全配置器
 * 用于向Spring Security配置中添加自定义过滤器
 *
 * @param <H> HttpSecurityBuilder的子类
 */
public final class CustomSecurityConfigurer<H extends HttpSecurityBuilder<H>> extends AbstractHttpConfigurer<CustomSecurityConfigurer<H>, H> {
   
   /**
    * 配置方法，在此处添加自定义过滤器
    * 
    * @param builder HttpSecurity构建器
    * @throws Exception 配置过程中可能抛出的异常
    */
   public void configure(H builder) throws Exception {
      CustomRedirectFilter filter = new CustomRedirectFilter();
      builder.addFilterBefore(filter, SecurityContextHolderFilter.class);
   }
}