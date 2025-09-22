package cn.topiam.employee.support.security.password.validator;

import cn.topiam.employee.support.security.password.PasswordValidator;
import cn.topiam.employee.support.security.password.exception.PasswordContinuousSameCharInvalidException;
import cn.topiam.employee.support.security.password.exception.PasswordInvalidException;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import java.util.List;
import lombok.Generated;
import org.passay.PasswordData;
import org.passay.RepeatCharactersRule;
import org.passay.Rule;
import org.passay.RuleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 密码连续相同字符验证器
 * 验证密码中连续相同字符的数量是否超过限制
 */
public final class PasswordContinuousSameCharValidator implements PasswordValidator {
   @Generated
   private static final Logger logger = LoggerFactory.getLogger(PasswordContinuousSameCharValidator.class);
   
   /**
    * 最大连续相同字符数
    */
   private final Integer maxConsecutiveChars;

   /**
    * 验证密码
    *
    * @param password 密码
    * @throws PasswordInvalidException 密码无效异常
    */
   @Override
   public void validate(String password) throws PasswordInvalidException {
      RepeatCharactersRule rule = new RepeatCharactersRule(this.maxConsecutiveChars);
      Rule[] rules = new Rule[1];
      rules[0] = rule;
      org.passay.PasswordValidator validator = new org.passay.PasswordValidator(rules);
      RuleResult result = validator.validate(new PasswordData(password));
      if (!result.isValid()) {
         logger.error("密码连续相同字符验证失败", JSONObject.toJSONString(result.getDetails(), new JSONWriter.Feature[0]));
         throw new PasswordContinuousSameCharInvalidException("密码中连续相同字符数量超过限制");
      }
   }

   /**
    * 构造函数
    *
    * @param maxConsecutiveChars 最大连续相同字符数
    */
   @Generated
   public PasswordContinuousSameCharValidator(Integer maxConsecutiveChars) {
      this.maxConsecutiveChars = maxConsecutiveChars;
   }
}
