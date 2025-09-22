package cn.topiam.employee.support.security.password.enums;

import cn.topiam.employee.support.enums.BaseEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 密码复杂度规则枚举
 * 定义密码复杂度的各种规则
 */
public enum PasswordComplexityRule implements BaseEnum {
   /**
    * 无要求
    */
   NONE("NONE", "无要求"),
   
   /**
    * 必须包含数字和字母
    */
   MUST_NUMBERS_AND_LETTERS("MUST_NUMBERS_AND_LETTERS", "必须包含数字和字母"),
   
   /**
    * 必须包含数字和大写字母
    */
   MUST_NUMBERS_AND_CAPITAL_LETTERS("MUST_NUMBERS_AND_CAPITAL_LETTERS", "必须包含数字和大写字母"),
   
   /**
    * 必须包含数字、大写字母、小写字母和特殊字符
    */
   MUST_CONTAIN_NUMBERS_UPPERCASE_LETTERS_LOWERCASE_LETTERS_AND_SPECIAL_CHARACTERS(
           "MUST_CONTAIN_NUMBERS_UPPERCASE_LETTERS_LOWERCASE_LETTERS_AND_SPECIAL_CHARACTERS", 
           "必须包含数字、大写字母、小写字母和特殊字符"),
   
   /**
    * 至少包含数字、字母和特殊字符中的两种
    */
   CONTAIN_AT_LEAST_TWO_OF_NUMBERS_LETTERS_AND_SPECIAL_CHARACTERS(
           "CONTAIN_AT_LEAST_TWO_OF_NUMBERS_LETTERS_AND_SPECIAL_CHARACTERS", 
           "至少包含数字、字母和特殊字符中的两种"),
   
   /**
    * 至少包含数字、大写字母、小写字母和特殊字符中的三种
    */
   CONTAIN_AT_LEAST_THREE_OF_NUMBERS_UPPERCASE_LETTERS_LOWERCASE_LETTERS_AND_SPECIAL_CHARACTERS(
           "CONTAIN_AT_LEAST_THREE_OF_NUMBERS_UPPERCASE_LETTERS_LOWERCASE_LETTERS_AND_SPECIAL_CHARACTERS", 
           "至少包含数字、大写字母、小写字母和特殊字符中的三种");

   /**
    * 代码
    */
   private final String code;
   
   /**
    * 描述
    */
   private final String desc;

   /**
    * 构造函数
    *
    * @param code 代码
    * @param desc 描述
    */
   PasswordComplexityRule(String code, String desc) {
      this.code = code;
      this.desc = desc;
   }

   /**
    * 获取描述
    *
    * @return 描述
    */
   @Override
   public String getDesc() {
      return this.desc;
   }

   /**
    * 获取代码
    *
    * @return 代码
    */
   @JsonValue
   @Override
   public String getCode() {
      return this.code;
   }

   /**
    * 根据代码获取类型
    *
    * @param code 代码
    * @return 密码复杂度规则
    */
   public static PasswordComplexityRule getType(String code) {
      for (PasswordComplexityRule rule : values()) {
         if (String.valueOf(rule.getCode()).equals(code)) {
            return rule;
         }
      }
      throw new NullPointerException("未找到匹配的密码复杂度规则");
   }

   @Override
   public String toString() {
      return this.code;
   }
}