package cn.topiam.employee.support.cache;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import lombok.Generated;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.lang.NonNull;

/**
 * 自定义键生成器
 * 用于生成缓存键
 */
public class CustomKeyGenerator implements KeyGenerator {
   
   /**
    * 前缀
    */
   private final String prefix;

   /**
    * 生成键前缀
    * 
    * @param gitProperties Git属性
    * @param buildProperties 构建属性
    * @return 键前缀
    */
   private String generatePrefix(GitProperties gitProperties, BuildProperties buildProperties) {
      String commitId = null;
      if (Objects.nonNull(gitProperties)) {
         commitId = gitProperties.getShortCommitId();
      }

      Instant buildTime = null;
      String version = null;
      if (Objects.nonNull(buildProperties)) {
         buildTime = buildProperties.getTime();
         version = buildProperties.getVersion();
      }

      Serializable[] values = new Serializable[4];
      values[0] = commitId;
      values[1] = buildTime;
      values[2] = version;
      values[3] = RandomStringUtils.randomAlphanumeric(12);
      return ObjectUtils.identityToString(this) + ":" + DateTimeFormatter.ISO_INSTANT.format(Instant.now()) + ":" + String.format("%08x", Objects.hash(values));
   }

   /**
    * 构造函数
    * 
    * @param gitProperties Git属性
    * @param buildProperties 构建属性
    */
   public CustomKeyGenerator(GitProperties gitProperties, BuildProperties buildProperties) {
      this.prefix = this.generatePrefix(gitProperties, buildProperties);
   }

   /**
    * 生成键
    * 
    * @param target 目标对象
    * @param method 方法
    * @param params 参数
    * @return 键
    */
   @NonNull
   public Object generate(@NonNull Object target, @NonNull Method method, @NonNull Object... params) {
      return new CacheKey(this.prefix, method.getName(), params);
   }
}