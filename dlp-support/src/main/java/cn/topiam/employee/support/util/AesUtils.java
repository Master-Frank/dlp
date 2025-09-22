package cn.topiam.employee.support.util;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AesUtils {
   // $FF: synthetic field
   private final String b;
   // $FF: synthetic field
   private static final String 1_1_1_ce = "AES";

   // $FF: synthetic method
   private static SecretKeySpec __1_1_ce/* $FF was: 1_1_1_ce*/(String a) {
      return new SecretKeySpec(Base64.getDecoder().decode(a), "AES");
   }

   public String decrypt(String a) {
      AesUtils var2 = a;

      try {
         return org.springframework.util.StringUtils.hasText(a) ? decrypt(a, var2.b) : null;
      } catch (Throwable var3) {
         throw var3;
      }
   }

   public static String encrypt(String a, String a) {
      String var2 = a;

      try {
         String var4 = 1_1_1_ce(a);
         Cipher var5;
         Cipher var10000 = var5 = Cipher.getInstance("AES");
         int var10001 = 3 >> 1;
         int var10003 = 3 >> 1;
         var10000.init(var10001, var4);
         return Base64.getEncoder().encodeToString(var5.doFinal(var2.getBytes(StandardCharsets.UTF_8)));
      } catch (Throwable var3) {
         throw var3;
      }
   }

   public static String decrypt(String a, String a) {
      String var2 = a;

      try {
         String var4 = 1_1_1_ce(a);
         Cipher var5;
         Cipher var10000 = var5 = Cipher.getInstance("AES");
         boolean var10003 = true;
         var10000.init(2, var4);
         return new String(var5.doFinal(Base64.getDecoder().decode(var2)), StandardCharsets.UTF_8);
      } catch (Throwable var3) {
         throw var3;
      }
   }

   public AesUtils(String a) {
      AesUtils var10000 = a;
      String var2 = a;
      String a = var10000;
      a.<init>();
      a.b = var2;
   }

   public static String generateKey() {
      try {
         KeyGenerator var10000 = KeyGenerator.getInstance("AES");
         boolean var10004 = true;
         var10000.init(128, new SecureRandom());
         SecretKey var1 = var10000.generateKey();
         return Base64.getEncoder().encodeToString(var1.getEncoded());
      } catch (Throwable var2) {
         throw var2;
      }
   }

   public static String __1_1_ce/* $FF was: 1_1_1_ce*/(Object a) {
      int var10000 = 5 << 4 ^ 2 << 2 ^ 3;
      int var10001 = (3 ^ 5) << 4 ^ (3 ^ 5) << (2 ^ 3);
      int var10002 = 5 << 4 ^ 4 << (3 & 5);
      String var3;
      int var10003 = (var3 = (String)a).length();
      char[] var10004 = new char[var10003];
      boolean var10006 = true;
      int var5;
      int var10 = var5 = var10003 - (5 >> 2);
      char[] var1 = var10004;
      int var4 = var10002;
      var10000 = var10;

      for(int var2 = var10000; var10000 >= 0; var10000 = var5) {
         var10001 = var5;
         var10002 = var3.charAt(var5);
         --var5;
         var1[var10001] = (char)(var10002 ^ var2);
         if (var5 < 0) {
            break;
         }

         var10002 = var5--;
         var1[var10002] = (char)(var3.charAt(var10002) ^ var4);
      }

      return new String(var1);
   }

   public String encrypt(String a) {
      AesUtils var2 = a;

      try {
         return org.springframework.util.StringUtils.hasText(a) ? encrypt(a, var2.b) : null;
      } catch (Throwable var3) {
         throw var3;
      }
   }
}
