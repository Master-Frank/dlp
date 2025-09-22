package cn.topiam.employee.support.error;

import cn.topiam.employee.support.error.ErrorAttributesHandler;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.View;

/**
 * 自定义错误配置类
 * 用于配置错误处理相关的Bean
 */
@AutoConfigureBefore({ErrorMvcAutoConfiguration.class})
public class CustomErrorConfiguration {
   
   /**
    * 默认错误视图Bean
    * 
    * @return 错误视图
    */
   @Bean(
      name = {"error"}
   )
   public View defaultErrorView() {
      return new CustomErrorView();
   }

   /**
    * 错误属性Bean
    * 
    * @param errorAttributesHandler 错误属性处理器
    * @return 错误属性
    */
   @Bean
   public ErrorAttributes errorAttributes(@Autowired(required = false) ErrorAttributesHandler errorAttributesHandler) {
      return Objects.isNull(errorAttributesHandler) ? new CustomErrorAttributes() : new CustomErrorAttributes(errorAttributesHandler);
   }
}