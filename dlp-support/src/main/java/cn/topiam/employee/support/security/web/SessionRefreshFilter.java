package cn.topiam.employee.support.security.web;

import cn.topiam.employee.support.security.session.RefreshCurrentSessionPrincipalService;
import cn.topiam.employee.support.security.userdetails.UserDetails;
import cn.topiam.employee.support.security.util.SecurityUtils;
import cn.topiam.employee.support.util.BeanUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;
import lombok.Generated;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.GenericFilterBean;

/**
 * 会话刷新过滤器
 * 用于刷新当前会话中的用户主体信息
 */
public class SessionRefreshFilter extends GenericFilterBean {
   
   /**
    * 安全上下文仓库
    */
   private SecurityContextRepository securityContextRepository;
   
   /**
    * 刷新当前会话主体服务
    */
   private final RefreshCurrentSessionPrincipalService refreshCurrentSessionPrincipalService;
   
   /**
    * 构造函数
    * 
    * @param refreshCurrentSessionPrincipalService 刷新当前会话主体服务
    */
   public SessionRefreshFilter(RefreshCurrentSessionPrincipalService refreshCurrentSessionPrincipalService) {
      this.refreshCurrentSessionPrincipalService = refreshCurrentSessionPrincipalService;
   }
   
   /**
    * 过滤方法
    * 
    * @param servletRequest Servlet请求
    * @param servletResponse Servlet响应
    * @param filterChain 过滤器链
    * @throws IOException IO异常
    * @throws ServletException Servlet异常
    */
   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
      HttpServletRequest request = (HttpServletRequest) servletRequest;
      HttpServletResponse response = (HttpServletResponse) servletResponse;
      SecurityContext context = this.securityContextRepository.loadContext(request);
      if (Objects.nonNull(context.getAuthentication()) && context.getAuthentication().isAuthenticated()) {
         Object principal = context.getAuthentication().getPrincipal();
         if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            String userId = userDetails.getId();
            UserDetails latestUserDetails = this.refreshCurrentSessionPrincipalService.refresh(userId);
            if (Objects.nonNull(latestUserDetails)) {
               HttpSession session = request.getSession(false);
               if (Objects.nonNull(session)) {
                  SecurityUtils.updateSecurityContext(latestUserDetails, session.getId());
               }
            }
         }
      }
      
      filterChain.doFilter(request, response);
   }
   
   /**
    * 设置安全上下文仓库
    * 
    * @param securityContextRepository 安全上下文仓库
    */
   public void setSecurityContextRepository(SecurityContextRepository securityContextRepository) {
      this.securityContextRepository = securityContextRepository;
   }
}