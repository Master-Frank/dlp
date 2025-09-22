package cn.topiam.employee.support.web.jackjson;

import cn.topiam.employee.support.web.useragent.UserAgent;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;

/**
 * Web Jackson模块类
 * 用于配置Web相关的Jackson序列化和反序列化
 */
public class WebJacksonModule extends Module {

   /**
    * 设置模块
    *
    * @param context 设置上下文
    */
   @Override
   public void setupModule(SetupContext context) {
      context.setMixInAnnotations(UserAgent.class, UserAgentMixin.class);
   }

   /**
    * 获取版本
    *
    * @return 版本
    */
   @Override
   public Version version() {
      return new Version(1, 0, 0, null, null, null);
   }

   /**
    * 获取模块名
    *
    * @return 模块名
    */
   @Override
   public String getModuleName() {
      return WebJacksonModule.class.getName();
   }
}
