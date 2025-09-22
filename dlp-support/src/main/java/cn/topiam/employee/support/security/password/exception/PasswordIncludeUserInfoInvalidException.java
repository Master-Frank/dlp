package cn.topiam.employee.support.security.password.exception;

import org.springframework.http.HttpStatus;

/**
 * 密码包含用户信息无效异常
 * 当密码包含用户个人信息时抛出此异常
 */
public class PasswordIncludeUserInfoInvalidException extends PasswordInvalidException {
   
   /**
    * 构造函数
    *
    * @param message 消息
    * @param code 代码
    * @param status HTTP状态码
    */
   public PasswordIncludeUserInfoInvalidException(String message, String code, HttpStatus status) {
      super(message, code, status);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    */
   public PasswordIncludeUserInfoInvalidException(String message) {
      super(message, HttpStatus.BAD_REQUEST);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    * @param cause 原因
    * @param code 代码
    * @param status HTTP状态码
    */
   public PasswordIncludeUserInfoInvalidException(String message, Throwable cause, String code, HttpStatus status) {
      super(message, cause, code, status);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    * @param status HTTP状态码
    */
   public PasswordIncludeUserInfoInvalidException(String message, HttpStatus status) {
      super(message, status);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    * @param cause 原因
    */
   public PasswordIncludeUserInfoInvalidException(String message, Throwable cause) {
      super(message, cause);
   }
}