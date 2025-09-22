package cn.topiam.employee.support.security.password.exception;

import org.springframework.http.HttpStatus;

/**
 * 密码历史无效异常
 * 当新密码与历史密码重复时抛出此异常
 */
public class PasswordHistoryInvalidException extends PasswordInvalidException {
   
   /**
    * 构造函数
    *
    * @param message 消息
    * @param cause 原因
    */
   public PasswordHistoryInvalidException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    * @param code 代码
    * @param status HTTP状态码
    */
   public PasswordHistoryInvalidException(String message, String code, HttpStatus status) {
      super(message, code, status);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    * @param status HTTP状态码
    */
   public PasswordHistoryInvalidException(String message, HttpStatus status) {
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
   public PasswordHistoryInvalidException(String message, Throwable cause, String code, HttpStatus status) {
      super(message, cause, code, status);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    */
   public PasswordHistoryInvalidException(String message) {
      super(message, HttpStatus.BAD_REQUEST);
   }
}