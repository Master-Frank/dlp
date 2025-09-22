package cn.topiam.employee.support.security.password.validator;

import cn.topiam.employee.support.security.password.PasswordValidator;
import cn.topiam.employee.support.security.password.exception.PasswordIllegalSequenceInvalidException;
import cn.topiam.employee.support.security.password.exception.PasswordInvalidException;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import java.util.List;
import lombok.Generated;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.PasswordData;
import org.passay.Rule;
import org.passay.RuleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 密码非法序列验证器
 * 验证密码是否包含非法序列（如连续字母、数字、键盘序列等）
 */
public final class PasswordIllegalSequenceValidator implements PasswordValidator {
   @Generated
   private static final Logger logger = LoggerFactory.getLogger(PasswordIllegalSequenceValidator.class);
   
   /**
    * 是否启用验证
    */
   private final Boolean enabled;

   /**
    * 构造函数
    *
    * @param enabled 是否启用
    */
   @Generated
   public PasswordIllegalSequenceValidator(Boolean enabled) {
      this.enabled = enabled;
   }

   /**
    * 验证密码
    *
    * @param password 密码
    * @throws PasswordInvalidException 密码无效异常
    */
   @Override
   public void validate(String password) throws PasswordInvalidException {
      if (this.enabled) {
         IllegalSequenceRule alphabeticalRule = new IllegalSequenceRule(EnglishSequenceData.Alphabetical);
         IllegalSequenceRule numericalRule = new IllegalSequenceRule(EnglishSequenceData.Numerical);
         IllegalSequenceRule qwertyRule = new IllegalSequenceRule(EnglishSequenceData.USQwerty);
         
         Rule[] rules = new Rule[3];
         rules[0] = alphabeticalRule;
         rules[1] = numericalRule;
         rules[2] = qwertyRule;
         
         org.passay.PasswordValidator validator = new org.passay.PasswordValidator(rules);
         RuleResult result = validator.validate(new PasswordData(password));
         if (!result.isValid()) {
            logger.error("密码非法序列验证失败", JSONObject.toJSONString(result.getDetails(), new JSONWriter.Feature[0]));
            throw new PasswordIllegalSequenceInvalidException("密码不能包含连续的字母、数字或键盘序列");
         }
      }
   }
}