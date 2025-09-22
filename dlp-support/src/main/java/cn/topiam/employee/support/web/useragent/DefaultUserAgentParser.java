package cn.topiam.employee.support.web.useragent;

import cn.topiam.employee.support.context.ApplicationContextService;
import com.blueconic.browscap.BrowsCapField;
import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.UserAgentService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 默认用户代理解析器实现类
 * 使用browscap库解析User-Agent信息
 */
public class DefaultUserAgentParser implements UserAgentParser {
   /**
    * browscap用户代理解析器
    */
   private final com.blueconic.browscap.UserAgentParser userAgentParser;

   /**
    * 获取用户代理信息
    *
    * @param request HTTP请求
    * @return 用户代理信息
    */
   @Override
   public UserAgent getUserAgent(HttpServletRequest request) {
      Capabilities capabilities = this.userAgentParser.parse(request.getHeader(ApplicationContextService.decryptString("\f=<<t\u000f>+7:")));
      String browser = capabilities.getBrowser();
      String browserType = capabilities.getBrowserType();
      String browserMajorVersion = capabilities.getBrowserMajorVersion();
      String deviceType = capabilities.getDeviceType();
      String platform = capabilities.getPlatform();
      String platformVersion = capabilities.getPlatformVersion();
      String renderingEngineMaker = capabilities.getValue(BrowsCapField.RENDERING_ENGINE_MAKER);
      return UserAgent.builder()
            .browser(browser)
            .browserType(browserType)
            .browserMajorVersion(browserMajorVersion)
            .deviceType(deviceType)
            .platform(platform)
            .platformVersion(platformVersion)
            .renderingEngineMaker(renderingEngineMaker)
            .build();
   }

   /**
    * 默认构造函数
    */
   public DefaultUserAgentParser() {
      super();

      try {
         this.userAgentParser = (new UserAgentService()).loadParser();
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
}
