package cn.topiam.employee.support.redis;

import java.nio.charset.StandardCharsets;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;

/**
 * 键字符串Redis序列化器
 * 用于Redis键的序列化和反序列化
 */
public final class KeyStringRedisSerializer implements RedisSerializer<String> {
   
   /**
    * Redis键的前缀，用于区分不同的键空间
    */
   private final String keyPrefix;

   /**
    * 序列化字符串值
    * 将字符串值添加键前缀后转换为UTF-8编码的字节数组
    *
    * @param stringValue 需要序列化的字符串值，可能为null
    * @return UTF-8编码的字节数组，如果输入为null则返回null
    */
   @Override
   public byte[] serialize(@Nullable String stringValue) {
      return stringValue == null ? null : (this.keyPrefix + stringValue).getBytes(StandardCharsets.UTF_8);
   }

   /**
    * 比较对象是否相等
    * 使用超类的equals方法进行比较
    *
    * @param object 要比较的对象
    * @return 如果对象相等返回true，否则返回false
    */
   @Override
   public final boolean equals(Object object) {
      return super.equals(object);
   }

   /**
    * 反序列化字节数组
    * 将字节数组解码为UTF-8字符串，并去除键前缀
    *
    * @param byteValue 需要反序列化的字节数组，可能为null
    * @return 去除键前缀后的字符串值，如果输入为null则返回null
    */
   @Override
   public String deserialize(@Nullable byte[] byteValue) {
      return byteValue == null ? null : (new String(byteValue, StandardCharsets.UTF_8)).replaceFirst(this.keyPrefix, "");
   }

   /**
    * 构造函数
    * 创建一个新的KeyStringRedisSerializer实例，并添加冒号作为前缀的一部分
    *
    * @param prefix Redis键的前缀
    */
   public KeyStringRedisSerializer(String prefix) {
      this.keyPrefix = prefix + ":";
   }

   /**
    * 返回对象的字符串表示形式
    * 使用超类的toString方法
    *
    * @return 对象的字符串表示形式
    */
   @Override
   public final String toString() {
      return super.toString();
   }

   /**
    * 计算对象的哈希码
    * 使用超类的hashCode方法
    *
    * @return 对象的哈希码值
    */
   @Override
   public final int hashCode() {
      return super.hashCode();
   }

   /**
    * 获取配置的键前缀
    *
    * @return 返回配置的键前缀
    */
   public String getKeyPrefix() {
      return this.keyPrefix;
   }
}