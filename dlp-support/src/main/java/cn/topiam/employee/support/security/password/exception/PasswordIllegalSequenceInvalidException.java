package cn.topiam.employee.support.security.password.exception;

import org.springframework.http.HttpStatus;

/**
 * 密码非法序列无效异常
 * 当密码包含非法序列（如连续数字、字母等）时抛出此异常
 */
public class PasswordIllegalSequenceInvalidException extends PasswordInvalidException {
   
   /**
    * 构造函数
    *
    * @param message 消息
    * @param status HTTP状态码
    */
   public PasswordIllegalSequenceInvalidException(String message, HttpStatus status) {
      super(message, status);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    * @param cause 原因
    */
   public PasswordIllegalSequenceInvalidException(String message, Throwable cause) {
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
   public PasswordIllegalSequenceInvalidException(String message, Throwable cause, String code, HttpStatus status) {
      super(message, cause, code, status);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    * @param code 代码
    * @param status HTTP状态码
    */
   public PasswordIllegalSequenceInvalidException(String message, String code, HttpStatus status) {
      super(message, code, status);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    */
   public PasswordIllegalSequenceInvalidException(String message) {
      super(message, HttpStatus.BAD_REQUEST);
   }
}