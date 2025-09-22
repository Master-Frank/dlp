package cn.topiam.employee.support.security.password.exception;

import org.springframework.http.HttpStatus;

/**
 * 密码验证失败异常
 * 当密码验证失败时抛出此异常
 */
public class PasswordValidatedFailException extends PasswordInvalidException {
   
   /**
    * 构造函数
    */
   public PasswordValidatedFailException() {
      super("密码验证失败", "PASSWORD_VALIDATED_FAIL", HttpStatus.BAD_REQUEST);
   }

   /**
    * 构造函数
    *
    * @param message 消息
    */
   public PasswordValidatedFailException(String message) {
      super("密码验证失败", message, HttpStatus.BAD_REQUEST);
   }
}
