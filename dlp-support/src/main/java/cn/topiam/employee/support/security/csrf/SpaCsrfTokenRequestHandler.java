package cn.topiam.employee.support.security.csrf;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.function.Supplier;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.util.StringUtils;

/**
 * SPA CSRF令牌请求处理器
 * 用于处理单页应用的CSRF令牌请求
 */
public final class SpaCsrfTokenRequestHandler extends CsrfTokenRequestAttributeHandler {
   /**
    * CSRF令牌请求处理器
    */
   private final CsrfTokenRequestHandler delegate = new XorCsrfTokenRequestAttributeHandler();

   /**
    * 处理CSRF令牌请求
    *
    * @param request HTTP请求
    * @param response HTTP响应
    * @param csrfTokenSupplier CSRF令牌提供者
    */
   @Override
   public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfTokenSupplier) {
      this.delegate.handle(request, response, csrfTokenSupplier);
   }

   /**
    * 解析CSRF令牌值
    *
    * @param request HTTP请求
    * @param csrfToken CSRF令牌
    * @return CSRF令牌值
    */
   @Override
   public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
      // 如果请求头中有CSRF令牌，则使用父类方法解析，否则使用委托处理器解析
      return StringUtils.hasText(request.getHeader(csrfToken.getHeaderName())) ? 
             super.resolveCsrfTokenValue(request, csrfToken) : 
             this.delegate.resolveCsrfTokenValue(request, csrfToken);
   }
}