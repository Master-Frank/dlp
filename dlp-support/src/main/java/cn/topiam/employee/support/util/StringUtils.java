package cn.topiam.employee.support.util;

import cn.topiam.employee.support.security.jackjson.GrantedAuthorityMixin;
import cn.topiam.employee.support.security.userdetails.Group;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringUtils {
   public static Pattern 1_1_1_ce = Pattern.compile(GrantedAuthorityMixin.1_1_1_ce("[h-g\u000eg\ng\r"));
   public static final String SPLIT_DEFAULT = ",";

   public static String escapeLike(String a) {
      return org.apache.commons.lang3.StringUtils.isEmpty(a) ? a : org.springframework.util.StringUtils.replace(org.springframework.util.StringUtils.replace(a, GrantedAuthorityMixin.1_1_1_ce("\""), Group.1_1_1_ce("tB")), GrantedAuthorityMixin.1_1_1_ce("X"), Group.1_1_1_ce("t8"));
   }

   // $FF: synthetic method
   private static byte __1_1_ce/* $FF was: 1_1_1_ce*/(char a) {
      return (byte)Group.1_1_1_ce("\u0018V\u001aT\u001cR\u001eP\u0010^i%k#m!").indexOf(a);
   }

   public static String byteToHexString(byte[] a) {
      StringBuilder var5 = new StringBuilder();
      byte[] var4;
      int var3 = (var4 = a).length;
      int var10000 = 3 >> 2;
      boolean var10002 = true;

      for(int var2 = var10000; var10000 < var3; var10000 = var2) {
         var10000 = var4[var2];
         int var10003 = 1;
         String var6;
         var10000 = (var6 = Integer.toHexString(var10000 & 255)).length();
         int var10001 = 3 >> 1;
         var10003 = 3 >> 1;
         if (var10000 == var10001) {
            var6 = "0" + var6;
         }

         ++var2;
         var5.append(var6.toUpperCase());
      }

      return var5.toString();
   }

   public static String replaceBlank(String a) {
      String var1 = a;
      a = "";
      if (var1 != null) {
         a = 1_1_1_ce.matcher(var1).replaceAll("");
      }

      return a;
   }

   public static byte[] hexStringToBytes(String a) {
      if (a != null && !a.isEmpty()) {
         String var10000 = a.toUpperCase();
         int var10001 = var10000.length();
         boolean var10004 = true;
         int var4 = var10001 / 2;
         char[] var2 = var10000.toCharArray();
         byte[] var7 = new byte[var4];
         int var10002 = 1;
         byte[] var3 = var7;
         int var8 = 3 & 4;
         var10002 = (boolean)1;

         for(String var6 = var8; var8 < var4; var8 = var6) {
            boolean var10003 = true;
            int var5 = var6 * 2;
            var10001 = var6;
            var10002 = 1_1_1_ce(var2[var5]);
            int var10005 = 1;
            var10002 <<= 4;
            var10005 = 2 ^ 3;
            int var10007 = 2 ^ 3;
            var10002 = (byte)(var10002 | 1_1_1_ce(var2[var5 + var10005]));
            ++var6;
            var3[var10001] = (byte)var10002;
         }

         return var3;
      } else {
         return null;
      }
   }

   public static Map<Character, Long> statisticsCharCount(String a) {
      return (Map)Stream.of(a).flatMap((ax) -> {
         String var5 = ax.toCharArray();
         ArrayList var2 = new ArrayList();
         char[] var6;
         int var4 = (var6 = var5).length;
         int var10000 = 3 & 4;
         boolean var10002 = true;

         for(int var3 = var10000; var10000 < var4; var10000 = var3) {
            var2.add(var6[var3++]);
         }

         return var2.stream();
      }).collect(Collectors.groupingBy(Character::charValue, Collectors.counting()));
   }
}
