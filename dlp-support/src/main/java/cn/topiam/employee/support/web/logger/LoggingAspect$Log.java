package cn.topiam.employee.support.web.logger;

import cn.topiam.employee.support.repository.page.domain.PageModel;
import cn.topiam.employee.support.util.EmailUtils;
import cn.topiam.employee.support.web.useragent.UserAgent;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import java.util.Map;
import lombok.Generated;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

/**
 * 日志记录类
 * 用于记录请求和响应的相关信息
 */
public class LoggingAspect$Log {
   /**
    * 时间跨度（毫秒）
    */
   private Long timeSpan;
   
   /**
    * 响应结果对象
    */
   private Object result;
   
   /**
    * 请求头信息
    */
   private Map<String, String> headers;
   
   /**
    * 请求URL
    */
   private String requestUrl;
   
   /**
    * HTTP方法（GET、POST等）
    */
   private String httpMethod;
   
   /**
    * 客户端IP地址
    */
   private String ip;
   
   /**
    * 请求体内容
    */
   private String body;
   
   /**
    * 请求参数
    */
   private Object parameter;
   
   /**
    * 请求是否成功
    */
   private Boolean success;
   
   /**
    * 用户代理信息
    */
   private UserAgent userAgent;

   /**
    * 获取请求体内容
    *
    * @return 请求体内容
    */
   @Generated
   public String getBody() {
      return this.body;
   }

   /**
    * 设置请求体内容
    *
    * @param body 请求体内容
    */
   @Generated
   public void setBody(String body) {
      this.body = body;
   }

   /**
    * 获取请求头信息
    *
    * @return 请求头信息
    */
   @Generated
   public Map<String, String> getHeaders() {
      return this.headers;
   }

   /**
    * 设置请求头信息
    *
    * @param headers 请求头信息
    */
   @Generated
   public void setHeaders(Map<String, String> headers) {
      this.headers = headers;
   }

   /**
    * 获取时间跨度
    *
    * @return 时间跨度（毫秒）
    */
   @Generated
   public Long getTimeSpan() {
      return this.timeSpan;
   }

   /**
    * 设置时间跨度
    *
    * @param timeSpan 时间跨度（毫秒）
    */
   @Generated
   public void setTimeSpan(Long timeSpan) {
      this.timeSpan = timeSpan;
   }

   /**
    * 获取用户代理信息
    *
    * @return 用户代理信息
    */
   @Generated
   public UserAgent getUserAgent() {
      return this.userAgent;
   }

   /**
    * 设置用户代理信息
    *
    * @param userAgent 用户代理信息
    */
   @Generated
   public void setUserAgent(UserAgent userAgent) {
      this.userAgent = userAgent;
   }

   /**
    * 获取客户端IP地址
    *
    * @return 客户端IP地址
    */
   @Generated
   public String getIp() {
      return this.ip;
   }

   /**
    * 设置客户端IP地址
    *
    * @param ip 客户端IP地址
    */
   @Generated
   public void setIp(String ip) {
      this.ip = ip;
   }

   /**
    * 获取请求URL
    *
    * @return 请求URL
    */
   @Generated
   public String getRequestUrl() {
      return this.requestUrl;
   }

   /**
    * 设置请求URL
    *
    * @param requestUrl 请求URL
    */
   @Generated
   public void setRequestUrl(String requestUrl) {
      this.requestUrl = requestUrl;
   }

   /**
    * 获取HTTP方法
    *
    * @return HTTP方法
    */
   @Generated
   public String getHttpMethod() {
      return this.httpMethod;
   }

   /**
    * 设置HTTP方法
    *
    * @param httpMethod HTTP方法
    */
   @Generated
   public void setHttpMethod(String httpMethod) {
      this.httpMethod = httpMethod;
   }

   /**
    * 检查是否可以与另一个对象相等比较
    *
    * @param other 要比较的对象
    * @return 如果可以比较则返回true
    */
   @Generated
   public boolean canEqual(Object other) {
      return other instanceof LoggingAspect$Log;
   }

   /**
    * 获取请求是否成功
    *
    * @return 是否成功
    */
   @Generated
   public Boolean getSuccess() {
      return this.success;
   }

   /**
    * 设置请求是否成功
    *
    * @param success 是否成功
    */
   @Generated
   public void setSuccess(Boolean success) {
      this.success = success;
   }

   /**
    * 获取请求参数
    *
    * @return 请求参数
    */
   @Generated
   public Object getParameter() {
      return this.parameter;
   }

   /**
    * 设置请求参数
    *
    * @param parameter 请求参数
    */
   @Generated
   public void setParameter(Object parameter) {
      this.parameter = parameter;
   }

   /**
    * 获取响应结果
    *
    * @return 响应结果
    */
   @Generated
   public Object getResult() {
      return this.result;
   }

