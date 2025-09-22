package cn.topiam.employee.support.listener;

import cn.topiam.employee.support.config.SystemInitializer;
import cn.topiam.employee.support.util.PhoneUtils;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * 系统初始化监听器
 * 用于在应用上下文刷新时执行系统初始化任务
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemInitializationListener implements ApplicationListener<ContextRefreshedEvent> {
   
   /**
    * 系统初始化器列表
    */
   private final List<SystemInitializer> initializers;

   /**
    * 应用上下文刷新事件处理
    * 
    * @param event 上下文刷新事件
    */
   @Override
   public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
      if (event.getApplicationContext().getParent() == null) {
         this.initializeSystem();
      }
   }

   /**
    * 初始化系统
    */
   @PostConstruct
   public void initializeSystem() {
      log.info(PhoneUtils.decryptString("D\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001k\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001}\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"));
      this.initializers.forEach(initializer -> {
         try {
            initializer.initialize();
         } catch (Exception e) {
            log.error(PhoneUtils.decryptString("E\u001f\u000b\u001c\u001a\u0006\u001a\u0019\u001e\u001c\u001b\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"), e);
         }
      });
      log.info(PhoneUtils.decryptString("D\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001k\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u001b\u001a\u001e\u001f\u001e\u000f\u0018\u0018\u001b\u001d\u0018\u001a"));
   }
}