package cn.topiam.employee.support.security.password.exception;

import org.springframework.http.HttpStatus;

/**
 * 密码复杂度规则无效异常
 * 当密码不符合复杂度规则时抛出此异常
 */
public class PasswordComplexityRuleInvalidException extends PasswordInvalidException {
   
   /**
    * 构造函数
    *
    * @param message 消息
    * @param cause 原因
    * @param code 代码
    * @param status HTTP状态码
    */
   public PasswordComplexityRuleInvalidException(String message, Throwable cause, String code, HttpStatus status) {
      super(message, cause, code, status);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    * @param cause 原因
    */
   public PasswordComplexityRuleInvalidException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    * @param code 代码
    * @param status HTTP状态码
    */
   public PasswordComplexityRuleInvalidException(String message, String code, HttpStatus status) {
      super(message, code, status);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    * @param status HTTP状态码
    */
   public PasswordComplexityRuleInvalidException(String message, HttpStatus status) {
      super(message, status);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    */
   public PasswordComplexityRuleInvalidException(String message) {
      super(message, HttpStatus.BAD_REQUEST);
   }
}