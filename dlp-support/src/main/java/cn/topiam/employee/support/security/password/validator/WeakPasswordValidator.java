package cn.topiam.employee.support.security.password.validator;

import cn.topiam.employee.support.security.password.PasswordValidator;
import cn.topiam.employee.support.security.password.exception.PasswordInvalidException;
import cn.topiam.employee.support.security.password.exception.PasswordWeakInvalidException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 弱密码验证器
 * 验证密码是否为弱密码
 */
public final class WeakPasswordValidator implements PasswordValidator {
   /**
    * 是否启用验证
    */
   private final Boolean enabled;
   
   /**
    * 弱密码集合
    */
   private final Set<String> weakPasswords;
   
   @Generated
   private static final Logger logger = LoggerFactory.getLogger(WeakPasswordValidator.class);

   /**
    * 构造函数
    *
    * @param enabled 是否启用
    */
   public WeakPasswordValidator(Boolean enabled) {
      this.enabled = enabled;
      this.weakPasswords = new HashSet<>(16);
   }

   /**
    * 构造函数
    *
    * @param weakPasswordList 弱密码列表
    */
   public WeakPasswordValidator(List<String> weakPasswordList) {
      this.enabled = Boolean.TRUE;
      this.weakPasswords = new HashSet<>(weakPasswordList);
   }

   /**
    * 验证密码
    *
    * @param password 密码
    * @throws PasswordInvalidException 密码无效异常
    */
   @Override
   public void validate(String password) throws PasswordInvalidException {
      if (this.enabled && this.weakPasswords.contains(password)) {
         throw new PasswordWeakInvalidException("密码强度太弱");
      }
   }

   /**
    * 构造函数
    *
    * @param enabled 是否启用
    * @param weakPasswords 弱密码集合
    */
   @Generated
   public WeakPasswordValidator(Boolean enabled, Set<String> weakPasswords) {
      this.enabled = enabled;
      this.weakPasswords = weakPasswords;
   }
}