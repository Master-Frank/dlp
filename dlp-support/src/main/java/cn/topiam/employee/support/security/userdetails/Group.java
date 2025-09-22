package cn.topiam.employee.support.security.userdetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Objects;
import lombok.Generated;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * 用户组类
 * 用于表示用户所属的组信息
 */
public class Group implements Serializable {
   /**
    * 组ID
    */
   private String id;
   
   /**
    * 序列化版本号
    */
   private static final long serialVersionUID = 4385720422327909792L;
   
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
    * 获取组ID
    *
    * @return 组ID
    */
   @JsonProperty("id")
   public String getId() {
      return this.id;
   }

   /**
    * 默认构造函数
    */
   public Group() {
   }

   /**
    * 设置组ID
    *
    * @param id 组ID
    */
   @Generated
   public void setId(String id) {
      this.id = id;
   }

   /**
    * 构造函数
    *
    * @param id 组ID
    */
   public Group(String id) {
      this.id = id;
   }

   /**
    * 计算哈希值
    *
    * @return 哈希值
    */
   @Override
   public int hashCode() {
      Object[] values = new Object[1];
      values[0] = this.id;
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
         Group group = (Group) other;
         return Objects.equals(this.id, group.id);
      } else {
         return false;
      }
   }
}
