package cn.topiam.employee.support.util;

import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;

/**
 * 邮箱工具类
 * 提供邮箱相关的工具方法
 */
public class EmailUtils {
   
   /**
    * 字符串解密方法
    * 
    * @param object 待解密的对象
    * @return 解密后的字符串
    */
   public static String decryptString(Object object) {
      int key1 = 4 << 4 ^ 2 << 2 ^ 5 >> 2;
      int key2 = 5 << 3 ^ 2;
      int key3 = (3 ^ 5) << 4 ^ 2 << 2 ^ 3 & 5;
      String str = (String) object;
      int length = str.length();
      char[] chars = new char[length];
      int index = length - (3 >> 1);
      char[] result = chars;
      int var4 = key3;
      int var5 = index;

      for (int var6 = key2; var5 >= 0; var5 = index) {
         int tempIndex = index;
         int charValue = str.charAt(index);
         --index;
         result[tempIndex] = (char) (charValue ^ var6);
         if (index < 0) {
            break;
         }

         int tempIndex2 = index--;
         result[tempIndex2] = (char) (str.charAt(tempIndex2) ^ var4);
      }

      return new String(result);
   }

   /**
    * 验证邮箱格式是否正确
    *
    * @param email 邮箱地址
    * @return 邮箱格式是否正确
    */
   public static boolean isEmailValidate(String email) {
      return (new EmailValidator()).isValid(email, (ConstraintValidatorContext) null);
   }
}