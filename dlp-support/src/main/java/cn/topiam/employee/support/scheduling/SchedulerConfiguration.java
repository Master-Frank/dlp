package cn.topiam.employee.support.scheduling;

import cn.topiam.employee.support.async.CustomThreadPoolTaskExecutor;
import java.util.Map;
import java.util.concurrent.Executor;
import org.slf4j.MDC;
import org.springframework.boot.task.ThreadPoolTaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * 调度配置类
 * 用于配置定时任务调度器
 */
@Configuration
@EnableScheduling
public class SchedulerConfiguration implements SchedulingConfigurer {
   
   /**
    * 线程池任务调度器构建器
    */
   private final ThreadPoolTaskSchedulerBuilder threadPoolTaskSchedulerBuilder;

   /**
    * 配置任务调度
    * 
    * @param taskRegistrar 任务注册器
    */
   public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
      taskRegistrar.setScheduler(this.taskScheduler());
   }

   /**
    * 任务调度器Bean
    * 
    * @return 任务调度器
    */
   @Bean(
      destroyMethod = "shutdown"
   )
   public Executor taskScheduler() {
      Map<String, String> mdcContextMap = MDC.getCopyOfContextMap();
      CustomThreadPoolTaskExecutor executor = new CustomThreadPoolTaskExecutor(mdcContextMap);
      executor.initialize();
      return executor;
   }

   /**
    * 构造函数
    * 
    * @param threadPoolTaskSchedulerBuilder 线程池任务调度器构建器
    */
   public SchedulerConfiguration(ThreadPoolTaskSchedulerBuilder threadPoolTaskSchedulerBuilder) {
      this.threadPoolTaskSchedulerBuilder = threadPoolTaskSchedulerBuilder;
   }
}