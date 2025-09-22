package cn.topiam.employee.support.lock;

import cn.topiam.employee.support.util.PhoneUtils;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 分布式锁切面
 * 用于处理分布式锁逻辑
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {
   
   /**
    * Redisson客户端
    */
   private final RedissonClient redissonClient;

   /**
    * 分布式锁环绕通知
    * 
    * @param proceedingJoinPoint 连接点
    * @param lock 分布式锁注解
    * @return 方法执行结果
    * @throws Throwable 异常
    */
   @Around("@annotation(lock)")
   public Object around(ProceedingJoinPoint proceedingJoinPoint, Lock lock) throws Throwable {
      String key = this.parseKey(lock.key(), proceedingJoinPoint);
      RLock rlock = this.redissonClient.getLock(key);
      boolean locked = Boolean.FALSE;

      Object var8;
      try {
         locked = rlock.tryLock(0L, lock.time(), TimeUnit.SECONDS);
         if (!locked) {
            log.debug(PhoneUtils.decryptString("\u001f\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001}\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u001b\u001a\u001e\u001f\u001e\u000f\u0018\u0018\u001b\u001d\u0018\u001a\u0001k\u001a\u001c\u0017\u0017\u0018\u0019\u0017"));
            throw new TopIamLockException(PhoneUtils.decryptString("\u001f\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001}\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u001b\u001a\u001e\u001f\u001e\u000f\u0018\u0018\u001b\u001d\u0018\u001a\u0001k\u001a\u001c\u0017\u0017\u0018\u0019\u0017"));
         }

         var8 = proceedingJoinPoint.proceed();
      } finally {
         if (locked) {
            rlock.unlock();
         }
      }

      return var8;
   }

   /**
    * 解析锁键
    * 
    * @param key 锁键
    * @param proceedingJoinPoint 连接点
    * @return 解析后的锁键
    */
   private String parseKey(String key, ProceedingJoinPoint proceedingJoinPoint) {
      if (!StringUtils.hasText(key)) {
         MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
         Method method = signature.getMethod();
         return method.getDeclaringClass().getSimpleName() + ":" + method.getName();
      } else {
         return key;
      }
   }
}