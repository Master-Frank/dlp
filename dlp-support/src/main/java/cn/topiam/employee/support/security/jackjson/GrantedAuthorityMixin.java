package cn.topiam.employee.support.security.jackjson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * 授权权限Mixin类
 * 用于Jackson序列化/反序列化配置
 */
@JsonTypeInfo(
   use = Id.CLASS
)
@JsonDeserialize(
   using = Object.class
)
@JsonAutoDetect(
   fieldVisibility = Visibility.ANY,
   getterVisibility = Visibility.NONE,
   isGetterVisibility = Visibility.NONE
)
@JsonIgnoreProperties(
   ignoreUnknown = true
)
public abstract class GrantedAuthorityMixin {
   
   /**
    * 构造函数
    *
    * @param authority 权限标识
    * @param type 权限类型
    */
   @JsonCreator
   public GrantedAuthorityMixin(@JsonProperty("authority") String authority, @JsonProperty("type") String type) {
   }

   public static String decryptString(Object object) {
      int var1 = 2 ^ 5;
      int var2 = 4 << 3 ^ 3;
      int var3 = 3 << 3 ^ 3;
      String str = (String) object;
      int length = str.length();
      char[] chars = new char[length];
      int index = length - (3 >> 1);
      char[] result = chars;
      int var4 = var3;
      int var5 = index;

      for(int var6 = var1; var5 >= 0; var5 = index) {
         int tempIndex = index;
         int charValue = str.charAt(index);
         --index;
         result[tempIndex] = (char)(charValue ^ var6);
         if (index < 0) {
            break;
         }

         int tempIndex2 = index--;
         result[tempIndex2] = (char)(str.charAt(tempIndex2) ^ var4);
      }

      return new String(result);
   }
}
