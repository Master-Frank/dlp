package cn.topiam.employee.support.security.core;

import lombok.Generated;
import org.springframework.util.Assert;

/**
 * 授权权限类
 * 实现Spring Security的GrantedAuthority接口
 */
public final class GrantedAuthority implements org.springframework.security.core.GrantedAuthority {
   /**
    * 权限类型
    */
   private final String type;
   
   /**
    * 权限标识
    */
   private final String authority;
   
   private static final long serialVersionUID = -1L;

   /**
    * 获取权限类型
    *
    * @return 权限类型
    */
   @Generated
   public String getType() {
      return this.type;
   }

   /**
    * 获取权限标识
    *
    * @return 权限标识
    */
   @Override
   public String getAuthority() {
      return this.authority;
   }

   /**
    * 判断对象是否相等
    *
    * @param object 比较对象
    * @return 是否相等
    */
   @Generated
   @Override
   public boolean equals(Object object) {
      if (this == object) {
         return true;
      } else if (!(object instanceof GrantedAuthority)) {
         return false;
      } else {
         GrantedAuthority that = (GrantedAuthority)object;
         String thisAuthority = this.getAuthority();
         String thatAuthority = that.getAuthority();
         if (thisAuthority == null) {
            if (thatAuthority != null) {
               return false;
            }
         } else if (!thisAuthority.equals(thatAuthority)) {
            return false;
         }

         String thisType = this.getType();
         String thatType = that.getType();
         if (thisType == null) {
            if (thatType != null) {
               return false;
            }
         } else if (!thisType.equals(thatType)) {
            return false;
         }

         return true;
      }
   }

   /**
    * 生成哈希码
    *
    * @return 哈希码
    */
   @Generated
   @Override
   public int hashCode() {
      int result = 59;
      String authority = this.getAuthority();
      result = result * 59 + (authority == null ? 43 : authority.hashCode());
      String type = this.getType();
      return result * 59 + (type == null ? 43 : type.hashCode());
   }

   /**
    * 构造函数
    *
    * @param type 权限类型
    * @param authority 权限标识
    */
   public GrantedAuthority(String type, String authority) {
      Assert.notNull(type, "权限类型不能为空");
      Assert.notNull(authority, "权限标识不能为空");
      this.authority = authority;
      this.type = type;
   }
}
