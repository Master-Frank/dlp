package cn.topiam.employee.support.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 密钥无效异常类
 * 当密钥验证失败时抛出此异常
 */
public class SecretInvalidException extends AuthenticationException {
   
   /**
    * 构造函数
    *
    * @param message 异常消息
    */
   public SecretInvalidException(String message) {
      super(message);
   }

   /**
    * 构造函数
    *
    * @param message 异常消息
    * @param cause 异常原因
    */
   public SecretInvalidException(String message, Throwable cause) {
      super(message, cause);
   }
}