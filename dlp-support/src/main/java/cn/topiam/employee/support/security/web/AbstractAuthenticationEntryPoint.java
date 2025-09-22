package cn.topiam.employee.support.security.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import cn.topiam.employee.support.web.useragent.UserAgentParser;

/**
 * 抽象认证入口点类
 * 实现AuthenticationEntryPoint接口，处理未认证用户的访问请求
 */
public abstract class AbstractAuthenticationEntryPoint implements AuthenticationEntryPoint {
   /**
    * 用户代理解析器
    */
   private final UserAgentParser userAgentParser;
   
   /**
    * 日志记录器
    */
   private final Logger logger;

   /**
    * 构造函数
    *
    * @param userAgentParser 用户代理解析器
    */
   public AbstractAuthenticationEntryPoint(UserAgentParser userAgentParser) {
      this.logger = LoggerFactory.getLogger(this.getClass());
      this.userAgentParser = userAgentParser;
   }

   /**
    * 处理未认证用户的访问请求
    *
    * @param request HTTP请求
    * @param response HTTP响应
    * @param authException 认证异常
    * @throws IOException IO异常
    * @throws ServletException Servlet异常
    */
   @Override
   public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
      response.setStatus(401);
      String requestBody = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8).replaceAll("\\s+", "");
      
      cn.topiam.employee.support.web.logger.LoggingAspect$Log log = new cn.topiam.employee.support.web.logger.LoggingAspect$Log();
      log.setRequestUrl(request.getRequestURL().toString());
      log.setHttpType(request.getMethod());
      log.setBody(requestBody);
      log.setHeaders(cn.topiam.employee.support.util.HttpRequestUtils.getRequestHeaders(request));
      log.setIp(cn.topiam.employee.support.util.IpUtils.getIpAddr(request));
      log.setSuccess(Boolean.FALSE);
      log.setResult(authException.getMessage());
      log.setUserAgent(this.userAgentParser.getUserAgent(request));
      this.logger.error(log.toString());
   }
}