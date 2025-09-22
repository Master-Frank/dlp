package cn.topiam.employee.support.repository;

import cn.topiam.employee.support.repository.aspect.query.QuerySingleResult;
import cn.topiam.employee.support.util.PhoneUtils;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 查询单个结果切面
 * 用于处理查询单个结果的逻辑
 */
@Slf4j
@Aspect
@Component
public class QuerySingleResultAspect {
   
   /**
    * 查询单个结果切点
    */
   @Pointcut("@annotation(cn.topiam.employee.support.repository.aspect.query.QuerySingleResult)")
   public void querySingleResultPointcut() {
   }

   /**
    * 环绕通知
    * 
    * @param proceedingJoinPoint 连接点
    * @return 方法执行结果
    * @throws Throwable 异常
    */
   @Around("querySingleResultPointcut()")
   public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
      try {
         Object result = proceedingJoinPoint.proceed();
         if (result instanceof List) {
            List<?> list = (List<?>) result;
            if (list.isEmpty()) {
               throw new NoResultException(PhoneUtils.decryptString("E\u001f\u000b\u001c\u001a\u0006\u001a\u0019\u001e\u001c\u001b\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"));
            } else if (list.size() > 1) {
               throw new NonUniqueResultException(PhoneUtils.decryptString("E\u001f\u000b\u001c\u001a\u0006\u001a\u0019\u001e\u001c\u001b\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"));
            } else {
               return list.get(0);
            }
         } else {
            return result;
         }
      } catch (Exception e) {
         log.error(PhoneUtils.decryptString("E\u001f\u000b\u001c\u001a\u0006\u001a\u0019\u001e\u001c\u001b\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"), e);
         throw e;
      }
   }
}