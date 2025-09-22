package cn.topiam.employee.support.security.util;

import java.util.Map;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

public class HttpSecurityConfigUtils {
   public static PasswordEncoder getPasswordEncoder(HttpSecurity a) {
      HttpSecurity var1 = a;
      PasswordEncoder var2;
      if ((var2 = (PasswordEncoder)a.getSharedObject(PasswordEncoder.class)) == null) {
         var2 = (PasswordEncoder)getOptionalBean(var1, PasswordEncoder.class);
         var1.setSharedObject(PasswordEncoder.class, var2);
      }

      return var2;
   }

   public static UserDetailsService getUserDetailsService(HttpSecurity a) {
      HttpSecurity var1 = a;
      UserDetailsService var2;
      if ((var2 = (UserDetailsService)a.getSharedObject(UserDetailsService.class)) == null) {
         var2 = (UserDetailsService)getOptionalBean(var1, UserDetailsService.class);
         var1.setSharedObject(UserDetailsService.class, var2);
      }

      return var2;
   }

   public static <T> T getOptionalBean(HttpSecurity a, Class<T> a) {
      Map var3;
      int var10000 = (var3 = BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)a.getSharedObject(ApplicationContext.class), a)).size();
      int var10001 = 3 & 5;
      int var10003 = 3 & 5;
      if (var10000 > var10001) {
         var10003 = var3.size();
         String var10004 = a.getName();
         throw new NoUniqueBeanDefinitionException(a, var10003, "Expected single matching bean of type '" + var10004 + "' but found " + var3.size() + ": " + StringUtils.collectionToCommaDelimitedString(var3.keySet()));
      } else {
         return (T)(!var3.isEmpty() ? var3.values().iterator().next() : null);
      }
   }

   public static <T> T getOptionalBean(HttpSecurity a, ResolvableType a) {
      ApplicationContext var2;
      String[] var4;
      int var10000 = (var4 = (var2 = (ApplicationContext)a.getSharedObject(ApplicationContext.class)).getBeanNamesForType(a)).length;
      int var10001 = 2 ^ 3;
      int var10003 = 2 ^ 3;
      if (var10000 > var10001) {
         throw new NoUniqueBeanDefinitionException(a, var4);
      } else {
         var10000 = var4.length;
         var10001 = 2 ^ 3;
         var10003 = 2 ^ 3;
         if (var10000 == var10001) {
            int var10002 = 3 & 4;
            boolean var10004 = true;
            return (T)var2.getBean(var4[var10002]);
         } else {
            return null;
         }
      }
   }
}
