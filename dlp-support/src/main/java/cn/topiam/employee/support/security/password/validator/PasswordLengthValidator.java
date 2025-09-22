package cn.topiam.employee.support.security.password.validator;

import cn.topiam.employee.support.security.password.PasswordValidator;
import cn.topiam.employee.support.security.password.exception.PasswordInvalidException;
import cn.topiam.employee.support.security.password.exception.PasswordLengthInvalidException;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import java.util.List;
import lombok.Generated;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 密码长度验证器
 * 验证密码长度是否符合要求
 */
public final class PasswordLengthValidator implements PasswordValidator {
   @Generated
   private static final Logger logger = LoggerFactory.getLogger(PasswordLengthValidator.class);
   
   /**
    * 最小长度
    */
   private final Integer minLength;
   
   /**
    * 最大长度
    */
   private final Integer maxLength;

   /**
    * 构造函数
    *
    * @param minLength 最小长度
    * @param maxLength 最大长度
    */
   @Generated
   public PasswordLengthValidator(Integer minLength, Integer maxLength) {
      this.minLength = minLength;
      this.maxLength = maxLength;
   }

   /**
    * 验证密码
    *
    * @param password 密码
    * @throws PasswordInvalidException 密码无效异常
    */
   @Override
   public void validate(String password) throws PasswordInvalidException {
      LengthRule lengthRule = new LengthRule(this.minLength, this.maxLength);
      RuleResult result = lengthRule.validate(new PasswordData(password));
      if (!result.isValid()) {
         logger.error("密码长度验证失败: {}", JSONObject.toJSONString(result.getDetails(), new JSONWriter.Feature[0]));
         throw new PasswordLengthInvalidException("密码长度必须在" + this.minLength + "到" + this.maxLength + "之间");
      }
   }
}
