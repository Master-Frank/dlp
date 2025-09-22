package cn.topiam.employee.support.security.util;

import cn.topiam.employee.support.security.jackjson.GrantedAuthorityMixin;
import java.util.Random;

public final class ValidateCodeUtils {
   // $FF: synthetic field
   private static final Integer G;
   // $FF: synthetic field
   private static final Integer D;
   // $FF: synthetic field
   private static final Integer b;
   // $FF: synthetic field
   private static final Integer 1_1_1_ce;

   public static String generateValidateCode4String(int a) {
      String var10000 = Integer.toHexString((new Random()).nextInt());
      int var10001 = 3 & 4;
      boolean var10003 = true;
      return var10000.substring(var10001, a);
   }

   static {
      boolean var10002 = true;
      D = 4;
      var10002 = true;
      1_1_1_ce = 1000;
      var10002 = true;
      G = 4;
      b = 100000;
   }

   public static Integer generateValidateCode(int a) {
      int var5;
      if (a == D) {
         Random var10000 = new Random();
         boolean var10003 = true;
         if ((a = var10000.nextInt(9999)) < 1_1_1_ce) {
            var10003 = true;
            int var3;
            var5 = var3 = a + 1000;
            return var5;
         }
      } else {
         if (a != G) {
            throw new RuntimeException(GrantedAuthorityMixin.1_1_1_ce("叱胺甄戗/佊戍1佖敷孌骋诚砆"));
         }

         if ((a = (new Random()).nextInt(999999)) < b) {
            int var4;
            var5 = var4 = a + 100000;
            return var5;
         }
      }

      var5 = a;
      return var5;
   }
}
