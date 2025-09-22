package cn.topiam.employee.support.redis;

import cn.topiam.employee.support.cache.CachePrefixGenerator;
import cn.topiam.employee.support.util.PhoneUtils;
import java.time.Duration;
import java.util.Objects;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Redis配置类
 * 用于配置Redis连接和相关Bean
 */
@Configuration
public class RedisConfiguration {
   
   /**
    * Redis连接工厂Bean
    * 
    * @param redisProperties Redis属性
    * @return Redis连接工厂
    */
   @Bean
   public LettuceConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
      RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
      configuration.setHostName(redisProperties.getHost());
      configuration.setPort(redisProperties.getPort());
      configuration.setDatabase(redisProperties.getDatabase());
      if (redisProperties.getPassword() != null) {
         configuration.setPassword(RedisPassword.of(redisProperties.getPassword()));
      }

      return new LettuceConnectionFactory(configuration);
   }

   /**
    * Redis模板Bean
    * 
    * @param connectionFactory Redis连接工厂
    * @return Redis模板
    */
   @Bean
   public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
      RedisTemplate<String, Object> template = new RedisTemplate<>();
      template.setKeySerializer(new StringRedisSerializer());
      template.setValueSerializer(new StringRedisSerializer());
      template.setHashKeySerializer(new StringRedisSerializer());
      template.setHashValueSerializer(new StringRedisSerializer());
      template.setConnectionFactory(connectionFactory);
      return template;
   }

   /**
    * Redis缓存管理器Bean
    * 
    * @param connectionFactory Redis连接工厂
    * @param cachePrefixGenerator 缓存前缀生成器
    * @return Redis缓存管理器
    */
   @Bean
   public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, CachePrefixGenerator cachePrefixGenerator) {
      RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
              .entryTtl(Duration.ofHours(1))
              .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
              .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
              .computePrefixWith(cachePrefixGenerator)
              .disableCachingNullValues();
      return RedisCacheManager.builder(connectionFactory).cacheDefaults(config).build();
   }

   /**
    * Redisson客户端Bean
    * 
    * @param redisProperties Redis属性
    * @return Redisson客户端
    */
   @Bean(
      destroyMethod = "shutdown"
   )
   public RedissonClient redisson(RedisProperties redisProperties) {
      Config config = new Config();
      String host = redisProperties.getHost();
      int port = redisProperties.getPort();
      String password = redisProperties.getPassword();
      Assert.hasText(host, PhoneUtils.decryptString("\u0001g0`'|6D'`1{-|"));
      config.useSingleServer().setAddress("redis://" + host + ":" + port);
      if (StringUtils.hasText(password)) {
         config.useSingleServer().setPassword(password);
      }

      return Redisson.create(config);
   }
}