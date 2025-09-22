package cn.topiam.employee.support.cache;

import cn.topiam.employee.support.repository.page.domain.Page;
import cn.topiam.employee.support.security.jackjson.GrantedAuthorityMixin;
import java.io.Serializable;
import java.util.Arrays;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 缓存键
 * 用于生成和管理缓存键值
 */
public class CacheKey implements Serializable {
   
   /**
    * 组
    */
   private final String group;
   
   /**
    * 参数
    */
   private final Object[] params;
   
   /**
    * 哈希码
    */
   private int hashCodeValue;
   
   /**
    * 键名
    */
   private final String keyName;

   /**
    * 判断对象是否相等
    * 
    * @param obj 比较对象
    * @return 是否相等
    */
   public boolean equals(Object obj) {
      if (this != obj && (!(obj instanceof CacheKey) || 
              !this.group.equals(((CacheKey) obj).group) || 
              !this.keyName.equals(((CacheKey) obj).keyName) || 
              !Arrays.deepEquals(this.params, ((CacheKey) obj).params))) {
         return false;
      } else {
         return true;
      }
   }

   /**
    * 构造函数
    * 
    * @param group 组
    * @param keyName 键名
    * @param params 参数
    */
   public CacheKey(String group, String keyName, Object... params) {
      Assert.notNull(group, GrantedAuthorityMixin.decodeString("Wib}nc'vrhs;its;e~'urwk"));
      Assert.notNull(params, Page.decode("o2O3O0^-\n3_-^~D1^~H;\n0_2F"));
      this.group = group;
      this.keyName = keyName;
      this.params = new Object[params.length];
      System.arraycopy(params, 0, this.params, 0, params.length);
      
      this.hashCodeValue = this.group.hashCode();
      this.hashCodeValue = 31 * this.hashCodeValue + this.keyName.hashCode();
      this.hashCodeValue = 31 * this.hashCodeValue + Arrays.deepHashCode(this.params);
   }

   /**
    * 转换为字符串
    * 
    * @return 字符串表示
    */
   public String toString() {
      return this.group + " " + this.getClass().getSimpleName() + this.keyName + " [" + 
              StringUtils.arrayToCommaDelimitedString(this.params) + "]";
   }

   /**
    * 获取哈希码
    * 
    * @return 哈希码
    */
   public final int hashCode() {
      return this.hashCodeValue;
   }
}