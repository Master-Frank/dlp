package cn.topiam.employee.support.security.authentication;

import cn.topiam.employee.support.geo.GeoLocation;
import cn.topiam.employee.support.web.useragent.UserAgent;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Web认证详情类
 * 包含认证相关的详细信息
 */
public class WebAuthenticationDetails extends org.springframework.security.web.authentication.WebAuthenticationDetails {
   /**
    * 地理位置信息
    */
   private final GeoLocation geoLocation;
   
   /**
    * 认证时间
    */
   private final LocalDateTime authenticationTime;
   
   /**
    * 用户代理信息
    */
   private final UserAgent userAgent;
   
   /**
    * 认证提供者
    */
   private AuthenticationProvider authenticationProvider;

   /**
    * 获取认证时间
    *
    * @return 认证时间
    */
   @JsonProperty("authenticationTime")
   @JsonFormat(
      pattern = "yyyy-MM-dd HH:mm:ss"
   )
   public LocalDateTime getAuthenticationTime() {
      return this.authenticationTime;
   }

   /**
    * 获取认证提供者
    *
    * @return 认证提供者
    */
   @JsonProperty("authenticationProvider")
   public AuthenticationProvider getAuthenticationProvider() {
      return this.authenticationProvider;
   }

   /**
    * 获取用户代理信息
    *
    * @return 用户代理信息
    */
   @JsonProperty("userAgent")
   public UserAgent getUserAgent() {
      return this.userAgent;
   }

   /**
    * 构造函数
    *
    * @param request HTTP请求
    * @param userAgent 用户代理
    * @param geoLocation 地理位置
    */
   public WebAuthenticationDetails(HttpServletRequest request, UserAgent userAgent, GeoLocation geoLocation) {
      super(request);
      this.userAgent = userAgent;
      this.geoLocation = geoLocation;
      this.authenticationTime = LocalDateTime.now();
   }

   /**
    * 获取地理位置信息
    *
    * @return 地理位置信息
    */
   @JsonProperty("geoLocation")
   public GeoLocation getGeoLocation() {
      return this.geoLocation;
   }

   /**
    * 构造函数
    *
    * @param remoteAddress 远程地址
    * @param sessionId 会话ID
    * @param userAgent 用户代理
    * @param geoLocation 地理位置
    * @param authenticationProvider 认证提供者
    * @param authenticationTime 认证时间
    */
   public WebAuthenticationDetails(String remoteAddress, String sessionId, UserAgent userAgent, GeoLocation geoLocation, AuthenticationProvider authenticationProvider, LocalDateTime authenticationTime) {
      super(remoteAddress, sessionId);
      this.userAgent = userAgent;
      this.geoLocation = geoLocation;
      this.authenticationProvider = authenticationProvider;
      this.authenticationTime = authenticationTime;
   }

   /**
    * 设置认证提供者
    *
    * @param authenticationProvider 认证提供者
    */
   public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
      this.authenticationProvider = authenticationProvider;
   }
}
