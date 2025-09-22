package cn.topiam.employee.support.security.password.exception;

import org.springframework.http.HttpStatus;

/**
 * 弱密码无效异常
 * 当密码被认为是弱密码时抛出此异常
 */
public class PasswordWeakInvalidException extends PasswordInvalidException {
   
   /**
    * 构造函数
    *
    * @param message 消息
    * @param cause 原因
    */
   public PasswordWeakInvalidException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    * @param cause 原因
    * @param code 代码
    * @param status HTTP状态码
    */
   public PasswordWeakInvalidException(String message, Throwable cause, String code, HttpStatus status) {
      super(message, cause, code, status);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    */
   public PasswordWeakInvalidException(String message) {
      super(message, HttpStatus.BAD_REQUEST);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    * @param status HTTP状态码
    */
   public PasswordWeakInvalidException(String message, HttpStatus status) {
      super(message, status);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    * @param code 代码
    * @param status HTTP状态码
    */
   public PasswordWeakInvalidException(String message, String code, HttpStatus status) {
      super(message, code, status);
   }
}