   /**
    * 设置响应结果
    *
    * @param result 响应结果
    */
   @Generated
   public void setResult(Object result) {
      this.result = result;
   }

   /**
    * 比较两个对象是否相等
    *
    * @param other 要比较的对象
    * @return 如果相等返回true，否则返回false
    */
   @Generated
   @Override
   public boolean equals(Object other) {
      if (this == other) {
         return true;
      }
      
      if (!(other instanceof LoggingAspect$Log)) {
         return false;
      }
      
      if (!((LoggingAspect$Log) other).canEqual(this)) {
         return false;
      }
      
      LoggingAspect$Log log = (LoggingAspect$Log) other;
      
      return ObjectUtils.nullSafeEquals(this.timeSpan, log.timeSpan)
          && ObjectUtils.nullSafeEquals(this.success, log.success)
          && ObjectUtils.nullSafeEquals(this.ip, log.ip)
          && ObjectUtils.nullSafeEquals(this.result, log.result)
          && ObjectUtils.nullSafeEquals(this.requestUrl, log.requestUrl)
          && ObjectUtils.nullSafeEquals(this.httpMethod, log.httpMethod)
          && ObjectUtils.nullSafeEquals(this.body, log.body)
          && ObjectUtils.nullSafeEquals(this.parameter, log.parameter)
          && ObjectUtils.nullSafeEquals(this.userAgent, log.userAgent)
          && ObjectUtils.nullSafeEquals(this.headers, log.headers);
   }

   /**
    * 计算对象的哈希码
    *
    * @return 哈希码
    */
   @Generated
   @Override
   public int hashCode() {
      int result = 1;
      result = result * 59 + (this.timeSpan == null ? 43 : this.timeSpan.hashCode());
      result = result * 59 + (this.success == null ? 43 : this.success.hashCode());
      result = result * 59 + (this.ip == null ? 43 : this.ip.hashCode());
      result = result * 59 + (this.result == null ? 43 : this.result.hashCode());
      result = result * 59 + (this.requestUrl == null ? 43 : this.requestUrl.hashCode());
      result = result * 59 + (this.httpMethod == null ? 43 : this.httpMethod.hashCode());
      result = result * 59 + (this.body == null ? 43 : this.body.hashCode());
      result = result * 59 + (this.parameter == null ? 43 : this.parameter.hashCode());
      result = result * 59 + (this.success == null ? 43 : this.success.hashCode());
      result = result * 59 + (this.userAgent == null ? 43 : this.userAgent.hashCode());
      result = result * 59 + (this.headers == null ? 43 : this.headers.hashCode());
      return result;
   }

   /**
    * 获取对象的字符串表示
    *
    * @return 格式化的日志信息字符串
    */
   @Override
   public String toString() {
      // 格式化用户代理信息
      String userAgentStr = JSONObject.toJSONString(this.userAgent, new JSONWriter.Feature[0]);
      // HTTP方法
      String httpMethod = this.httpMethod;
      // 请求URL
      String requestUrl = this.requestUrl;
      // 客户端IP
      String ip = this.ip;
      // 请求方法信息
      String methodInfo = StringUtils.isNotBlank(this.httpMethod) 
          ? "┣ 请求方法:  " + this.httpMethod + "\n" 
          : "";
      // 请求头信息
      String headersStr = JSONObject.toJSONString(this.headers, new JSONWriter.Feature[0]);
      // 请求参数
      String params = ObjectUtils.isEmpty(this.parameter) 
          ? PageModel.1_1_1_ce("7{1") 
          : String.valueOf(this.parameter);
      // 请求体内容
      String bodyContent = StringUtils.isNotBlank(this.body) 
          ? this.body 
          : EmailUtils.1_1_1_ce("a");
      // 响应信息
      String responseInfo;
      if (this.success != null && this.success) {
         responseInfo = "┣ 请求响应:  " + String.valueOf(this.result) + "\n";
      } else {
         String error = JSONObject.toJSONString(this.result, new JSONWriter.Feature[0]);
         responseInfo = "┣ 请求异常:  " + error + "\n";
      }

      return "\n┣ ----------------------------------------------------------\n"
           + "┣ 用户代理:  " + userAgentStr + "\n"
           + "┣ 请求路径:  " + httpMethod + " " + requestUrl + "\n"
           + "┣ 客户端IP:  " + ip + "\n"
           + methodInfo
           + "┣ 请求标头:  " + headersStr + "\n"
           + "┣ 请求参数:  " + params + "\n"
           + "┣ 请求载荷:  " + bodyContent + "\n"
           + responseInfo
           + "┣ 请求耗时:  " + this.timeSpan + " ms\n"
           + "┣ ----------------------------------------------------------";
   }