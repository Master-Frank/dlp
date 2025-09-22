package cn.topiam.employee.support.web.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * 枚举转换器
 * 用于将字符串转换为枚举值
 */
public class EnumConverter<T extends Enum<T>> implements Converter<String, T> {
   
   /**
    * 枚举类型
    */
   private final Class<T> enumType;

   /**
    * 构造函数
    * 
    * @param enumType 枚举类型
    */
   public EnumConverter(Class<T> enumType) {
      this.enumType = enumType;
   }

   /**
    * 转换字符串为枚举值
    * 
    * @param source 源字符串
    * @return 枚举值
    */
   @Override
   public T convert(String source) {
      return Enum.valueOf(this.enumType, source.toUpperCase());
   }
}