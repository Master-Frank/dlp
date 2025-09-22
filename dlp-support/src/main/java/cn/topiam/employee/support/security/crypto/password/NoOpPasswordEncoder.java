package cn.topiam.employee.support.security.crypto.password;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 无操作密码编码器
 * 不对密码进行任何编码操作，直接返回原密码
 */
public final class NoOpPasswordEncoder implements PasswordEncoder {
   /**
    * 单例实例
    */
   private static final PasswordEncoder INSTANCE = new NoOpPasswordEncoder();

   /**
    * 编码密码
    *
    * @param rawPassword 原始密码
    * @return 编码后的密码
    */
   @Override
   public String encode(CharSequence rawPassword) {
      return rawPassword.toString();
   }

   /**
    * 匹配密码
    *
    * @param rawPassword 原始密码
    * @param encodedPassword 编码后的密码
    * @return 是否匹配
    */
   @Override
   public boolean matches(CharSequence rawPassword, String encodedPassword) {
      return rawPassword.toString().equals(encodedPassword);
   }

   /**
    * 获取单例实例
    *
    * @return 密码编码器实例
    */
   public static PasswordEncoder getInstance() {
      return INSTANCE;
   }

   /**
    * 私有构造函数
    */
   private NoOpPasswordEncoder() {
   }
}