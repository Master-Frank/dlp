package cn.topiam.employee.support.security.authentication;

import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 空事件发布器
 * 用于不发布认证事件的场景
 */
public final class NullEventPublisher implements AuthenticationEventPublisher {
   
   /**
    * 发布认证成功事件
    *
    * @param authentication 认证信息
    */
   @Override
   public void publishAuthenticationSuccess(Authentication authentication) {
      // 空实现，不发布事件
   }

   /**
    * 发布认证失败事件
    *
    * @param exception 认证异常
    * @param authentication 认证信息
    */
   @Override
   public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
      // 空实现，不发布事件
   }
}