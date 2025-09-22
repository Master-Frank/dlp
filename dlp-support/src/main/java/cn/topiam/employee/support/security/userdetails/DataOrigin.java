package cn.topiam.employee.support.security.userdetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Generated;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 数据来源类
 * 用于表示用户数据的来源信息
 */
public class DataOrigin implements Serializable {
   /**
    * 飞书数据来源
    */
   public static final DataOrigin FEI_SHU = new DataOrigin("FEI_SHU", "飞书");
   
   /**
    * 手动录入数据来源
    */
   public static final DataOrigin INPUT = new DataOrigin("INPUT", "手动录入");
   
   /**
    * 数据来源类型
    */
   private String type;
   
   /**
    * 钉钉数据来源
    */
   public static final DataOrigin DING_TALK = new DataOrigin("DING_TALK", "钉钉");
   
   /**
    * 数据来源名称
    */
   private String name;

   /**
    * 默认构造函数
    */
   public DataOrigin() {
   }

   /**
    * 获取数据来源名称
    *
    * @return 数据来源名称
    */
   @JsonProperty("name")
   public String getName() {
      return this.name;
   }

   /**
    * 设置数据来源名称
    *
    * @param name 数据来源名称
    */
   @Generated
   public void setName(String name) {
      this.name = name;
   }

   /**
    * 获取数据来源类型
    *
    * @return 数据来源类型
    */
   @JsonProperty("type")
   public String getType() {
      return this.type;
   }

   /**
    * 设置数据来源类型
    *
    * @param type 数据来源类型
    */
   @Generated
   public void setType(String type) {
      this.type = type;
   }

   /**
    * 构造函数
    *
    * @param type 数据来源类型
    * @param name 数据来源名称
    */
   public DataOrigin(String type, String name) {
      this.type = type;
      this.name = name;
   }

   /**
    * 计算哈希值
    *
    * @return 哈希值
    */
   @Override
   public int hashCode() {
      return (new HashCodeBuilder(17, 37)).append(this.type).append(this.name).toHashCode();
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
         DataOrigin dataOrigin = (DataOrigin) other;
         return (new EqualsBuilder()).append(this.type, dataOrigin.type).append(this.name, dataOrigin.name).isEquals();
      } else {
         return false;
      }
   }
}