package cn.topiam.employee.support.util;

import cn.topiam.employee.support.repository.page.domain.Page;
import java.lang.reflect.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityUtils {
   // $FF: synthetic field
   private static final Logger 1_1_1_ce = LoggerFactory.getLogger(EntityUtils.class);

   public static boolean isHasField(Object a, String a) {
      Object var6 = a.getClass().getDeclaredFields();
      int var10000 = 3 ^ 3;
      int var10002 = 1;
      int var3 = var10000;
      Field[] var7;
      int var4 = (var7 = var6).length;
      var10000 = 5 >> 3;
      var10002 = (boolean)1;

      for(int var5 = var10000; var10000 < var4; var10000 = var5) {
         if (var7[var5].getName().equals(a)) {
            var10000 = 4 ^ 5;
            var10002 = 4 ^ 5;
            return (boolean)var10000;
         }

         ++var5;
      }

      return (boolean)var3;
   }

   public static Object getPropertyValue(Object a, String a) {
      Object var3 = a;
      Class var2 = a.getClass();

      try {
         Object var5 = var2.getDeclaredField(a);
         int var10003 = 2 ^ 3;
         int var10005 = 2 ^ 3;
         var5.setAccessible((boolean)var10003);
         return var5.get(var3);
      } catch (IllegalAccessException | NoSuchFieldException var4) {
         Logger var10000 = 1_1_1_ce;
         String var10001 = Page.1_1_1_ce("莝厈q%W\u0003寓谿屴恹\u0010\u0005Q#w偢弨幦\u0010\u0005Q#w");
         int var10004 = 1;
         Object[] var10002 = new Object[3];
         var10004 = (boolean)1;
         var10004 = 3 ^ 3;
         int var10006 = 1;
         var10002[var10004] = var2.getName();
         var10004 = 5 >> 2;
         var10006 = 5 >> 2;
         var10002[var10004] = a;
         var10006 = 1;
         var10002[2] = ((ReflectiveOperationException)var4).getMessage();
         var10000.error(var10001, var10002);
         throw new RuntimeException(var4);
      }
   }
}
