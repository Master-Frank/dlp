package cn.topiam.employee.support.validation;

import cn.topiam.employee.support.validation.annotation.Phone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * 手机号验证器
 * 用于验证手机号格式是否正确
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {
   
   /**
    * 手机号正则表达式
    */
   public static final String PHONE_REGEX = "^((13[0-9])|(14[5-9])|(15([0-3]|[5-9]))|(16([5,6])|(17[0-8])|(18[0-9]))|(19[1,8,9]))\\d{8}$";
   
   /**
    * 手机号模式
    */
   public Pattern phonePattern;

   /**
    * 初始化验证器
    * 
    * @param phone 手机号注解
    */
   @Override
   public void initialize(Phone phone) {
      this.phonePattern = Pattern.compile(PHONE_REGEX);
   }

   /**
    * 验证手机号是否有效
    * 
    * @param phone 手机号
    * @param context 验证上下文
    * @return 是否有效
    */
   @Override
   public boolean isValid(String phone, ConstraintValidatorContext context) {
      if (StringUtils.isNotBlank(phone)) {
         return this.phonePattern.matcher(phone).matches();
      } else {
         return false;
      }
   }
}