package cn.topiam.employee.support.security.configurer;

import cn.topiam.employee.support.security.authentication.WebAuthenticationDetailsSource;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 表单登录配置器
 * 用于配置表单登录相关的安全设置
 *
 * @param <H> HttpSecurityBuilder类型
 */
public class FormLoginConfigurer<H extends HttpSecurityBuilder<H>> extends AbstractAuthenticationFilterConfigurer<H, FormLoginConfigurer<H>, Object> {
   
   /**
    * 初始化配置
    *
    * @param builder HttpSecurityBuilder
    * @throws Exception 异常
    */
   @Override
   public void init(H builder) throws Exception {
      builder.addFilterBefore(this.getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
      this.authenticationDetailsSource(getAuthenticationDetailsSource(builder));
      super.init(builder);
   }

   /**
    * 创建登录处理URL匹配器
    *
    * @param loginProcessingUrl 登录处理URL
    * @return 请求匹配器
    */
   @Override
   public RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
      return new AntPathRequestMatcher(loginProcessingUrl, "POST");
   }

   /**
    * 获取认证详情源
    *
    * @param builder HttpSecurityBuilder
    * @param <B> HttpSecurityBuilder类型
    * @return WebAuthenticationDetailsSource
    */
   public static <B extends HttpSecurityBuilder<B>> WebAuthenticationDetailsSource getAuthenticationDetailsSource(B builder) {
      WebAuthenticationDetailsSource detailsSource;
      if ((detailsSource = builder.getSharedObject(WebAuthenticationDetailsSource.class)) == null) {
         detailsSource = getBean(builder, WebAuthenticationDetailsSource.class);
         builder.setSharedObject(WebAuthenticationDetailsSource.class, detailsSource);
      }

      return detailsSource;
   }

   /**
    * 获取Bean实例
    *
    * @param builder HttpSecurityBuilder
    * @param clazz Bean类型
    * @param <B> HttpSecurityBuilder类型
    * @param <T> Bean类型
    * @return Bean实例
    */
   public static <B extends HttpSecurityBuilder<B>, T> T getBean(B builder, Class<T> clazz) {
      return builder.getSharedObject(ApplicationContext.class).getBean(clazz);
   }

   /**
    * 默认构造函数
    */
   public FormLoginConfigurer() {
      super(new Object(), "/api/v1/login");
   }
}