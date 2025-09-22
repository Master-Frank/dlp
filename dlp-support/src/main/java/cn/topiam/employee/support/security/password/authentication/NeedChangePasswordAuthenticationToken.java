package cn.topiam.employee.support.security.password.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * 需要更改密码认证令牌类
 * 用于表示需要更改密码的认证令牌
 */
public class NeedChangePasswordAuthenticationToken extends AbstractAuthenticationToken {
   /**
    * 是否验证旧密码
    */
   private final Boolean verifyOldPassword;
   
   /**
    * 认证信息
    */
   private final Authentication first;
   
   private static final long serialVersionUID = -1506897701981698420L;

   /**
    * 获取主体
    *
    * @return 主体
    */
   @Override
   public Object getPrincipal() {
      return this.first.getPrincipal();
   }

   /**
    * 获取凭证
    *
    * @return 凭证
    */
   @Override
   public Object getCredentials() {
      return "";
   }

   /**
    * 获取第一个认证信息
    *
    * @return 认证信息
    */
   @JsonProperty("first")
   public Authentication getFirst() {
      return this.first;
   }

   /**
    * 构造函数
    *
    * @param first 认证信息
    */
   public NeedChangePasswordAuthenticationToken(Authentication first) {
      this(first, Boolean.TRUE);
   }

   /**
    * 构造函数
    *
    * @param first 认证信息
    * @param verifyOldPassword 是否验证旧密码
    */
   public NeedChangePasswordAuthenticationToken(Authentication first, Boolean verifyOldPassword) {
      super(Collections.emptyList());
      this.first = first;
      this.verifyOldPassword = verifyOldPassword;
      this.setAuthenticated(false);
      this.setDetails(first.getDetails());
   }

   /**
    * 获取是否验证旧密码
    *
    * @return 是否验证旧密码
    */
   @JsonProperty("verifyOldPassword")
   public Boolean getVerifyOldPassword() {
      return this.verifyOldPassword;
   }
}