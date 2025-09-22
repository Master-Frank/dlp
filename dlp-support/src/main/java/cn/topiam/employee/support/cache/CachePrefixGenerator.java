package cn.topiam.employee.support.cache;

import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.lang.NonNull;

/**
 * 缓存前缀生成器
 * 用于生成Redis缓存键的前缀
 */
public class CachePrefixGenerator implements CacheKeyPrefix {
   
   /**
    * 前缀
    */
   private final String prefix;

   /**
    * 计算缓存键前缀
    * 
    * @param cacheName 缓存名称
    * @return 完整的缓存键前缀
    */
   @NonNull
   public String compute(@NonNull String cacheName) {
      return this.prefix + ":" + cacheName + ":";
   }

   /**
    * 构造函数
    * 
    * @param prefix 前缀
    */
   public CachePrefixGenerator(String prefix) {
      this.prefix = prefix;
   }
}