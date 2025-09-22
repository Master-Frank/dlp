package cn.topiam.employee.support.demo;

import cn.topiam.employee.support.util.PhoneUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 演示环境切面
 * 用于在演示环境中拦截特定操作
 */
@Slf4j
@Aspect
@Component
public class DemoEnvironmentAspect {
   
   /**
    * 演示环境环绕通知
    * 
    * @param proceedingJoinPoint 连接点
    * @return 方法执行结果
    * @throws Throwable 异常
    */
   @Around("@annotation(cn.topiam.employee.support.demo.Preview)")
   public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
      log.warn(PhoneUtils.decryptString("D\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001k\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001}\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u001b\u001a\u001e\u001f\u001e\u000f\u0018\u0018\u001b\u001d\u0018\u001a"));
      throw new DemoEnvDoesNotAllowOperationException(PhoneUtils.decryptString("E\u001f\u000b\u001c\u001a\u0006\u001a\u0019\u001e\u001c\u001b\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"));
   }
}