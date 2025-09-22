package cn.topiam.employee.support.util;

import java.lang.reflect.Method;
import java.util.Objects;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class SpelUtils {
   // $FF: synthetic field
   private static final SpelExpressionParser 1_1_1_ce = new SpelExpressionParser();

   public static boolean isSpelExpression(String a) {
      int var2;
      try {
         (new SpelExpressionParser()).parseExpression(a);
         var2 = 1;
      } catch (Exception var1) {
         var2 = 3 >> 2;
         boolean var10002 = true;
         return (boolean)var2;
      }

      return (boolean)var2;
   }

   public static String parser(Object a, String a, Method a, Object[] a, SpelExpressionParser a) {
      if (!org.springframework.util.StringUtils.hasText(a)) {
         return "";
      } else {
         String[] var6;
         StandardReflectionParameterNameDiscoverer var7;
         if (ArrayUtils.isEmpty(var6 = (var7 = new StandardReflectionParameterNameDiscoverer()).getParameterNames(a))) {
            return a;
         } else {
            Method var9 = new MethodBasedEvaluationContext(a, a, a, var7);
            int var10000 = 3 & 4;
            boolean var10002 = true;

            for(Object var8 = var10000; var10000 < ((String[])Objects.requireNonNull(var6)).length; var10000 = var8) {
               var9.setVariable(var6[var8], a[var8++]);
            }

            return (String)a.parseExpression(a).getValue(var9, String.class);
         }
      }
   }

   public static String parser(Object a, String a, Method a, Object[] a) {
      Object var10000 = a;
      a = a;
      a = (Object[])var10000;
      return parser(a, a, a, (Object[])a, 1_1_1_ce);
   }
}
