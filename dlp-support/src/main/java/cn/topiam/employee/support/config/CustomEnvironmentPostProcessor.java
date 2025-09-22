package cn.topiam.employee.support.config;

import cn.topiam.employee.support.util.PhoneUtils;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

/**
 * 自定义环境后置处理器
 * 用于在环境初始化后添加自定义属性
 */
public class CustomEnvironmentPostProcessor implements EnvironmentPostProcessor {
   
   /**
    * 后置处理环境
    * 
    * @param environment 可配置的环境
    * @param application Spring应用
    */
   public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
      MutablePropertySources propertySources = environment.getPropertySources();
      Map<String, Object> map = new HashMap<>();
      map.put(PhoneUtils.decryptString("k\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u0018\u0019\u0017\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"), PhoneUtils.decryptString("\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"));
      map.put(PhoneUtils.decryptString("k\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001}\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"), PhoneUtils.decryptString("\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"));
      map.put(PhoneUtils.decryptString("k\u001a\u001c\u0017\u0017\u0018\u0019\u0017\u0001\u001f\u001c\u001b\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"), PhoneUtils.decryptString("\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"));
      PropertySource<?> propertySource = new MapPropertySource("customProperties", map);
      propertySources.addFirst(propertySource);
   }
}