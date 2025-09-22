package cn.topiam.employee.support.web.decrypt;

import cn.topiam.employee.support.exception.TopIamException;
import org.springframework.http.HttpStatus;

/**
 * 加密密钥不存在异常类
 * 用于处理加密密钥不存在的异常情况
 */
public class EncryptSecretNotExistException extends TopIamException {
   
   /**
    * 构造函数，初始化异常信息
    */
   public EncryptSecretNotExistException() {
      super("ENCRYPT_SECRET_NOT_EXIST", "加密密钥不存在", HttpStatus.BAD_REQUEST);
   }
}
