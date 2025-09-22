package cn.topiam.employee.support.web.filter;

import cn.topiam.employee.support.util.VersionUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 版本头部过滤器
 * 用于在响应中添加版本信息头部
 */
public class VersionHeaderFilter extends OncePerRequestFilter {
   
   /**
    * 版本头部名称
    */
   public static final String VERSION_HEADER_NAME = "X-Application-Version";

   /**
    * 过滤方法
    * 
    * @param request HTTP请求
    * @param response HTTP响应
    * @param filterChain 过滤器链
    * @throws ServletException Servlet异常
    * @throws IOException IO异常
    */
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      response.addHeader(VERSION_HEADER_NAME, VersionUtils.getVersion());
      filterChain.doFilter(request, response);
   }
}