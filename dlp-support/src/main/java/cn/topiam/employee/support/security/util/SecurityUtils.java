package cn.topiam.employee.support.security.util;

import cn.topiam.employee.support.security.userdetails.UserDetails;
import cn.topiam.employee.support.security.userdetails.UserType;
import java.util.Objects;
import java.util.Optional;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {
   public static final String ANONYMOUS_USER = "anonymousUser";
   // $FF: synthetic field
   private static final AuthenticationTrustResolver AUTHENTICATION_TRUST_RESOLVER = new AuthenticationTrustResolverImpl();

   public static boolean isCurrentUserInRole(String a) {
      Optional var10000 = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).map((ax) -> {
         String var10000 = a;
         Authentication var2 = ax;
         Authentication a = var10000;
         return var2.getAuthorities().stream().anyMatch((axx) -> {
            String var10000 = a;
            GrantedAuthority var2 = axx;
            GrantedAuthority a = var10000;
            return var2.getAuthority().equals(a);
         });
      });
      int var10001 = 3 ^ 3;
      boolean var10003 = true;
      return (Boolean)var10000.orElse(Boolean.valueOf((boolean)var10001));
   }

   public static String getCurrentUserName() {
      return (String)Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).map((a) -> {
         if (a.getPrincipal() instanceof UserDetails) {
            return ((UserDetails)a.getPrincipal()).getUsername();
         } else if (a.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            return ((org.springframework.security.core.userdetails.UserDetails)a.getPrincipal()).getUsername();
         } else {
            return a.getPrincipal() instanceof String ? (String)a.getPrincipal() : null;
         }
      }).orElse("anonymousUser");
   }

   // $FF: synthetic method
   private SecurityUtils() {
   }

   public static boolean isPrincipalAuthenticated(Authentication a) {
      if (a != null && !AnonymousAuthenticationToken.class.isAssignableFrom(a.getClass()) && a.isAuthenticated()) {
         int var10000 = 3 & 5;
         int var1 = 3 & 5;
         return (boolean)var10000;
      } else {
         boolean var10002 = true;
         return false;
      }
   }

   public static String getPrincipal(AbstractAuthenticationFailureEvent a) {
      AbstractAuthenticationFailureEvent var1 = a;
      AbstractAuthenticationFailureEvent var2 = (String)a.getAuthentication().getPrincipal();
      if (var1.getAuthentication().getPrincipal() instanceof String) {
         var2 = (String)var1.getAuthentication().getPrincipal();
      }

      if (var1.getAuthentication().getPrincipal() instanceof UserDetails || var1.getAuthentication().getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
         var2 = ((UserDetails)var1.getAuthentication().getPrincipal()).getUsername();
      }

      return var2;
   }

   public static UserDetails getCurrentUser() {
      return (UserDetails)Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).map((a) -> a.getPrincipal() instanceof UserDetails ? (UserDetails)a.getPrincipal() : null).orElse((Object)null);
   }

   public static String getCurrentUserId() {
      return (String)Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).map((a) -> {
         if (a.getPrincipal() instanceof UserDetails) {
            return ((UserDetails)a.getPrincipal()).getId();
         } else {
            return a.getPrincipal() instanceof String ? (String)a.getPrincipal() : null;
         }
      }).orElse("anonymousUser");
   }

   public static UserType getCurrentUserType() {
      return (UserType)Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).map((a) -> a.getPrincipal() instanceof UserDetails ? ((UserDetails)a.getPrincipal()).getUserType() : null).orElse((Object)null);
   }

   public static SecurityContext getSecurityContext() {
      return SecurityContextHolder.getContext();
   }

   public static boolean isAuthenticated() {
      Authentication var0;
      if (!Objects.isNull(var0 = getSecurityContext().getAuthentication()) && !AUTHENTICATION_TRUST_RESOLVER.isAnonymous(var0) && var0.isAuthenticated()) {
         int var10000 = 3 & 5;
         int var1 = 3 & 5;
         return (boolean)var10000;
      } else {
         boolean var10002 = true;
         return false;
      }
   }
}