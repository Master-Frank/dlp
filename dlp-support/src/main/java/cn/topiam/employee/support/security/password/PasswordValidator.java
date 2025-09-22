package cn.topiam.employee.support.security.password;

import cn.topiam.employee.support.security.password.exception.PasswordInvalidException;

/**
 * 密码验证器接口
 * 定义密码验证的方法
 */
public interface PasswordValidator {
   
   /**
    * 验证密码
    *
    * @param password 密码
    * @throws PasswordInvalidException 密码无效异常
    */
   void validate(String password) throws PasswordInvalidException;
}