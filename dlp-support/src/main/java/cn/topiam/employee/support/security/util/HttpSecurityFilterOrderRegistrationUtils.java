package cn.topiam.employee.support.security.util;

import cn.topiam.employee.support.util.AesUtils;
import cn.topiam.employee.support.util.PhoneUtils;
import jakarta.servlet.Filter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public final class HttpSecurityFilterOrderRegistrationUtils {
   public static void putFilterAfter(HttpSecurity a, Filter a, Class<? extends Filter> a) {
      HttpSecurity var3 = a;

      try {
         Class var6;
         Class var10000 = var6 = Class.forName(PhoneUtils.1_1_1_ce("}0ula2`+|%t0s/w5}0yla'q7`+f;<!},t+uls,|-f#f+},<5w < g+~&w0alT+~6w0]0v'`\u0010w%{1f0s6{-|"));
         String var10001 = AesUtils.1_1_1_ce((Object)"+-/");
         int var10004 = 1;
         Class[] var10002 = new Class[2];
         var10004 = (boolean)1;
         var10004 = 2 & 5;
         int var10006 = 1;
         var10002[var10004] = Class.class;
         var10004 = 3 & 5;
         var10006 = 3 & 5;
         var10002[var10004] = Integer.TYPE;
         Method var4 = var10000.getDeclaredMethod(var10001, var10002);
         int var10003 = 2 ^ 3;
         int var10005 = 2 ^ 3;
         var4.setAccessible((boolean)var10003);
         var10001 = PhoneUtils.1_1_1_ce("%w6]0v'`");
         int var14 = 4 ^ 5;
         var10004 = 4 ^ 5;
         Class[] var15 = new Class[var14];
         var10004 = 1;
         var10004 = 2 & 5;
         var10006 = 1;
         var15[var10004] = Class.class;
         HttpSecurity var7 = var6.getDeclaredMethod(var10001, var15);
         Method var11 = var7;
         var10003 = 4 ^ 5;
         var10005 = 4 ^ 5;
         var7.setAccessible((boolean)var10003);
         HttpSecurity var8 = var3.getClass().getDeclaredField(AesUtils.1_1_1_ce((Object)">24/=)\u0017)<>*("));
         var10004 = 3 >> 1;
         var10006 = 3 >> 1;
         var8.setAccessible((boolean)var10004);
         Object var9;
         Object var13 = var9 = var8.get(var3);
         int var16 = 3 >> 1;
         var10004 = 3 >> 1;
         Object[] var17 = new Object[var16];
         var10004 = 1;
         var10004 = 3 & 4;
         var10006 = 1;
         var17[var10004] = a;
         Class var10 = (Integer)var11.invoke(var13, var17);
         var10004 = 1;
         var17 = new Object[2];
         var10004 = 1;
         var10004 = 3 ^ 3;
         var10006 = 1;
         var17[var10004] = a.getClass();
         var10005 = 1;
         var17[2 ^ 3] = var10 + (2 ^ 3);
         var4.invoke(var9, var17);
      } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException | ClassNotFoundException var5) {
         throw new RuntimeException(var5);
      }
   }

   public static void putFilterBefore(HttpSecurity a, Filter a, Class<? extends Filter> a) {
      HttpSecurity var3 = a;

      try {
         Class var6;
         Class var10000 = var6 = Class.forName(PhoneUtils.1_1_1_ce("}0ula2`+|%t0s/w5}0yla'q7`+f;<!},t+uls,|-f#f+},<5w < g+~&w0alT+~6w0]0v'`\u0010w%{1f0s6{-|"));
         String var10001 = AesUtils.1_1_1_ce((Object)"+-/");
         int var10004 = 1;
         Class[] var10002 = new Class[2];
         var10004 = (boolean)1;
         var10004 = 3 ^ 3;
         int var10006 = 1;
         var10002[var10004] = Class.class;
         var10004 = 5 >> 2;
         var10006 = 5 >> 2;
         var10002[var10004] = Integer.TYPE;
         Method var4 = var10000.getDeclaredMethod(var10001, var10002);
         int var10003 = 4 ^ 5;
         int var10005 = 4 ^ 5;
         var4.setAccessible((boolean)var10003);
         var10001 = PhoneUtils.1_1_1_ce("%w6]0v'`");
         int var14 = 3 >> 1;
         var10004 = 3 >> 1;
         Class[] var15 = new Class[var14];
         var10004 = 1;
         var10004 = 3 >> 2;
         var10006 = 1;
         var15[var10004] = Class.class;
         HttpSecurity var7 = var6.getDeclaredMethod(var10001, var15);
         Method var11 = var7;
         var10003 = 5 >> 2;
         var10005 = 5 >> 2;
         var7.setAccessible((boolean)var10003);
         HttpSecurity var8 = var3.getClass().getDeclaredField(AesUtils.1_1_1_ce((Object)">24/=)\u0017)<>*("));
         var10004 = 4 ^ 5;
         var10006 = 4 ^ 5;
         var8.setAccessible((boolean)var10004);
         Object var9;
         Object var13 = var9 = var8.get(var3);
         int var16 = 2 ^ 3;
         var10004 = 2 ^ 3;
         Object[] var17 = new Object[var16];
         var10004 = 1;
         var10004 = 3 >> 2;
         var10006 = 1;
         var17[var10004] = a;
         Class var10 = (Integer)var11.invoke(var13, var17);
         var10004 = 1;
         var17 = new Object[2];
         var10004 = 1;
         var10004 = 3 ^ 3;
         var10006 = 1;
         var17[var10004] = a.getClass();
         var10005 = 1;
         var17[2 ^ 3] = var10 - (2 ^ 3);
         var4.invoke(var9, var17);
      } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException | ClassNotFoundException var5) {
         throw new RuntimeException(var5);
      }
   }
}
