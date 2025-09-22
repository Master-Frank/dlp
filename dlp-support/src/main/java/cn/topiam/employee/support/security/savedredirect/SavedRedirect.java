package cn.topiam.employee.support.security.savedredirect;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.springframework.http.HttpMethod;

/**
 * 保存的重定向信息类
 * 用于保存用户登录前的重定向信息
 */
public class SavedRedirect implements Serializable {
   /**
    * 重定向动作
    */
   private String action;
   
   /**
    * 序列化版本号
    */
   private static final long serialVersionUID = 6530884551410354148L;
   
   /**
    * 参数列表
    */
   private List<Parameter> parameters;
   
   /**
    * HTTP方法
    */
   private String method;

   @Generated
   @Override
   public int hashCode() {
      int result = 59;
      result = result * 59 + (this.getAction() == null ? 43 : this.getAction().hashCode());
      result = result * 59 + (this.getMethod() == null ? 43 : this.getMethod().hashCode());
      result = result * 59 + (this.getParameters() == null ? 43 : this.getParameters().hashCode());
      return result;
   }

   @Generated
   public SavedRedirect() {
      this.method = HttpMethod.GET.toString();
      this.parameters = new ArrayList<>();
   }

   @Generated
   @Override
   public String toString() {
      return "SavedRedirect(action=" + this.getAction() + ", method=" + this.getMethod() + ", parameters=" + String.valueOf(this.getParameters()) + ")";
   }

   @Generated
   public boolean canEqual(Object other) {
      return other instanceof SavedRedirect;
   }

   /**
    * 获取参数列表
    *
    * @return 参数列表
    */
   @JsonProperty("parameters")
   public List<Parameter> getParameters() {
      return this.parameters;
   }

   @Generated
   @Override
   public boolean equals(Object other) {
      if (other == this) {
         return true;
      } else if (!(other instanceof SavedRedirect)) {
         return false;
      } else if (!((SavedRedirect) other).canEqual(this)) {
         return false;
      } else {
         String action = ((SavedRedirect) other).getAction();
         String thisAction = this.getAction();
         if (thisAction == null) {
            if (action != null) {
               return false;
            }
         } else if (!thisAction.equals(action)) {
            return false;
         }

         String method = ((SavedRedirect) other).getMethod();
         String thisMethod = this.getMethod();
         if (thisMethod == null) {
            if (method != null) {
               return false;
            }
         } else if (!thisMethod.equals(method)) {
            return false;
         }

         List<Parameter> parameters = ((SavedRedirect) other).getParameters();
         List<Parameter> thisParameters = this.getParameters();
         if (thisParameters == null) {
            if (parameters != null) {
               return false;
            }
         } else if (!thisParameters.equals(parameters)) {
            return false;
         }

         return true;
      }
   }

   /**
    * 设置动作
    *
    * @param action 动作
    */
   @Generated
   public void setAction(String action) {
      this.action = action;
   }

   /**
    * 设置参数列表
    *
    * @param parameters 参数列表
    */
   @Generated
   public void setParameters(List<Parameter> parameters) {
      this.parameters = parameters;
   }

   /**
    * 获取HTTP方法
    *
    * @return HTTP方法
    */
   @JsonProperty("method")
   public String getMethod() {
      return this.method;
   }

   /**
    * 获取动作
    *
    * @return 动作
    */
   @JsonProperty("action")
   public String getAction() {
      return this.action;
   }

   /**
    * 设置HTTP方法
    *
    * @param method HTTP方法
    */
   @Generated
   public void setMethod(String method) {
      this.method = method;
   }

   /**
    * 参数类
    * 用于保存重定向参数
    */
   @JsonTypeInfo(
      use = Id.CLASS
   )
   public static class Parameter implements Serializable {
      /**
       * 序列化版本号
       */
      private static final long serialVersionUID = -2873886962316261616L;
      
      /**
       * 参数键
       */
      private String key;
      
      /**
       * 参数值
       */
      private String value;

      /**
       * 获取参数值
       *
       * @return 参数值
       */
      @JsonProperty("value")
      public String getValue() {
         return this.value;
      }

      /**
       * 设置参数键
       *
       * @param key 参数键
       */
      @Generated
      public void setKey(String key) {
         this.key = key;
      }

      @Generated
      @Override
      public boolean equals(Object other) {
         if (other == this) {
            return true;
         } else if (!(other instanceof Parameter)) {
            return false;
         } else if (!((Parameter) other).canEqual(this)) {
            return false;
         } else {
            String key = ((Parameter) other).getKey();
            String thisKey = this.getKey();
            if (thisKey == null) {
               if (key != null) {
                  return false;
               }
            } else if (!thisKey.equals(key)) {
               return false;
            }

            String value = ((Parameter) other).getValue();
            String thisValue = this.getValue();
            if (thisValue == null) {
               if (value != null) {
                  return false;
               }
            } else if (!thisValue.equals(value)) {
               return false;
            }

            return true;
         }
      }

      @Generated
      @Override
      public String toString() {
         return "SavedRedirect.Parameter(key=" + this.getKey() + ", value=" + this.getValue() + ")";
      }

      @Generated
      @Override
      public int hashCode() {
         int result = 59;
         result = result * 59 + (this.getKey() == null ? 43 : this.getKey().hashCode());
         result = result * 59 + (this.getValue() == null ? 43 : this.getValue().hashCode());
         return result;
      }

      @Generated
      public boolean canEqual(Object other) {
         return other instanceof Parameter;
      }

      /**
       * 获取参数键
       *
       * @return 参数键
       */
      @JsonProperty("key")
      public String getKey() {
         return this.key;
      }

      /**
       * 设置参数值
       *
       * @param value 参数值
       */
      @Generated
      public void setValue(String value) {
         this.value = value;
      }
   }
}