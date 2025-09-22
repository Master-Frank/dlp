package cn.topiam.employee.support.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * 自定义枚举转换器工厂
 * 用于将字符串转换为枚举类型
 */
public class CustomEnumConverterFactory implements ConverterFactory<String, Enum> {
   
   /**
    * 获取转换器
    * 
    * @param targetType 目标类型
    * @param <T> 枚举类型
    * @return 转换器
    */
   @Override
   public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
      return new CustomEnumConverter(targetType);
   }
}