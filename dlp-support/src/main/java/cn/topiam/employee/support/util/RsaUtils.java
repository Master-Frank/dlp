package cn.topiam.employee.support.util;

import cn.topiam.employee.support.context.ApplicationContextService;
import cn.topiam.employee.support.repository.page.domain.PageModel;
import jakarta.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RsaUtils {
   // $FF: synthetic field
   @Generated
   private static final Logger G;
   // $FF: synthetic field
   private static final String D = "RSA";
   // $FF: synthetic field
   public static final boolean b;
   public static final String PUBLIC_KEY = "PUBLIC_KEY";
   public static final String PRIVATE_KEY = "PRIVATE_KEY";
   // $FF: synthetic field
   private static final Integer 1_1_1_ce;

   public static String encrypt(RSAPublicKey a, byte[] a) throws Exception {
      RSAPublicKey var10000 = a;
      byte[] var8 = a;
      byte[] a = (byte[])var10000;
      if (a == null) {
         throw new Exception(PageModel.1_1_1_ce("勌宝儀链乖稡@{讛该缂"));
      } else {
         try {
            Cipher var2 = Cipher.getInstance("RSA");
            int var10003 = 3 >> 1;
            int var10005 = 3 >> 1;
            var2.init(var10003, a);
            return base64ToStr(var2.doFinal(var8));
         } catch (NoSuchAlgorithmException var3) {
            throw new Exception(ApplicationContextService.1_1_1_ce("方欪勹守篎沛"));
         } catch (NoSuchPaddingException var4) {
            G.error(PageModel.1_1_1_ce(" \u0011"), var4.getStackTrace());
            return null;
         } catch (InvalidKeyException var5) {
            throw new Exception(ApplicationContextService.1_1_1_ce("勹守儵铫震沛u讹梙枫"));
         } catch (IllegalBlockSizeException var6) {
            throw new Exception(PageModel.1_1_1_ce("晕旫锤廊霅油"));
         } catch (BadPaddingException var7) {
            throw new Exception(ApplicationContextService.1_1_1_ce("晀旞放挷嶼挆圁"));
         }
      }
   }

   public static RSAPrivateKey loadPrivateKey(String a) throws Exception {
      String var1 = a;

      try {
         String var6 = DatatypeConverter.parseBase64Binary(var1);
         PKCS8EncodedKeySpec var2 = new PKCS8EncodedKeySpec(var6);
         return (RSAPrivateKey)KeyFactory.getInstance("RSA").generatePrivate(var2);
      } catch (NoSuchAlgorithmException var3) {
         throw new Exception(ApplicationContextService.1_1_1_ce("方欪篎沛"));
      } catch (InvalidKeySpecException var4) {
         throw new Exception(PageModel.1_1_1_ce("禚铉霅油"));
      } catch (NullPointerException var5) {
         throw new Exception(ApplicationContextService.1_1_1_ce("禘铫攩挠乣稴"));
      }
   }

   public static String encrypt(RSAPrivateKey a, byte[] a) throws Exception {
      RSAPrivateKey var10000 = a;
      byte[] var8 = a;
      byte[] a = (byte[])var10000;
      if (a == null) {
         throw new Exception(PageModel.1_1_1_ce("勌宝禭链乖稡@{讛该缂"));
      } else {
         try {
            Cipher var2 = Cipher.getInstance("RSA");
            int var10003 = 5 >> 2;
            int var10005 = 5 >> 2;
            var2.init(var10003, a);
            return base64ToStr(var2.doFinal(var8));
         } catch (NoSuchAlgorithmException var3) {
            throw new Exception(ApplicationContextService.1_1_1_ce("方欪勹守篎沛"));
         } catch (NoSuchPaddingException var4) {
            G.error(PageModel.1_1_1_ce(" \u0011"), var4.getStackTrace());
            return null;
         } catch (InvalidKeyException var5) {
            throw new Exception(ApplicationContextService.1_1_1_ce("勹守禘铫震沛u讹梙枫"));
         } catch (IllegalBlockSizeException var6) {
            throw new Exception(PageModel.1_1_1_ce("晕旫锤廊霅油"));
         } catch (BadPaddingException var7) {
            throw new Exception(ApplicationContextService.1_1_1_ce("晀旞放挷嶼挆圁"));
         }
      }
   }

   public static String base64ToStr(byte[] a) {
      return DatatypeConverter.printBase64Binary(a);
   }

   public static byte[] strToBase64(String a) {
      return DatatypeConverter.parseBase64Binary(a);
   }

   static {
      int var10000;
      if (!RsaUtils.class.desiredAssertionStatus()) {
         var10000 = 4 ^ 5;
         int var10002 = 4 ^ 5;
      } else {
         var10000 = 0;
         boolean var0 = true;
      }

      b = (boolean)var10000;
      G = LoggerFactory.getLogger(RsaUtils.class);
      boolean var1 = true;
      1_1_1_ce = 2048;
   }

   public static String decrypt(RSAPublicKey a, byte[] a) throws Exception {
      RSAPublicKey var10000 = a;
      byte[] var8 = a;
      byte[] a = (byte[])var10000;
      if (a == null) {
         throw new Exception(PageModel.1_1_1_ce("規宝儀链乖稡@{讛该缂"));
      } else {
         try {
            Cipher var2 = Cipher.getInstance("RSA");
            boolean var10005 = true;
            var2.init(2, a);
            var8 = var2.doFinal(var8);
            return new String(var8);
         } catch (NoSuchAlgorithmException var3) {
            throw new Exception(ApplicationContextService.1_1_1_ce("方欪覺守篎沛"));
         } catch (NoSuchPaddingException var4) {
            G.error(PageModel.1_1_1_ce(" \u0011"), var4.getStackTrace());
            return null;
         } catch (InvalidKeyException var5) {
            throw new Exception(ApplicationContextService.1_1_1_ce("覺守儵铫震沛u讹梙枫"));
         } catch (IllegalBlockSizeException var6) {
            throw new Exception(PageModel.1_1_1_ce("宝旫锤廊霅油"));
         } catch (BadPaddingException var7) {
            throw new Exception(ApplicationContextService.1_1_1_ce("守旞放挷嶼挆圁"));
         }
      }
   }

   public static String decrypt(RSAPrivateKey a, byte[] a) throws Exception {
      RSAPrivateKey var10000 = a;
      byte[] var8 = a;
      byte[] a = (byte[])var10000;
      if (a == null) {
         throw new Exception(PageModel.1_1_1_ce("規宝禭链乖稡@{讛该缂"));
      } else {
         try {
            Cipher var2 = Cipher.getInstance("RSA");
            boolean var10005 = true;
            var2.init(2, a);
            var8 = var2.doFinal(var8);
            return new String(var8);
         } catch (NoSuchAlgorithmException var3) {
            throw new Exception(ApplicationContextService.1_1_1_ce("方欪覺守篎沛"));
         } catch (NoSuchPaddingException var4) {
            G.error(PageModel.1_1_1_ce(" \u0011"), var4.getStackTrace());
            return null;
         } catch (InvalidKeyException var5) {
            throw new Exception(ApplicationContextService.1_1_1_ce("覺守禘铫震沛u讹梙枫"));
         } catch (IllegalBlockSizeException var6) {
            throw new Exception(PageModel.1_1_1_ce("宝旫锤廊霅油"));
         } catch (BadPaddingException var7) {
            throw new Exception(ApplicationContextService.1_1_1_ce("守旞放挷嶼挆圁"));
         }
      }
   }

   public static RSAPublicKey loadPublicKey(String a) throws Exception {
      String var1 = a;

      try {
         String var7 = DatatypeConverter.parseBase64Binary(var1);
         KeyFactory var2 = KeyFactory.getInstance("RSA");
         X509EncodedKeySpec var3 = new X509EncodedKeySpec(var7);
         return (RSAPublicKey)var2.generatePublic(var3);
      } catch (NoSuchAlgorithmException var4) {
         throw new Exception(PageModel.1_1_1_ce("斻欈篌油"));
      } catch (InvalidKeySpecException var5) {
         throw new Exception(ApplicationContextService.1_1_1_ce("儵铫震沛"));
      } catch (NullPointerException var6) {
         throw new Exception(PageModel.1_1_1_ce("儷铉攫挂乡稖"));
      }
   }

   public static RsaResult getKeys() {
      return getKeys(1_1_1_ce);
   }

   public static RsaResult getKeys(int a) {
      if (a < 1_1_1_ce) {
         throw new IllegalArgumentException(PageModel.1_1_1_ce("\t?\u001aL宝铉锤廊乖屽仕Li\\oT{伡"));
      } else {
         KeyPairGenerator var1 = null;

         try {
            var1 = KeyPairGenerator.getInstance("RSA");
         } catch (NoSuchAlgorithmException var3) {
            G.error(ApplicationContextService.1_1_1_ce("\"3"), var3.getStackTrace());
         }

         if (!b && var1 == null) {
            throw new AssertionError();
         } else {
            var1.initialize(a, new SecureRandom());
            int var4 = var1.generateKeyPair();
            return new RsaResult(var4.getPrivate(), var4.getPublic());
         }
      }
   }

   public static class RsaResult {
      // $FF: synthetic field
      private PrivateKey b;
      // $FF: synthetic field
      private PublicKey 1_1_1_ce;

      @Generated
      public String toString() {
         String var10000 = String.valueOf(a.getPrivateKey());
         return "RsaUtils.RsaResult(privateKey=" + var10000 + ", publicKey=" + String.valueOf(a.getPublicKey()) + ")";
      }

      @Generated
      public void setPrivateKey(PrivateKey a) {
         RsaResult var10000 = a;
         PrivateKey var2 = a;
         PrivateKey a = var10000;
         a.b = var2;
      }

      @Generated
      public RsaResult(PrivateKey a, PublicKey a) {
         RsaResult var10000 = a;
         PublicKey var3 = a;
         PublicKey a = var10000;
         a.<init>();
         a.b = a;
         a.1_1_1_ce = var3;
      }

      @Generated
      public void setPublicKey(PublicKey a) {
         RsaResult var10000 = a;
         PublicKey var2 = a;
         PublicKey a = var10000;
         a.1_1_1_ce = var2;
      }

      @Generated
      public int hashCode() {
         RsaResult var2 = a;
         int var10000 = 59;
         int var10002 = 1;
         var10000 = 5 >> 2;
         var10002 = 5 >> 2;
         int var1 = var10000;
         RsaResult var3 = a.getPrivateKey();
         boolean var10003 = true;
         var10000 = var1 * 59;
         int var10001;
         if (var3 == null) {
            var10001 = 43;
            var10003 = true;
         } else {
            var10001 = var3.hashCode();
         }

         var1 = var10000 + var10001;
         RsaResult var4 = var2.getPublicKey();
         var10003 = true;
         var10000 = var1 * 59;
         if (var4 == null) {
            var10001 = 43;
            var10003 = true;
         } else {
            var10001 = var4.hashCode();
         }

         return var10000 + var10001;
      }

      @Generated
      public boolean equals(Object a) {
         RsaResult var10000 = a;
         RsaResult var4 = (RsaResult)a;
         a = var10000;
         if (var4 == a) {
            int var11 = 3 & 5;
            int var16 = 3 & 5;
            return (boolean)var11;
         } else if (!(var4 instanceof RsaResult)) {
            int var10 = 3 ^ 3;
            boolean var15 = true;
            return (boolean)var10;
         } else if (!(var4 = var4).canEqual(a)) {
            int var9 = 2 & 5;
            boolean var14 = true;
            return (boolean)var9;
         } else {
            label44: {
               PrivateKey var2 = ((RsaResult)a).getPrivateKey();
               PrivateKey var3 = var4.getPrivateKey();
               if (var2 == null) {
                  if (var3 != null) {
                     break label44;
                  }
               } else if (!var2.equals(var3)) {
                  break label44;
               }

               label30: {
                  PublicKey var8 = ((RsaResult)a).getPublicKey();
                  RsaResult var6 = var4.getPublicKey();
                  if (var8 == null) {
                     if (var6 != null) {
                        break label30;
                     }
                  } else if (!var8.equals(var6)) {
                     break label30;
                  }

                  boolean var10002 = true;
                  return true;
               }

               boolean var12 = true;
               return false;
            }

            boolean var13 = true;
            return false;
         }
      }

      @Generated
      public PublicKey getPublicKey() {
         return a.1_1_1_ce;
      }

      @Generated
      public boolean canEqual(Object a) {
         return a instanceof RsaResult;
      }

      @Generated
      public PrivateKey getPrivateKey() {
         return a.b;
      }
   }
}
