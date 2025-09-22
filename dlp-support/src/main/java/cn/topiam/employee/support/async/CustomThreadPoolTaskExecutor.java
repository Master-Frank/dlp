package cn.topiam.employee.support.async;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Custom thread pool task executor
 * Thread pool that supports MDC context propagation
 */
public class CustomThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {
   
   /**
    * MDC context mapping
    */
   private Map<String, String> mdcContextMap;
   
   /**
    * Serialization version UID
    */
   private static final long serialVersionUID = 1735074699085892566L;
   
   /**
    * Whether to use fixed context
    */
   private boolean useFixedContext;

   /**
    * Get MDC context mapping
    * 
    * @return MDC context mapping
    */
   private Map<String, String> getMdcContextMap() {
      return this.useFixedContext ? this.mdcContextMap : MDC.getCopyOfContextMap();
   }

   /**
    * Execute task
    * 
    * @param task 任务
    */
   public void execute(@NonNull Runnable task) {
      super.execute(wrap(task, this.getMdcContextMap()));
   }

   /**
    * Submit task
    * 
    * @param task 任务
    * @param <T> 泛型类型
    * @return Future对象
    */
   public <T> Future<T> submit(Callable<T> task) {
      return super.submit(wrap(task, this.getMdcContextMap()));
   }

   /**
    * Submit task
    * 
    * @param task 任务
    * @param result 结果
    * @param <T> 泛型类型
    * @return Future对象
    */
   public <T> Future<T> submit(Runnable task, T result) {
      return super.submit(wrap(task, this.getMdcContextMap()), result);
   }

   /**
    * Wrap Runnable task
    * 
    * @param task Task
    * @param context MDC context
    * @return Wrapped task
    */
   private static Runnable wrap(Runnable task, Map<String, String> context) {
      return () -> {
         Map<String, String> previous = MDC.getCopyOfContextMap();
         if (context == null) {
            MDC.clear();
         } else {
            MDC.setContextMap(context);
         }

         try {
            task.run();
         } finally {
            if (previous == null) {
               MDC.clear();
            } else {
               MDC.setContextMap(previous);
            }
         }
      };
   }

   /**
    * Wrap Callable task
    * 
    * @param task Task
    * @param context MDC context
    * @param <T> Generic type
    * @return Wrapped task
    */
   private static <T> Callable<T> wrap(Callable<T> task, Map<String, String> context) {
      return () -> {
         Map<String, String> previous = MDC.getCopyOfContextMap();
         if (context == null) {
            MDC.clear();
         } else {
            MDC.setContextMap(context);
         }

         try {
            return task.call();
         } finally {
            if (previous == null) {
               MDC.clear();
            } else {
               MDC.setContextMap(previous);
            }
         }
      };
   }

   /**
    * Constructor
    * 
    * @param mdcContextMap MDC context mapping
    */
   public CustomThreadPoolTaskExecutor(Map<String, String> mdcContextMap) {
      this.mdcContextMap = mdcContextMap;
      this.useFixedContext = true;
   }

   /**
    * Default constructor
    */
   public CustomThreadPoolTaskExecutor() {
      this.useFixedContext = false;
   }
}