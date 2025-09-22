package cn.topiam.employee.support.web.useragent;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.io.Serializable;

/**
 * 用户代理类
 * 用于表示HTTP请求中的User-Agent信息
 */
public final class UserAgent implements Serializable {
   /**
    * 浏览器类型
    */
   private String browserType;
   
   /**
    * 设备类型
    */
   private String deviceType;
   
   /**
    * 渲染引擎制造商
    */
   private String renderingEngineMaker;
   
   /**
    * 浏览器主版本
    */
   private String browserMajorVersion;
   
   /**
    * 平台版本
    */
   private String platformVersion;
   
   /**
    * 浏览器
    */
   private String browser;
   
   /**
    * 平台
    */
   private String platform;

   /**
    * 设置平台
    *
    * @param platform 平台
    */
   public void setPlatform(String platform) {
      this.platform = platform;
   }

   /**
    * 转换为字符串
    *
    * @return JSON字符串
    */
   @Override
   public String toString() {
      return JSON.toJSONString(this);
   }

   /**
    * 设置浏览器主版本
    *
    * @param browserMajorVersion 浏览器主版本
    */
   public void setBrowserMajorVersion(String browserMajorVersion) {
      this.browserMajorVersion = browserMajorVersion;
   }

   /**
    * 设置浏览器
    *
    * @param browser 浏览器
    */
   public void setBrowser(String browser) {
      this.browser = browser;
   }

   /**
    * 构造函数
    *
    * @param platform 平台
    * @param browserType 浏览器类型
    * @param deviceType 设备类型
    * @param platformVersion 平台版本
    * @param renderingEngineMaker 渲染引擎制造商
    * @param browserMajorVersion 浏览器主版本
    * @param browser 浏览器
    */
   public UserAgent(String platform, String browserType, String deviceType, String platformVersion, String renderingEngineMaker, String browserMajorVersion, String browser) {
      this.browser = browser;
      this.browserType = browserType;
      this.browserMajorVersion = browserMajorVersion;
      this.deviceType = deviceType;
      this.platform = platform;
      this.platformVersion = platformVersion;
      this.renderingEngineMaker = renderingEngineMaker;
   }

   /**
    * 获取设备类型
    *
    * @return 设备类型
    */
   @JsonProperty("deviceType")
   public String getDeviceType() {
      return this.deviceType;
   }

   /**
    * 设置渲染引擎制造商
    *
    * @param renderingEngineMaker 渲染引擎制造商
    */
   public void setRenderingEngineMaker(String renderingEngineMaker) {
      this.renderingEngineMaker = renderingEngineMaker;
   }

   /**
    * 设置设备类型
    *
    * @param deviceType 设备类型
    */
   public void setDeviceType(String deviceType) {
      this.deviceType = deviceType;
   }

   /**
    * 获取平台
    *
    * @return 平台
    */
   @JsonProperty("platform")
   public String getPlatform() {
      return this.platform;
   }

   /**
    * 获取浏览器
    *
    * @return 浏览器
    */
   @JsonProperty("browser")
   public String getBrowser() {
      return this.browser;
   }

   /**
    * 设置浏览器类型
    *
    * @param browserType 浏览器类型
    */
   public void setBrowserType(String browserType) {
      this.browserType = browserType;
   }

   /**
    * 创建用户代理构建器
    *
    * @return 用户代理构建器
    */
   public static UserAgentBuilder builder() {
      return new UserAgentBuilder();
   }

   /**
    * 获取渲染引擎制造商
    *
    * @return 渲染引擎制造商
    */
   @JsonProperty("renderingEngineMaker")
   public String getRenderingEngineMaker() {
      return this.renderingEngineMaker;
   }

   /**
    * 设置平台版本
    *
    * @param platformVersion 平台版本
    */
   public void setPlatformVersion(String platformVersion) {
      this.platformVersion = platformVersion;
   }

   /**
    * 获取浏览器类型
    *
    * @return 浏览器类型
    */
   @JsonProperty("browserType")
   public String getBrowserType() {
      return this.browserType;
   }

   /**
    * 获取平台版本
    *
    * @return 平台版本
    */
   @JsonProperty("platformVersion")
   public String getPlatformVersion() {
      return this.platformVersion;
   }

   /**
    * 获取浏览器主版本
    *
    * @return 浏览器主版本
    */
   @JsonProperty("browserMajorVersion")
   public String getBrowserMajorVersion() {
      return this.browserMajorVersion;
   }

   /**
    * 用户代理构建器
    */
   @JsonPOJOBuilder(withPrefix = "")
   public static class UserAgentBuilder {
      /**
       * 浏览器类型
       */
      private String browserType;
      
      /**
       * 设备类型
       */
      private String deviceType;
      
      /**
       * 渲染引擎制造商
       */
      private String renderingEngineMaker;
      
      /**
       * 浏览器主版本
       */
      private String browserMajorVersion;
      
      /**
       * 平台版本
       */
      private String platformVersion;
      
      /**
       * 浏览器
       */
      private String browser;
      
      /**
       * 平台
       */
      private String platform;

      /**
       * 构建用户代理对象
       *
       * @return 用户代理对象
       */
      public UserAgent build() {
         return new UserAgent(this.platform, this.browserType, this.deviceType, this.platformVersion, this.renderingEngineMaker, this.browserMajorVersion, this.browser);
      }

      /**
       * 设置平台
       *
       * @param platform 平台
       * @return 用户代理构建器
       */
      public UserAgentBuilder platform(String platform) {
         this.platform = platform;
         return this;
      }

      /**
       * 设置浏览器类型
       *
       * @param browserType 浏览器类型
       * @return 用户代理构建器
       */
      public UserAgentBuilder browserType(String browserType) {
         this.browserType = browserType;
         return this;
      }

      /**
       * 设置设备类型
       *
       * @param deviceType 设备类型
       * @return 用户代理构建器
       */
      public UserAgentBuilder deviceType(String deviceType) {
         this.deviceType = deviceType;
         return this;
      }

      /**
       * 设置渲染引擎制造商
       *
       * @param renderingEngineMaker 渲染引擎制造商
       * @return 用户代理构建器
       */
      public UserAgentBuilder renderingEngineMaker(String renderingEngineMaker) {
         this.renderingEngineMaker = renderingEngineMaker;
         return this;
      }

      /**
       * 设置平台版本
       *
       * @param platformVersion 平台版本
       * @return 用户代理构建器
       */
      public UserAgentBuilder platformVersion(String platformVersion) {
         this.platformVersion = platformVersion;
         return this;
      }

      /**
       * 设置浏览器主版本
       *
       * @param browserMajorVersion 浏览器主版本
       * @return 用户代理构建器
       */
      public UserAgentBuilder browserMajorVersion(String browserMajorVersion) {
         this.browserMajorVersion = browserMajorVersion;
         return this;
      }

      /**
       * 设置浏览器
       *
       * @param browser 浏览器
       * @return 用户代理构建器
       */
      public UserAgentBuilder browser(String browser) {
         this.browser = browser;
         return this;
      }

      /**
       * 转换为字符串
       *
       * @return JSON字符串
       */
      @Override
      public String toString() {
         return JSON.toJSONString(this);
      }
   }
}