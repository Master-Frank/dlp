package cn.topiam.employee.support.util;

import cn.topiam.employee.support.H;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.NonNull;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlUtils {
   // $FF: synthetic field
   private static final String b = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()";
   // $FF: synthetic field
   private static final Logger 1_1_1_ce = LoggerFactory.getLogger(UrlUtils.class);

   public static String format(String a) {
      return a.replaceAll(H.1_1_1_ce("y\u001am\u0004yM%Q!\u001f-M%Q!Vk\fx\nz"), "/");
   }

   public static boolean testUrlWithTimeOut(String a, int a) {
      String var2 = a;

      int var5;
      try {
         URLConnection var4 = (new URL(var2)).openConnection();
         var4.setConnectTimeout(a);
         var4.connect();
         var5 = 1;
      } catch (Exception var3) {
         1_1_1_ce.error(H.1_1_1_ce("q4V%\u0005%M4\u0005\u0004w\u001d\u0005%L<@>P%\u00054]2@!Q8J?\u001fq^,"), var3.getMessage());
         var5 = 5 >> 3;
         boolean var10002 = true;
         return (boolean)var5;
      }

      return (boolean)var5;
   }

   // $FF: synthetic method
   private static String __1_1_ce/* $FF was: 1_1_1_ce*/(byte[] a) {
      int var10002 = a.length;
      boolean var10005 = true;
      byte[] var6 = new StringBuilder(var10002 * 3);
      byte[] var5 = a;
      int var4 = a.length;
      int var10000 = 3 & 4;
      var10002 = 1;

      for(int var3 = var10000; var10000 < var4; var10000 = var3) {
         byte var10001 = var5[var3];
         boolean var10004 = true;
         int var2 = var10001 & 255;
         var6.append(H.1_1_1_ce("t"));
         boolean var10003 = true;
         if (var2 < 16) {
            var6.append(PhoneUtils.1_1_1_ce("\""));
         }

         long var7 = (long)var2;
         var10004 = true;
         ++var3;
         var6.append(Long.toString(var7, 16).toUpperCase());
      }

      return var6.toString();
   }

   public static String encodeUriComponent(String a) {
      String var2 = a;
      if (org.apache.commons.lang3.StringUtils.isEmpty(a)) {
         return a;
      } else {
         int var4 = a.length();
         int var10005 = 1;
         String var6 = new StringBuilder(var4 * 3);
         int var10000 = 3 >> 2;
         boolean var10002 = true;

         for(int var3 = var10000; var10000 < var4; var10000 = var3) {
            int var10003 = 5 >> 2;
            var10005 = 5 >> 2;
            String var1 = var2.substring(var3, var3 + var10003);
            if (!"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()".contains(var1)) {
               byte[] var5 = var1.getBytes(StandardCharsets.UTF_8);
               var6.append(1_1_1_ce(var5));
            } else {
               var6.append(var1);
            }

            ++var3;
         }

         return var6.toString();
      }
   }

   public static String integrate(@NonNull String basicUrl, Map<String, String> parameters) {
      if (basicUrl == null) {
         throw new NullPointerException(PhoneUtils.1_1_1_ce("p#a+q\u0017`.2+ab\u007f#`)w&2,},?,g.~bp7fb{12,g.~"));
      } else {
         String var3 = (new StringBuilder(basicUrl)).append(H.1_1_1_ce("n"));
         if (MapUtils.isNotEmpty(parameters)) {
            parameters.forEach((a, ax) -> {
               StringBuilder var10000 = var3;
               String var3x = ax;
               String a = var10000;
               ((StringBuilder)a).append(PhoneUtils.1_1_1_ce("4")).append(a).append(H.1_1_1_ce("l")).append(var3x);
            });
         }

         int var10002 = var3.indexOf(PhoneUtils.1_1_1_ce("}4"));
         int var10003 = 2 ^ 3;
         int var10005 = 2 ^ 3;
         var3.deleteCharAt(var10002 + var10003);
         return var3.toString();
      }
   }
}
