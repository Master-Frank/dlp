package cn.topiam.employee.support.security.password.generator;

import cn.topiam.employee.support.security.password.PasswordGenerator;
import java.util.Arrays;
import java.util.List;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;

/**
 * 默认密码生成器
 * 生成包含大小写字母和数字的随机密码
 */
public final class DefaultPasswordGenerator implements PasswordGenerator {
   /**
    * 字符规则列表
    */
   private final List<CharacterRule> characterRules;

   /**
    * 构造函数
    * 初始化字符规则，包括大写字母、小写字母和数字
    */
   public DefaultPasswordGenerator() {
      CharacterRule[] rules = new CharacterRule[3];
      rules[0] = new CharacterRule(EnglishCharacterData.UpperCase, 2);
      rules[1] = new CharacterRule(EnglishCharacterData.LowerCase, 2);
      rules[2] = new CharacterRule(EnglishCharacterData.Digit, 2);
      this.characterRules = Arrays.asList(rules);
   }

   /**
    * 生成密码
    *
    * @return 生成的密码
    */
   @Override
   public String generatePassword() {
      org.passay.PasswordGenerator generator = new org.passay.PasswordGenerator();
      return generator.generatePassword(10, this.characterRules);
   }
}