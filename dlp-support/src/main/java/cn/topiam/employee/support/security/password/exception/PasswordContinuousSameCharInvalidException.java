package cn.topiam.employee.support.security.password.exception;

import org.springframework.http.HttpStatus;

/**
 * 密码连续相同字符无效异常
 * 当密码包含过多连续相同字符时抛出此异常
 */
public class PasswordContinuousSameCharInvalidException extends PasswordInvalidException {
   
   /**
    * 构造函数
    *
    * @param message 消息
    * @param status HTTP状态码
    */
   public PasswordContinuousSameCharInvalidException(String message, HttpStatus status) {
      super(message, status);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    * @param cause 原因
    * @param code 代码
    * @param status HTTP状态码
    */
   public PasswordContinuousSameCharInvalidException(String message, Throwable cause, String code, HttpStatus status) {
      super(message, cause, code, status);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    * @param cause 原因
    */
   public PasswordContinuousSameCharInvalidException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    * @param code 代码
    * @param status HTTP状态码
    */
   public PasswordContinuousSameCharInvalidException(String message, String code, HttpStatus status) {
      super(message, code, status);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    */
   public PasswordContinuousSameCharInvalidException(String message) {
      super(message, HttpStatus.BAD_REQUEST);
   }
}