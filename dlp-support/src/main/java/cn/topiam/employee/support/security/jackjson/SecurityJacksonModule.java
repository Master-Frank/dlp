package cn.topiam.employee.support.security.jackjson;

import cn.topiam.employee.support.security.authentication.AuthenticationProvider;
import cn.topiam.employee.support.security.authentication.WebAuthenticationDetails;
import cn.topiam.employee.support.security.core.GrantedAuthority;
import cn.topiam.employee.support.security.password.authentication.NeedChangePasswordAuthenticationToken;
import cn.topiam.employee.support.security.savedredirect.SavedRedirect;
import cn.topiam.employee.support.security.userdetails.Application;
import cn.topiam.employee.support.security.userdetails.DataOrigin;
import cn.topiam.employee.support.security.userdetails.Group;
import cn.topiam.employee.support.security.userdetails.Organization;
import cn.topiam.employee.support.security.userdetails.UserDetails;
import cn.topiam.employee.support.security.userdetails.UserType;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;

/**
 * 安全Jackson模块类
 * 用于配置安全相关类的Jackson序列化/反序列化
 */
public class SecurityJacksonModule extends Module {
   
   /**
    * 获取模块版本
    *
    * @return 模块版本
    */
   @Override
   public Version version() {
      return new Version(1, 1, 1, null, null, null);
   }

   /**
    * 获取模块名称
    *
    * @return 模块名称
    */
   @Override
   public String getModuleName() {
      return SecurityJacksonModule.class.getName();
   }

   /**
    * 设置模块配置
    *
    * @param context 配置上下文
    */
   @Override
   public void setupModule(SetupContext context) {
      // 配置各类安全相关类的Mixin注解
      context.setMixInAnnotations(Long.class, Object.class);
      context.setMixInAnnotations(SavedRedirect.class, Object.class);
      context.setMixInAnnotations(UserDetails.class, Object.class);
      context.setMixInAnnotations(UserType.class, Object.class);
      context.setMixInAnnotations(DataOrigin.class, Object.class);
      context.setMixInAnnotations(Group.class, Object.class);
      context.setMixInAnnotations(Organization.class, Object.class);
      context.setMixInAnnotations(Application.class, Object.class);
      context.setMixInAnnotations(Application.ApplicationGroup.class, Object.class);
      context.setMixInAnnotations(WebAuthenticationDetails.class, Object.class);
      context.setMixInAnnotations(AuthenticationProvider.class, Object.class);
      context.setMixInAnnotations(GrantedAuthority.class, GrantedAuthorityMixin.class);
      context.setMixInAnnotations(NeedChangePasswordAuthenticationToken.class, Object.class);
   }
}