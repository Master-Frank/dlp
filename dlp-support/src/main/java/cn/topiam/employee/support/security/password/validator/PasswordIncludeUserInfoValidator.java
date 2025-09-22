package cn.topiam.employee.support.security.password.validator;

import cn.topiam.employee.support.context.ApplicationContextService;
import cn.topiam.employee.support.security.password.PasswordValidator;
import cn.topiam.employee.support.security.password.exception.PasswordIncludeUserInfoInvalidException;
import cn.topiam.employee.support.security.password.exception.PasswordInvalidException;
import lombok.Generated;
import org.apache.commons.lang3.StringUtils;
import org.passay.MatchBehavior;
import org.passay.PasswordData;
import org.passay.Rule;
import org.passay.UsernameRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 密码包含用户信息验证器
 * 验证密码是否包含用户相关信息（如用户名、手机号、邮箱等）
 */
public final class PasswordIncludeUserInfoValidator implements PasswordValidator {
   @Generated
   private static final Logger logger = LoggerFactory.getLogger(PasswordIncludeUserInfoValidator.class);
   
   /**
    * 邮箱
    */
   private final String email;
   
   /**
    * 昵称
    */
   private final String nickName;
   
   /**
    * 匹配行为
    */
   public MatchBehavior matchBehavior;
   
   /**
    * 全名
    */
   private final String fullName;
   
   /**
    * 是否启用验证
    */
   private final Boolean enabled;
   
   /**
    * 用户名
    */
   private final String username;
   
   /**
    * 手机号
    */
   private final String phone;

   /**
    * 获取邮箱
    *
    * @return 邮箱
    */
   @Generated
   public String getEmail() {
      return this.email;
   }

   /**
    * 构造函数
    *
    * @param enabled 是否启用
    * @param username 用户名
    * @param phone 手机号
    * @param email 邮箱
    * @param fullName 全名
    * @param nickName 昵称
    */
   @Generated
   public PasswordIncludeUserInfoValidator(Boolean enabled, String username, String phone, String email, String fullName, String nickName) {
      this.matchBehavior = MatchBehavior.Contains;
      this.enabled = enabled;
      this.fullName = fullName;
      this.nickName = nickName;
      this.username = username;
      this.phone = phone;
      this.email = email;
   }

   /**
    * 获取手机号
    *
    * @return 手机号
    */
   @Generated
   public String getPhone() {
      return this.phone;
   }

   /**
    * 获取全名
    *
    * @return 全名
    */
   @Generated
   public String getFullName() {
      return this.fullName;
   }

   /**
    * 构造函数
    *
    * @param enabled 是否启用
    */
   public PasswordIncludeUserInfoValidator(Boolean enabled) {
      this.matchBehavior = MatchBehavior.Contains;
      this.enabled = enabled;
      this.fullName = "";
      this.nickName = "";
      this.username = "";
      this.phone = "";
      this.email = "";
   }

   /**
    * 获取用户名
    *
    * @return 用户名
    */
   @Generated
   public String getUsername() {
      return this.username;
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
         // 检查是否包含用户名
         if (StringUtils.isNoneBlank(this.username)) {
            UsernameRule usernameRule = new UsernameRule();
            Rule[] rules = new Rule[1];
            rules[0] = usernameRule;
            org.passay.PasswordValidator validator = new org.passay.PasswordValidator(rules);
            PasswordData passwordData = new PasswordData(password);
            passwordData.setUsername(this.username);
            if (!validator.validate(passwordData).isValid()) {
               logger.error("密码不能包含用户名");
               throw new PasswordIncludeUserInfoInvalidException("密码不能包含用户名");
            }
         }

         // 检查是否包含手机号
         if (StringUtils.isNoneBlank(this.phone) && this.matchBehavior.match(password, this.phone)) {
            logger.error("密码不能包含手机号");
            throw new PasswordIncludeUserInfoInvalidException("密码不能包含手机号");
         }

         // 检查是否包含全名
         if (StringUtils.isNoneBlank(this.fullName)) {
            if (this.matchBehavior.match(password, cn.topiam.employee.support.util.Pinyin4jUtils.getFirstSpellPinYin(this.fullName, true))) {
               logger.error("密码不能包含用户姓名拼音");
               throw new PasswordIncludeUserInfoInvalidException("密码不能包含用户姓名拼音");
            }

            if (this.matchBehavior.match(password, cn.topiam.employee.support.util.Pinyin4jUtils.getFirstSpellPinYin(this.fullName, false)) 
                || this.matchBehavior.match(password, cn.topiam.employee.support.util.Pinyin4jUtils.getPinYinHeadChar(this.fullName))) {
               logger.error("密码不能包含用户姓名拼音");
               throw new PasswordIncludeUserInfoInvalidException("密码不能包含用户姓名拼音");
            }
         }

         // 检查是否包含昵称
         if (StringUtils.isNoneBlank(this.nickName)) {
            if (this.matchBehavior.match(password, this.nickName)) {
               logger.error("密码不能包含昵称");
               throw new PasswordIncludeUserInfoInvalidException("密码不能包含昵称");
            }

            if (this.matchBehavior.match(password, cn.topiam.employee.support.util.Pinyin4jUtils.getFirstSpellPinYin(this.nickName, true))) {
               logger.error("密码不能包含昵称拼音");
               throw new PasswordIncludeUserInfoInvalidException("密码不能包含昵称拼音");
            }

            if (this.matchBehavior.match(password, cn.topiam.employee.support.util.Pinyin4jUtils.getFirstSpellPinYin(this.nickName, false)) 
                || this.matchBehavior.match(password, cn.topiam.employee.support.util.Pinyin4jUtils.getPinYinHeadChar(this.nickName))) {
               logger.error("密码不能包含昵称拼音");
               throw new PasswordIncludeUserInfoInvalidException("密码不能包含昵称拼音");
            }
         }

         // 检查是否包含邮箱
         if (StringUtils.isNoneBlank(this.email)) {
            int atIndex = this.email.lastIndexOf('@');
            if (atIndex > 0) {
               String emailPrefix = this.email.substring(0, atIndex);
               if (this.matchBehavior.match(password, emailPrefix)) {
                  logger.error("密码不能包含邮箱前缀");
                  throw new PasswordIncludeUserInfoInvalidException("密码不能包含邮箱前缀");
               }
            }
         }
      }
   }

   /**
    * 获取昵称
    *
    * @return 昵称
    */
   @Generated
   public String getNickName() {
      return this.nickName;
   }

   /**
    * 构造函数
    *
    * @param username 用户名
    * @param phone 手机号
    * @param email 邮箱
    * @param fullName 全名
    * @param nickName 昵称
    */
   public PasswordIncludeUserInfoValidator(String username, String phone, String email, String fullName, String nickName) {
      this.matchBehavior = MatchBehavior.Contains;
      this.enabled = Boolean.TRUE;
      this.fullName = fullName;
      this.nickName = nickName;
      this.username = username;
      this.phone = phone;
      this.email = email;
   }
}