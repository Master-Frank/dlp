package cn.topiam.employee.support.security.password.validator;

import cn.topiam.employee.support.security.password.PasswordValidator;
import cn.topiam.employee.support.security.password.exception.PasswordHistoryInvalidException;
import cn.topiam.employee.support.security.password.exception.PasswordInvalidException;
import com.google.common.collect.Lists;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 历史密码验证器
 * 验证新密码是否与历史密码重复
 */
public final class HistoryPasswordValidator implements PasswordValidator {
   @Generated
   private static final Logger logger = LoggerFactory.getLogger(HistoryPasswordValidator.class);
   
   /**
    * 是否启用验证
    */
   private final Boolean enabled;
   
   /**
    * 密码编码器
    */
   private final PasswordEncoder passwordEncoder;
   
   /**
    * 历史密码列表
    */
   private final List<String> historyPasswords;

   /**
    * 验证密码
    *
    * @param password 密码
    * @throws PasswordInvalidException 密码无效异常
    */
   @Override
   public void validate(String password) throws PasswordInvalidException {
      if (this.enabled) {
         for (String historyPassword : this.historyPasswords) {
            if (this.passwordEncoder.matches(password, historyPassword)) {
               throw new PasswordHistoryInvalidException("密码不能与历史密码重复");
            }
         }
      }
   }

   /**
    * 构造函数
    *
    * @param enabled 是否启用
    * @param historyPasswords 历史密码列表
    * @param passwordEncoder 密码编码器
    */
   @Generated
   public HistoryPasswordValidator(Boolean enabled, List<String> historyPasswords, PasswordEncoder passwordEncoder) {
      this.enabled = enabled;
      this.historyPasswords = historyPasswords;
      this.passwordEncoder = passwordEncoder;
   }

   /**
    * 构造函数
    *
    * @param enabled 是否启用
    */
   public HistoryPasswordValidator(Boolean enabled) {
      this.enabled = enabled;
      this.historyPasswords = Lists.newArrayList();
      this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
   }

   /**
    * 构造函数
    *
    * @param historyPasswords 历史密码列表
    * @param passwordEncoder 密码编码器
    */
   public HistoryPasswordValidator(List<String> historyPasswords, PasswordEncoder passwordEncoder) {
      this.enabled = Boolean.TRUE;
      this.historyPasswords = historyPasswords;
      this.passwordEncoder = passwordEncoder;
   }
}
