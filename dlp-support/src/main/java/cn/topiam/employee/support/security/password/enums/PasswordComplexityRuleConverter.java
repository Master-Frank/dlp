package cn.topiam.employee.support.security.password.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Objects;

/**
 * 密码复杂度规则转换器
 * 用于JPA实体与数据库之间的类型转换
 */
@Converter(autoApply = true)
public class PasswordComplexityRuleConverter implements AttributeConverter<PasswordComplexityRule, String> {

   /**
    * 将实体属性转换为数据库列值
    *
    * @param rule 密码复杂度规则
    * @return 数据库列值
    */
   @Override
   public String convertToDatabaseColumn(PasswordComplexityRule rule) {
      return Objects.isNull(rule) ? null : rule.getCode();
   }

   /**
    * 将数据库列值转换为实体属性
    *
    * @param code 数据库列值
    * @return 密码复杂度规则
    */
   @Override
   public PasswordComplexityRule convertToEntityAttribute(String code) {
      return PasswordComplexityRule.getType(code);
   }
}
