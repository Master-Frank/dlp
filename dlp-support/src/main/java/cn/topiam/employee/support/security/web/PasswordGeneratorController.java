package cn.topiam.employee.support.security.web;

import cn.topiam.employee.support.result.ApiRestResult;
import cn.topiam.employee.support.security.password.PasswordGenerator;
import cn.topiam.employee.support.util.PhoneUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 密码生成器控制器
 * 用于提供密码生成相关接口
 */
@RestController
public class PasswordGeneratorController {
   
   /**
    * 密码生成器
    */
   private final PasswordGenerator passwordGenerator;
   
   /**
    * 字符串解密方法
    * 
    * @param object 待解密的对象
    * @return 解密后的字符串
    */
   public static String decryptString(Object object) {
      String str = (String) object;
      char[] chars = new char[str.length()];
      
      for (int i = str.length() - 1; i >= 0; i--) {
         if (i % 2 == 0) {
            chars[i] = (char) (str.charAt(i) ^ (str.length() - 1));
         } else {
            chars[i] = (char) (str.charAt(i) ^ 5);
         }
      }
      
      return new String(chars);
   }

   /**
    * 生成随机密码
    * 
    * @return 随机密码结果
    */
   @GetMapping({"/api/v1/random_password"})
   public ApiRestResult<String> randomPassword() {
      return ApiRestResult.ok(this.passwordGenerator.generatePassword());
   }

   /**
    * 构造函数
    * 
    * @param passwordGenerator 密码生成器
    */
   public PasswordGeneratorController(PasswordGenerator passwordGenerator) {
      this.passwordGenerator = passwordGenerator;
   }
}