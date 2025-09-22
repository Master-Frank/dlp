package cn.topiam.employee.support.security.userdetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;
import lombok.Generated;

/**
 * 组织机构类
 * 实现了序列化接口，用于组织信息的传输和存储
 */
public class Organization implements Serializable {
   
   /**
    * 序列化版本号
    */
   private static final long serialVersionUID = 4385720422327909792L;
   
   /**
    * 组织路径
    */
   private String path;
   
   /**
    * 组织ID
    */
   private String id;
   
   /**
    * 字符串解密方法
    * 
    * @param object 待解密的对象
    * @return 解密后的字符串
    */
   public static String decryptString(Object object) {
      int key1 = 5 << 3 ^ 3;
      int key2 = 4 << 3 ^ 5;
      int key3 = 3 << 4 ^ 5;
      String str = (String) object;
      int length = str.length();
      char[] chars = new char[length];
      int index = length - (3 >> 1);
      char[] result = chars;
      int var4 = key3;
      int var5 = index;

      for (int var6 = key1; var5 >= 0; var5 = index) {
         int tempIndex = index;
         int charValue = str.charAt(index);
         --index;
         result[tempIndex] = (char) (charValue ^ var6);
         if (index < 0) {
            break;
         }

         int tempIndex2 = index--;
         result[tempIndex2] = (char) (str.charAt(tempIndex2) ^ var4);
      }

      return new String(result);
   }

   /**
    * 默认构造函数
    */
   public Organization() {
   }

   /**
    * 获取组织ID
    *
    * @return 组织ID
    */
   @JsonProperty("id")
   public String getId() {
      return this.id;
   }

   /**
    * 获取组织路径
    *
    * @return 组织路径
    */
   @JsonProperty("path")
   public String getPath() {
      return this.path;
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
      values[1] = this.path;
      return Objects.hash(values);
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
         Organization organization = (Organization) other;
         if (Objects.equals(this.id, organization.id) && Objects.equals(this.path, organization.path)) {
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   /**
    * 构造函数
    *
    * @param id 组织ID
    * @param path 组织路径
    */
   public Organization(String id, String path) {
      this.id = id;
      this.path = path;
   }

   /**
    * 设置组织ID
    *
    * @param id 组织ID
    */
   @Generated
   public void setId(String id) {
      this.id = id;
   }

   /**
    * 设置组织路径
    *
    * @param path 组织路径
    */
   @Generated
   public void setPath(String path) {
      this.path = path;
   }
}