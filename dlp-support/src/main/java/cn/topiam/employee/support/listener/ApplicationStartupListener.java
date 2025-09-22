package cn.topiam.employee.support.listener;

import cn.topiam.employee.support.util.PhoneUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * 应用启动监听器
 * 用于在应用准备就绪时执行相关操作
 */
@Slf4j
@Component
public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent> {
   
   /**
    * 应用准备就绪事件处理
    * 
    * @param event 应用准备就绪事件
    */
   @Override
   public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
      log.info(PhoneUtils.decryptString("D\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001k\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001}\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u001b\u001a\u001e\u001f\u001e\u000f\u0018\u0018\u001b\u001d\u0018\u001a"));
   }
}