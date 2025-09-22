package cn.topiam.employee.support.security.password;

/**
 * 密码生成器接口
 * 定义密码生成的方法
 */
public interface PasswordGenerator {
   
   /**
    * 生成密码
    *
    * @return 生成的密码
    */
   String generatePassword();
}