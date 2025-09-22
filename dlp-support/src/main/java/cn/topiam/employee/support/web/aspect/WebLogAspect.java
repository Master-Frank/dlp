package cn.topiam.employee.support.web.aspect;

import cn.topiam.employee.support.context.ServletContextService;
import cn.topiam.employee.support.security.util.SecurityUtils;
import cn.topiam.employee.support.trace.TraceUtils;
import cn.topiam.employee.support.util.PhoneUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 * Web日志切面
 * 用于记录Web请求日志
 */
@Slf4j
@Aspect
@Component
public class WebLogAspect {
   
   /**
    * Web层切点
    */
   @Pointcut("execution(public * cn.topiam.employee..controller..*.*(..))")
   public void webLog() {
   }

   /**
    * 环绕通知
    * 
    * @param proceedingJoinPoint 连接点
    * @return 方法执行结果
    * @throws Throwable 异常
    */
   @Around("webLog()")
   public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
      long startTime = System.currentTimeMillis();
      Object result = null;
      Throwable throwable = null;
      try {
         result = proceedingJoinPoint.proceed();
         return result;
      } catch (Throwable e) {
         throwable = e;
         throw e;
      } finally {
         this.handleLog(proceedingJoinPoint, startTime, result, throwable);
      }
   }

   /**
    * 处理日志
    * 
    * @param proceedingJoinPoint 连接点
    * @param startTime 开始时间
    * @param result 执行结果
    * @param throwable 异常
    */
   private void handleLog(ProceedingJoinPoint proceedingJoinPoint, long startTime, Object result, Throwable throwable) {
      try {
         ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
         if (Objects.isNull(attributes)) {
            return;
         }

         HttpServletRequest request = attributes.getRequest();
         MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
         Method method = signature.getMethod();
         String methodStr = request.getMethod() + " " + request.getRequestURI();
         String params = this.getRequestParams(request, proceedingJoinPoint);
         long executeTime = System.currentTimeMillis() - startTime;
         String userId = SecurityUtils.getCurrentUserId();
         String clientIP = this.getClientIP(request);
         String userAgent = request.getHeader("User-Agent");
         String traceId = TraceUtils.get();
         Map<String, Object> logMap = new HashMap<>();
         logMap.put(PhoneUtils.decryptString("k\u001a\u001c\u0017\u0017\u0018\u0019\u0017"), methodStr);
         logMap.put(PhoneUtils.decryptString("}\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"), params);
         logMap.put(PhoneUtils.decryptString("\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"), executeTime);
         logMap.put(PhoneUtils.decryptString("\u001f\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"), userId);
         logMap.put(PhoneUtils.decryptString("\u0018\u0019\u0017\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"), clientIP);
         logMap.put(PhoneUtils.decryptString("\u001f\u001c\u001b\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"), userAgent);
         logMap.put(PhoneUtils.decryptString("\u001e\u000f\u0018\u0018\u001b\u001d\u0018\u001a\u0004\u0011\u0015\u0010\u001e\n\u0012\f\u001a\u001a\u0017\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"), traceId);
         logMap.put(PhoneUtils.decryptString("g\u001d\u001a\u001f\u001c\u001b\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"), LocalDateTime.now());
         logMap.put(PhoneUtils.decryptString("\u001b\u001a\u001e\u001f\u001e\u000f\u0018\u0018\u001b\u001d\u0018\u001a"), result);
         logMap.put(PhoneUtils.decryptString("\u001a\u001d\u001b\u001c\u001b\u001a\u001e"), throwable);
         if (throwable != null) {
            log.error(PhoneUtils.decryptString("D\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u0018\u0019\u0017\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u001f\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001}\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u001f\u001c\u001b\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u0018\u0019\u0017\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001g\u001d\u001a\u001f\u001c\u001b\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u001b\u001a\u001e\u001f\u001e\u000f\u0018\u0018\u001b\u001d\u0018\u001a\u0001k\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u001e\u000f\u0018\u0018\u001b\u001d\u0018\u001a\u0004\u0011\u0015\u0010\u001e\n\u0012\f\u001a\u001a\u0017\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"), logMap, throwable);
         } else {
            log.info(PhoneUtils.decryptString("D\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u0018\u0019\u0017\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u001f\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001}\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u001f\u001c\u001b\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u0018\u0019\u0017\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001g\u001d\u001a\u001f\u001c\u001b\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u001b\u001a\u001e\u001f\u001e\u000f\u0018\u0018\u001b\u001d\u0018\u001a\u0001k\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u001e\u000f\u0018\u0018\u001b\u001d\u0018\u001a\u0004\u0011\u0015\u0010\u001e\n\u0012\f\u001a\u001a\u0017\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"), logMap.toString());
         }
      } catch (Exception e) {
         log.error(PhoneUtils.decryptString("E\u001f\u000b\u001c\u001a\u0006\u001a\u0019\u001e\u001c\u001b\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"), e);
      }
   }

   /**
    * 获取请求参数
    * 
    * @param request HTTP请求
    * @param proceedingJoinPoint 连接点
    * @return 请求参数字符串
    */
   private String getRequestParams(HttpServletRequest request, ProceedingJoinPoint proceedingJoinPoint) {
      Object[] args = proceedingJoinPoint.getArgs();
      if (args.length == 0) {
         return "";
      } else {
         Object[] params = Arrays.stream(args).filter(arg -> !(arg instanceof HttpServletRequest) && !(arg instanceof HttpServletResponse) && !(arg instanceof MultipartFile)).toArray();
         return params.length == 0 ? "" : ServletContextService.getParameterMap(request).toString() + " " + Arrays.toString(params);
      }
   }
   
   /**
    * 获取客户端IP地址
    * 
    * @param request HTTP请求
    * @return 客户端IP地址
    */
   private String getClientIP(HttpServletRequest request) {
      String ip = request.getHeader("X-Forwarded-For");
      if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
         ip = request.getHeader("Proxy-Client-IP");
      }
      if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
         ip = request.getHeader("WL-Proxy-Client-IP");
      }
      if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
         ip = request.getRemoteAddr();
      }
      return ip;
   }
}