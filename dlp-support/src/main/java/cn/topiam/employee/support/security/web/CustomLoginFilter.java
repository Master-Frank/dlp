package cn.topiam.employee.support.security.web;

import cn.topiam.employee.support.enums.SecretType;
import cn.topiam.employee.support.repository.page.domain.ExampleRequest;
import cn.topiam.employee.support.repository.page.domain.Page;
import cn.topiam.employee.support.security.exception.SecretInvalidException;
import cn.topiam.employee.support.util.AesUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 自定义登录过滤器
 * 处理用户登录请求，支持加密密码验证
 */
public class CustomLoginFilter extends AbstractAuthenticationProcessingFilter {
   
   /**
    * 表单用户名字段名
    */
   public static final String SECURITY_FORM_USERNAME_KEY = "username";
   
   /**
    * 登录请求匹配器
    */
   private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login", "POST");
   
   /**
    * 用户名
    */
   private String usernameParameter = SECURITY_FORM_USERNAME_KEY;
   
   /**
    * 密码
    */
   private String passwordParameter = "password";
   
   /**
    * 是否只处理POST请求
    */
   private boolean postOnly = true;
   
   /**
    * 构造函数
    */
   public CustomLoginFilter() {
      super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
   }
   
   /**
    * 构造函数
    * 
    * @param authenticationManager 认证管理器
    */
   public CustomLoginFilter(AuthenticationManager authenticationManager) {
      super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
   }
   
   /**
    * 尝试认证
    * 
    * @param request HTTP请求
    * @param response HTTP响应
    * @return 认证对象
    * @throws AuthenticationException 认证异常
    */
   public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
      if (this.postOnly && !request.getMethod().equals("POST")) {
         throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
      } else {
         String username = this.obtainUsername(request);
         String password = this.obtainPassword(request);
         String decryptUsername = this.decryptString(username);
         String decryptPassword = this.decryptString(password);
         
         if (username == null) {
            username = "";
         }
         
         if (password == null) {
            password = "";
         }
         
         username = username.trim();
         CustomLoginFilter token = new CustomLoginFilter(decryptUsername, decryptPassword);
         this.setDetails(request, token);
         return this.getAuthenticationManager().authenticate(token);
      }
   }
   
   /**
    * 获取用户名
    * 
    * @param request HTTP请求
    * @return 用户名
    */
   protected String obtainUsername(HttpServletRequest request) {
      return request.getParameter(this.usernameParameter);
   }
   
   /**
    * 获取密码
    * 
    * @param request HTTP请求
    * @return 密码
    */
   protected String obtainPassword(HttpServletRequest request) {
      return request.getParameter(this.passwordParameter);
   }
   
   /**
    * 解密字符串
    * 
    * @param str 待解密字符串
    * @return 解密后的字符串
    */
   private String decryptString(String str) {
      try {
         return AesUtils.decrypt(str, SecretType.LOGIN_ENCRYPT_SECRET.getSecret());
      } catch (Exception e) {
         throw new SecretInvalidException("Decrypt failed", e);
      }
   }
   
   /**
    * 设置详细信息
    * 
    * @param request HTTP请求
    * @param authRequest 认证请求
    */
   protected void setDetails(HttpServletRequest request, CustomLoginFilter authRequest) {
      authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
   }
   
   /**
    * 设置用户名参数
    * 
    * @param usernameParameter 用户名参数
    */
   public void setUsernameParameter(String usernameParameter) {
      this.usernameParameter = usernameParameter;
   }
   
   /**
    * 设置密码参数
    * 
    * @param passwordParameter 密码参数
    */
   public void setPasswordParameter(String passwordParameter) {
      this.passwordParameter = passwordParameter;
   }
   
   /**
    * 设置是否只处理POST请求
    * 
    * @param postOnly 是否只处理POST请求
    */
   public void setPostOnly(boolean postOnly) {
      this.postOnly = postOnly;
   }
   
   /**
    * 获取用户名参数
    * 
    * @return 用户名参数
    */
   public final String getUsernameParameter() {
      return this.usernameParameter;
   }
   
   /**
    * 获取密码参数
    * 
    * @return 密码参数
    */
   public final String getPasswordParameter() {
      return this.passwordParameter;
   }
   
   /**
    * 构造函数
    * 
    * @param username 用户名
    * @param password 密码
    */
   public CustomLoginFilter(String username, String password) {
      super(new UsernamePasswordAuthenticationToken(username, password));
   }
   
   /**
    * 获取凭证
    * 
    * @return 凭证
    */
   public Object getCredentials() {
      return null;
   }
   
   /**
    * 获取主体
    * 
    * @return 主体
    */
   public Object getPrincipal() {
      return null;
   }
}