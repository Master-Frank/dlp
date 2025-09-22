package cn.topiam.employee.support.security.crypto.password;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码编码器工厂类
 * 用于创建委托密码编码器
 */
public class PasswordEncoderFactories {
   
   /**
    * 创建委托密码编码器
    *
    * @return 密码编码器
    */
   public PasswordEncoder createDelegatingPasswordEncoder() {
      Map<String, PasswordEncoder> encoders = new HashMap<>();
      String defaultEncoder = "bcrypt";
      encoders.put("bcrypt", new BCryptPasswordEncoder());
      encoders.put("noop", NoOpPasswordEncoder.getInstance());
      return new DelegatingPasswordEncoder(defaultEncoder, encoders);
   }
}