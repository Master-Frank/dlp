package cn.topiam.employee.support.web.converter;

import org.springframework.core.convert.converter.Converter;

/**
 * 枚举转换器包装器
 * 用于包装枚举转换器
 */
public class EnumConverterWrapper<T extends Enum<T>> implements Converter<String, T> {
   
   /**
    * 枚举转换器
    */
   private final EnumConverter<T> converter;

   /**
    * 构造函数
    * 
    * @param converter 枚举转换器
    */
   public EnumConverterWrapper(EnumConverter<T> converter) {
      this.converter = converter;
   }

   /**
    * 转换字符串为枚举值
    * 
    * @param source 源字符串
    * @return 枚举值
    */
   @Override
   public T convert(String source) {
      return this.converter.convert(source);
   }
}