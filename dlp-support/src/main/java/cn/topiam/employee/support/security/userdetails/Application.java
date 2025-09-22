package cn.topiam.employee.support.security.userdetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import lombok.Generated;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * 应用类
 * 用于表示用户可访问的应用信息
 */
public class Application implements Serializable {
   /**
    * 应用编码
    */
   private String code;
   
   /**
    * 应用ID
    */
   private String id;
   
   /**
    * 应用组列表
    */
   private List<ApplicationGroup> groups;
   
   /**
    * 应用名称
    */
   private String name;
   
   /**
    * 序列化版本号
    */
   private static final long serialVersionUID = 4385720422327909792L;

   /**
    * 构造函数
    *
    * @param id 应用ID
    * @param code 应用编码
    * @param name 应用名称
    * @param groups 应用组列表
    */
   public Application(String id, String code, String name, List<ApplicationGroup> groups) {
      this.id = id;
      this.code = code;
      this.name = name;
      this.groups = groups;
   }

   /**
    * 默认构造函数
    */
   public Application() {
   }

   /**
    * 设置应用组列表
    *
    * @param groups 应用组列表
    */
   @Generated
   public void setGroups(List<ApplicationGroup> groups) {
      this.groups = groups;
   }

   /**
    * 获取应用名称
    *
    * @return 应用名称
    */
   @JsonProperty("name")
   public String getName() {
      return this.name;
   }

   /**
    * 计算哈希值
    *
    * @return 哈希值
    */
   @Override
   public int hashCode() {
      Object[] values = new Object[2];
      values[0] = this.id;
      values[1] = this.code;
      return Objects.hash(values);
   }

   /**
    * 设置应用ID
    *
    * @param id 应用ID
    */
   @Generated
   public void setId(String id) {
      this.id = id;
   }

   /**
    * 获取应用编码
    *
    * @return 应用编码
    */
   @JsonProperty("code")
   public String getCode() {
      return this.code;
   }

   /**
    * 设置应用编码
    *
    * @param code 应用编码
    */
   @Generated
   public void setCode(String code) {
      this.code = code;
   }

   /**
    * 设置应用名称
    *
    * @param name 应用名称
    */
   @Generated
   public void setName(String name) {
      this.name = name;
   }

   /**
    * 获取应用组列表
    *
    * @return 应用组列表
    */
   @JsonProperty("groups")
   public List<ApplicationGroup> getGroups() {
      return this.groups;
   }

   /**
    * 获取应用ID
    *
    * @return 应用ID
    */
   @JsonProperty("id")
   public String getId() {
      return this.id;
   }

   /**
    * 比较对象是否相等
    *
    * @param other 其他对象
    * @return 是否相等
    */
   @Override
   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (other != null && this.getClass() == other.getClass()) {
         Application application = (Application) other;
         return (new EqualsBuilder()).append(this.id, application.id).isEquals();
      } else {
         return false;
      }
   }

   /**
    * 应用组类
    * 用于表示应用的组信息
    */
   public static class ApplicationGroup implements Serializable {
      /**
       * 应用组名称
       */
      private String name;
      
      /**
       * 应用组ID
       */
      private String id;
      
      /**
       * 序列化版本号
       */
      private static final long serialVersionUID = 4385720422327909792L;
      
      /**
       * 应用组编码
       */
      private String code;

      /**
       * 获取应用组名称
       *
       * @return 应用组名称
       */
      @JsonProperty("name")
      public String getName() {
         return this.name;
      }

      /**
       * 设置应用组名称
       *
       * @param name 应用组名称
       */
      @Generated
      public void setName(String name) {
         this.name = name;
      }

      /**
       * 获取应用组编码
       *
       * @return 应用组编码
       */
      @JsonProperty("code")
      public String getCode() {
         return this.code;
      }

      /**
       * 计算哈希值
       *
       * @return 哈希值
       */
      @Override
      public int hashCode() {
         Object[] values = new Object[2];
         values[0] = this.id;
         values[1] = this.code;
         return Objects.hash(values);
      }

      /**
       * 设置应用组编码
       *
       * @param code 应用组编码
       */
      @Generated
      public void setCode(String code) {
         this.code = code;
      }

      /**
       * 获取应用组ID
       *
       * @return 应用组ID
       */
      @JsonProperty("id")
      public String getId() {
         return this.id;
      }

      /**
       * 设置应用组ID
       *
       * @param id 应用组ID
       */
      @Generated
      public void setId(String id) {
         this.id = id;
      }

      /**
       * 构造函数
       *
       * @param id 应用组ID
       * @param code 应用组编码
       * @param name 应用组名称
       */
      public ApplicationGroup(String id, String code, String name) {
         this.id = id;
         this.code = code;
         this.name = name;
      }

      /**
       * 默认构造函数
       */
      public ApplicationGroup() {
      }

      /**
       * 比较对象是否相等
       *
       * @param other 其他对象
       * @return 是否相等
       */
      @Override
      public boolean equals(Object other) {
         if (this == other) {
            return true;
         } else if (other != null && this.getClass() == other.getClass()) {
            ApplicationGroup group = (ApplicationGroup) other;
            return (new EqualsBuilder()).append(this.id, group.id).isEquals();
         } else {
            return false;
         }
      }
   }
}