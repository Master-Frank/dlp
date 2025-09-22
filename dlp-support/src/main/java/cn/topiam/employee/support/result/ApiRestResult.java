package cn.topiam.employee.support.result;

import cn.topiam.employee.support.exception.enums.ExceptionStatus;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

/**
 * API REST结果类
 * 用于封装API接口返回结果
 *
 * @param <T> 泛型类型
 */
@Schema(description = "API REST")
public class ApiRestResult<T> implements Serializable {
   
   /**
    * 是否成功
    */
   @JsonProperty("success")
   @Parameter(description = "是否成功")
   private final Boolean success;
   
   public static final String FAIL = "fail";
   
   /**
    * 消息
    */
   @Parameter(description = "消息")
   private String message;
   
   public static final String SUCCESS = "success";
   
   /**
    * 状态
    */
   @JsonProperty("status")
   @Parameter(description = "状态")
   private String status;
   
   /**
    * 返回结果
    */
   @JsonProperty("result")
   @Parameter(description = "返回内容")
   private T result;
   
   private static final long serialVersionUID = -5396280450442040415L;

   /**
    * 创建错误结果
    *
    * @param result 结果
    * @param <T> 泛型类型
    * @return ApiRestResult实例
    */
   public static <T> ApiRestResult<T> err(T result) {
      return new ApiRestResult<T>(ExceptionStatus.EX900001.getCode(), ExceptionStatus.EX900001.getMessage(), result);
   }

   /**
    * 获取返回结果
    *
    * @return 返回结果
    */
   public T getResult() {
      return this.result;
   }

   /**
    * 创建成功结果
    *
    * @param <T> 泛型类型
    * @return ApiRestResult实例
    */
   public static <T> ApiRestResult<T> ok() {
      return new ApiRestResult<T>(String.valueOf(HttpStatus.OK.value()), "success", null);
   }

   /**
    * 构造函数
    *
    * @param status 状态
    * @param message 消息
    * @param result 结果
    */
   public ApiRestResult(String status, String message, T result) {
      this.status = status;
      this.result = result;
      this.success = status.equals(String.valueOf(HttpStatus.OK.value()));
      if (StringUtils.isNotBlank(message)) {
         this.message = message;
      } else {
         this.message = status.equals(String.valueOf(HttpStatus.OK.value())) ? "success" : "fail";
      }
   }

   /**
    * 创建错误结果
    *
    * @param <T> 泛型类型
    * @return ApiRestResult实例
    */
   public static <T> ApiRestResult<T> err() {
      return new ApiRestResult<T>(ExceptionStatus.EX900001.getCode(), ExceptionStatus.EX900001.getMessage(), null);
   }

   /**
    * 获取状态
    *
    * @return 状态
    */
   public String getStatus() {
      return StringUtils.isBlank(this.status) ? String.valueOf(HttpStatus.OK.value()) : this.status;
   }

   /**
    * 获取消息
    *
    * @return 消息
    */
   public String getMessage() {
      if (StringUtils.isBlank(this.message)) {
         return this.success ? "success" : "fail";
      } else {
         return this.message;
      }
   }

   /**
    * 创建结果构建器
    *
    * @param <T> 泛型类型
    * @return RestResultBuilder实例
    */
   public static <T> RestResultBuilder<T> builder() {
      return new RestResultBuilder<T>();
   }

   /**
    * 获取是否成功
    *
    * @return 是否成功
    */
   public Boolean getSuccess() {
      return this.success;
   }

   /**
    * 创建成功结果
    *
    * @param result 结果
    * @param <T> 泛型类型
    * @return ApiRestResult实例
    */
   public static <T> ApiRestResult<T> ok(T result) {
      return new ApiRestResult<T>(String.valueOf(HttpStatus.OK.value()), "success", result);
   }

   /**
    * 设置状态
    *
    * @param status 状态
    * @return ApiRestResult实例
    */
   public ApiRestResult<T> status(String status) {
      this.status = status;
      return this;
   }

   /**
    * 创建错误结果
    *
    * @param status 状态
    * @param message 消息
    * @param <T> 泛型类型
    * @return ApiRestResult实例
    */
   public static <T> ApiRestResult<T> err(String status, String message) {
      return new ApiRestResult<T>(status, message, null);
   }

   /**
    * 设置结果
    *
    * @param result 结果
    * @return ApiRestResult实例
    */
   public ApiRestResult<T> result(T result) {
      this.result = result;
      return this;
   }

   @Override
   public String toString() {
      return "ApiRestResult{result=" + String.valueOf(this.result) + ", status='" + this.status + "', message='" + this.message + "'} " + super.toString();
   }

   /**
    * 构建结果
    *
    * @return ApiRestResult实例
    */
   public ApiRestResult<T> build() {
      return new ApiRestResult<T>(this.status, this.message, this.result);
   }

   /**
    * 设置消息
    *
    * @param message 消息
    * @return ApiRestResult实例
    */
   public ApiRestResult<T> message(String message) {
      this.message = message;
      return this;
   }

   /**
    * REST结果构建器
    *
    * @param <T> 泛型类型
    */
   public static class RestResultBuilder<T> {
      private T result;
      private String message;
      private String status;

      /**
       * 构建结果
       *
       * @return ApiRestResult实例
       */
      public ApiRestResult<T> build() {
         if (ObjectUtils.isEmpty(this.status)) {
            this.status = Integer.toString(HttpStatus.OK.value());
         }

         if (!StringUtils.equals(this.status, String.valueOf(HttpStatus.OK.value())) && ObjectUtils.isEmpty(this.message)) {
            this.message = HttpStatus.OK.getReasonPhrase();
         }

         if (StringUtils.isBlank(this.message) && this.result instanceof Boolean && this.result.equals(Boolean.FALSE)) {
            this.message = "fail";
         }

         return new ApiRestResult<T>(this.status, this.message, this.result);
      }

      /**
       * 设置消息
       *
       * @param message 消息
       * @return RestResultBuilder实例
       */
      public RestResultBuilder<T> message(String message) {
         this.message = message;
         return this;
      }

      @Override
      public String toString() {
         return JSONObject.toJSONString(this, new JSONWriter.Feature[0]);
      }

      /**
       * 设置结果
       *
       * @param result 结果
       * @return RestResultBuilder实例
       */
      public RestResultBuilder<T> result(T result) {
         this.result = result;
         return this;
      }

      /**
       * 设置状态
       *
       * @param status 状态
       * @return RestResultBuilder实例
       */
      public RestResultBuilder<T> status(String status) {
         this.status = status;
         return this;
      }
   }
}