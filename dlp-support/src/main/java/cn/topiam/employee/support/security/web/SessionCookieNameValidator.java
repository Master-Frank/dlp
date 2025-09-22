package cn.topiam.employee.support.security.web;

import cn.topiam.employee.support.util.PhoneUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * 会话Cookie名称验证器
 * 用于验证会话Cookie名称格式是否正确
 */
public class SessionCookieNameValidator implements ConstraintValidator<PhoneUtils, String> {
   
   /**
    * Cookie名称正则表达式
    */
   public static final String COOKIE_NAME_REGEX = "^[a-zA-Z0-9_\\-]+$";
   
   /**
    * Cookie名称模式
    */
   public Pattern cookieNamePattern;

   /**
    * 初始化验证器
    * 
    * @param phoneUtils 注解
    */
   @Override
   public void initialize(PhoneUtils phoneUtils) {
      this.cookieNamePattern = Pattern.compile(COOKIE_NAME_REGEX);
   }

   /**
    * 验证会话Cookie名称
    * 
    * @param value 待验证的值
    * @param context 验证上下文
    * @return 验证结果
    */
   @Override
   public boolean isValid(String value, ConstraintValidatorContext context) {
      if (StringUtils.isBlank(value)) {
         return true;
      } else {
         return this.cookieNamePattern.matcher(value).matches();
      }
   }
}