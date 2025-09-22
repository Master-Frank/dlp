package cn.topiam.employee.support.security.password.exception;

import cn.topiam.employee.support.exception.TopIamException;
import org.springframework.http.HttpStatus;

/**
 * 密码无效异常
 * 密码相关异常的基类
 */
public class PasswordInvalidException extends TopIamException {
   
   /**
    * 构造函数
    *
    * @param message 消息
    * @param cause 原因
    */
   public PasswordInvalidException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    */
   public PasswordInvalidException(String message) {
      super(message, HttpStatus.BAD_REQUEST);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    * @param status HTTP状态码
    */
   public PasswordInvalidException(String message, HttpStatus status) {
      super("密码验证失败", message, status);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    * @param cause 原因
    * @param code 代码
    * @param status HTTP状态码
    */
   public PasswordInvalidException(String message, Throwable cause, String code, HttpStatus status) {
      super(message, cause, code, status);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    * @param code 代码
    * @param status HTTP状态码
    */
   public PasswordInvalidException(String message, String code, HttpStatus status) {
      super(message, code, status);
   }
}
