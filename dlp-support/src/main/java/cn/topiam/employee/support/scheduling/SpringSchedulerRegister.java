package cn.topiam.employee.support.scheduling;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;

/**
 * Spring调度注册器
 * 用于注册和管理定时任务
 */
public class SpringSchedulerRegister implements DisposableBean {
   
   /**
    * 任务调度器
    */
   private final TaskScheduler scheduler;
   
   /**
    * 任务映射表
    */
   private final Map<String, ScheduledFuture<?>> scheduledTasks;

   /**
    * 调度Cron任务
    *
    * @param cronTask 要调度的Cron任务
    * @return ScheduledFuture对象
    */
   private ScheduledFuture<?> scheduleCronTask(CronTask cronTask) {
      return this.scheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
   }

   /**
    * 添加Cron任务
    *
    * @param taskName 任务名称
    * @param runnable 任务执行体
    * @param cronExpression Cron表达式
    */
   public void addCronTask(String taskName, Runnable runnable, String cronExpression) {
      this.scheduleCronTask(taskName, new CronTask(runnable, cronExpression));
   }

   /**
    * 获取调度器
    *
    * @return 任务调度器
    */
   public TaskScheduler getScheduler() {
      return this.scheduler;
   }

   /**
    * 调度Cron任务
    *
    * @param taskName 任务名称
    * @param cronTask 要调度的Cron任务
    */
   private void scheduleCronTask(String taskName, CronTask cronTask) {
      if (cronTask != null) {
         if (this.scheduledTasks.containsKey(taskName)) {
            this.removeCronTask(taskName);
         }

         this.scheduledTasks.put(taskName, this.scheduleCronTask(cronTask));
      }
   }

   /**
    * 销毁方法
    * 在Bean销毁时取消所有定时任务
    */
   @Override
   public void destroy() {
      for(Iterator<ScheduledFuture<?>> iterator = this.scheduledTasks.values().iterator(); iterator.hasNext();) {
         ScheduledFuture<?> future = iterator.next();
         future.cancel(true);
      }

      this.scheduledTasks.clear();
   }

   /**
    * 移除Cron任务
    *
    * @param taskName 任务名称
    */
   public void removeCronTask(String taskName) {
      ScheduledFuture<?> future;
      if ((future = this.scheduledTasks.remove(taskName)) != null) {
         future.cancel(true);
      }
   }

   /**
    * 构造函数
    *
    * @param scheduler 任务调度器
    */
   public SpringSchedulerRegister(TaskScheduler scheduler) {
      this.scheduledTasks = new ConcurrentHashMap<>(16);
      this.scheduler = scheduler;
   }
}