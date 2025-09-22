package cn.topiam.employee.support.web.servlet;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 可重复请求过滤器类
 * 用于包装请求，使其可以被多次读取
 */
public class RepeatableRequestFilter extends OncePerRequestFilter {
   
   /**
    * 执行过滤
    *
    * @param httpServletRequest HTTP请求
    * @param httpServletResponse HTTP响应
    * @param filterChain 过滤器链
    * @throws ServletException Servlet异常
    * @throws IOException IO异常
    */
   @Override
   public void doFilterInternal(@NonNull HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse, @NotNull FilterChain filterChain) throws ServletException, IOException {
      RepeatedlyRequestWrapper requestWrapper = null;
      if (StringUtils.startsWithIgnoreCase(httpServletRequest.getContentType(), "application/json")) {
         requestWrapper = new RepeatedlyRequestWrapper(httpServletRequest, httpServletResponse);
      }

      filterChain.doFilter((ServletRequest) Objects.requireNonNullElse(requestWrapper, httpServletRequest), httpServletResponse);
   }
}