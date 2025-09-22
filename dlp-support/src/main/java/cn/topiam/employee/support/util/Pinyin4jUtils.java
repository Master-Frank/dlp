package cn.topiam.employee.support.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Pinyin4jUtils {
   public static String makeStringByStringSet(Set<String> a) {
      Set var1 = a;
      Set var5 = new StringBuilder();
      int var10000 = 3 >> 2;
      int var10002 = 1;
      int var4 = var10000;
      Iterator var3;
      if (!a.isEmpty()) {
         for(Iterator var6 = var3 = a.iterator(); var6.hasNext(); var6 = var3) {
            String var2 = (String)var3.next();
            int var10001 = var1.size();
            var10002 = 3 >> 1;
            int var10004 = 3 >> 1;
            if (var4 == var10001 - var10002) {
               var5.append(var2);
            } else {
               var5.append(var2).append(",");
            }

            ++var4;
         }
      }

      return var5.toString().toLowerCase();
   }

   public static Set<String> getPinyin(String a, boolean a) {
      if (a != null && !"".equalsIgnoreCase(a.trim())) {
         char[] var5 = a.toCharArray();
         HanyuPinyinOutputFormat var4;
         HanyuPinyinOutputFormat var10001 = var4 = new HanyuPinyinOutputFormat();
         var10001.setCaseType(HanyuPinyinCaseType.LOWERCASE);
         var10001.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
         var10001.setVCharType(HanyuPinyinVCharType.WITH_V);
         String[][] var10000 = new String[a.length()][];
         int var10002 = 1;
         String[][] var3 = var10000;
         int var13 = 5 >> 3;
         var10002 = (boolean)1;

         for(String var11 = var13; var13 < var5.length; var13 = var11) {
            char var6;
            if (String.valueOf(var6 = var5[var11]).matches(VersionUtils.1_1_1_ce("s\f]dm`\u0018}t%\u0011\u0016ieu{"))) {
               try {
                  var3[var11] = PinyinHelper.toHanyuPinyinStringArray(var5[var11], var4);
                  if (!a && var11 != 0) {
                     String[] var14 = new String[var3[var11].length];
                     var10002 = (boolean)1;
                     String[] var7 = var14;
                     int var15 = 3 & 4;
                     var10002 = (boolean)1;

                     for(int var8 = var15; var15 < var3[var11].length; var15 = var8) {
                        String var16 = var3[var11][var8];
                        var10002 = 3 ^ 3;
                        boolean var35 = true;
                        char var9 = var16.charAt(var10002);
                        int var17 = var8;
                        String var26 = Character.toString(var9);
                        ++var8;
                        var7[var17] = var26;
                     }

                     var3[var11] = var7;
                  }
               } catch (BadHanyuPinyinOutputFormatCombination var10) {
                  throw new RuntimeException(var10);
               }
            } else {
               label67: {
                  label66: {
                     boolean var10003 = true;
                     if (var6 >= 'A') {
                        var10003 = true;
                        if (var6 <= 'Z') {
                           break label66;
                        }
                     }

                     var10003 = true;
                     if (var6 >= 'a') {
                        var10003 = true;
                        if (var6 <= 'z') {
                           break label66;
                        }
                     }

                     var10002 = 2 ^ 3;
                     int var10004 = 2 ^ 3;
                     String[] var20 = new String[var10002];
                     var10004 = 1;
                     var10004 = 2 & 5;
                     boolean var10006 = true;
                     var20[var10004] = "";
                     var3[var11] = var20;
                     break label67;
                  }

                  var10002 = 5 >> 2;
                  int var32 = 5 >> 2;
                  String[] var22 = new String[var10002];
                  var32 = 1;
                  var32 = 3 & 4;
                  boolean var36 = true;
                  var22[var32] = String.valueOf(var5[var11]);
                  var3[var11] = var22;
               }
            }

            ++var11;
         }

         a = exchange(var3);
         return new HashSet(Arrays.asList(a));
      } else {
         return null;
      }
   }

   public static String getFirstSpellPinYin(String a, boolean a) {
      String var10000 = a;
      boolean var3 = (boolean)a;
      int a = (int)var10000;
      String[] var2;
      String var4;
      int var6 = (var2 = (var4 = makeStringByStringSet(getPinyin(a, var3))).split(",")).length;
      int var10001 = 3 >> 1;
      int var10003 = 3 >> 1;
      if (var6 > var10001) {
         var10001 = 3 ^ 3;
         var10003 = 1;
         var4 = var2[var10001];
      }

      return var4;
   }

   public static String getPinYinHeadChar(String a) {
      String var1 = a;
      String var5 = new StringBuilder();
      int var10000 = 5 >> 3;
      int var10002 = 1;

      for(int var2 = var10000; var10000 < var1.length(); var10000 = var2) {
         char var3;
         String[] var4;
         if ((var4 = PinyinHelper.toHanyuPinyinStringArray(var3 = var1.charAt(var2))) != null) {
            var10002 = 3 ^ 3;
            boolean var10004 = true;
            String var10001 = var4[var10002];
            var10002 = 3 ^ 3;
            var10004 = true;
            var5.append(var10001.charAt(var10002));
         } else {
            var5.append(var3);
         }

         ++var2;
      }

      return var5.toString();
   }

   public static String[] exchange(String[][] a) {
      String[][] var10000 = 1_1_1_ce(a);
      int var10001 = 5 >> 3;
      boolean var10003 = true;
      return var10000[var10001];
   }

   // $FF: synthetic method
   private static String[][] __1_1_ce/* $FF was: 1_1_1_ce*/(String[][] a) {
      String[][] var1 = a;
      int var6;
      int var10000 = var6 = a.length;
      int var10002 = -22510;
      int var10003 = 3566;
      int var10004 = 9731;
      boolean var10005 = true;
      var10003 = (boolean)1;
      if (var10000 < 2) {
         return a;
      } else {
         int var10001 = 2 & 5;
         var10003 = (boolean)1;
         int var2 = a[var10001].length;
         var10001 = 2 ^ 3;
         var10003 = 2 ^ 3;
         int var3 = a[var10001].length;
         String[] var10 = new String[var2 * var3];
         var10002 = (boolean)1;
         String[] var4 = var10;
         int var11 = 5 >> 3;
         var10002 = (boolean)1;
         int var5 = var11;
         var11 = 3 ^ 3;
         var10002 = (boolean)1;

         for(String[][] var8 = var11; var11 < var2; var11 = var8) {
            var11 = 5 >> 3;
            var10002 = (boolean)1;

            for(int var7 = var11; var11 < var3; var11 = var7) {
               var10001 = var5;
               var10003 = 5 >> 3;
               var10005 = true;
               String var22 = var1[var10003][var8];
               var10004 = 2 ^ 3;
               int var10006 = 2 ^ 3;
               var22 = var22 + var1[var10004][var7];
               ++var5;
               var4[var10001] = var22;
               ++var7;
            }

            ++var8;
         }

         var10001 = 3 >> 1;
         var10003 = 3 >> 1;
         String[][] var14 = new String[var6 - var10001][];
         var10002 = (boolean)1;
         a = var14;
         var10003 = 1;
         boolean var35 = true;
         System.arraycopy(var1, 2, a, 4 ^ 5, var6 - 2);
         var10002 = 3 >> 2;
         var10004 = 1;
         a[var10002] = var4;
         return 1_1_1_ce(a);
      }
   }
}
