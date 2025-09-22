package cn.topiam.employee.support.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.io.ClassPathResource;

public class AvatarUtils {
   public static final String RANDOM_AVATAR = "https://api.multiavatar.com/";

   public static BufferedImage generateAvatarImg(String a) {
      String var2 = a;

      try {
         int var10002 = 1;
         byte var4 = 100;
         var10002 = (boolean)1;
         String var9 = 100;
         int var3;
         int var10000 = var3 = var2.length();
         int var10003 = 1;
         String var1;
         if (var10000 <= 2) {
            var1 = var2;
         } else {
            int var10001 = 2 & 5;
            var10003 = (boolean)1;
            var10002 = 4 ^ 5;
            int var10004 = 4 ^ 5;
            if (isChinese(var2.substring(var10001, var10002))) {
               var10004 = 1;
               var1 = var2.substring(var3 - 2);
            } else {
               var10001 = 3 ^ 3;
               var10003 = (boolean)1;
               var10004 = 1;
               var1 = var2.substring(var10001, 2).toUpperCase();
            }
         }

         BufferedImage var5;
         label49: {
            int var46 = 3 & 5;
            int var10006 = 3 & 5;
            var14 = (Graphics2D)(var5 = new BufferedImage(var4, var9, var46)).getGraphics();
            var10002 = 3 ^ 3;
            var14.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            var14.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            var14.setBackground(1_1_1_ce());
            var46 = 1;
            var10003 = 3 & 4;
            int var10005 = 1;
            var14.clearRect(var10002, var10003, var4, var9);
            var14.setPaint(Color.WHITE);
            String var10 = (new ClassPathResource(DesensitizationUtils.1_1_1_ce("\u001dY]QFL\u001d~^VP^P^bJzJ[k[\u0012\u0000\u0012\u0007\n\u001fmWXGSSM\u001cKFY"))).getInputStream();
            var10000 = 3 ^ 3;
            var10002 = 1;
            var11 = Font.createFont(var10000, var10);
            var10000 = var1.length();
            var10003 = 1;
            if (var10000 >= 2) {
               int var21 = 4 ^ 5;
               var10003 = 4 ^ 5;
               var11 = var11.deriveFont(var21, 30.0F);
               var14.setFont(var11);
               var10002 = 3 & 4;
               var46 = 1;
               var10003 = 5 >> 2;
               var10005 = 5 >> 2;
               String var6 = var1.substring(var10002, var10003);
               var10002 = 1;
               String var7 = var1.substring(3 & 5, 2);
               if (isChinese(var6) && isChinese(var7)) {
                  var17 = var1;
                  var10005 = 1;
                  var10006 = 1;
                  var14.drawString(var1, 20, 60);
                  break label49;
               }

               if (isChinese(var6) && !isChinese(var7)) {
                  var17 = var1;
                  var10005 = 1;
                  var10006 = 1;
                  var14.drawString(var1, 27, 60);
                  break label49;
               }

               var21 = 5 >> 3;
               var10003 = 1;
               var10002 = 3 >> 1;
               var46 = 3 >> 1;
               var1 = var1.substring(var21, var10002);
            }

            var17 = var1;
         }

         label39: {
            var10000 = var17.length();
            int var23 = 5 >> 2;
            var10003 = 5 >> 2;
            if (var10000 == var23) {
               if (isChinese(var1)) {
                  var23 = 3 & 4;
                  var10003 = 1;
                  String var13 = var11.deriveFont(var23, 50.0F);
                  var19 = var5;
                  var14.setFont(var13);
                  boolean var55 = true;
                  boolean var58 = true;
                  var14.drawString(var1, 25, 70);
                  break label39;
               }

               var10003 = 1;
               String var12 = var11.deriveFont(0, 55.0F);
               var14.setFont(var12);
               String var24 = var1.toUpperCase();
               boolean var50 = true;
               boolean var54 = true;
               var14.drawString(var24, 33, 67);
            }

            var19 = var5;
         }

         var10003 = 1;
         return makeRoundedCorner(var19, 99);
      } catch (Exception var8) {
         throw new RuntimeException(var8);
      }
   }

   public static String bufferedImageToBase64(BufferedImage a) {
      BufferedImage var1 = a;

      try {
         BufferedImage var3 = new ByteArrayOutputStream();
         ImageIO.write(var1, AesUtils.1_1_1_ce((Object)"+6<"), var3);
         return "data:image/png;base64," + (new Base64()).encodeToString(var3.toByteArray());
      } catch (IOException var2) {
         throw new RuntimeException(var2);
      }
   }

   public static boolean isChinese(String a) {
      return Pattern.compile(AesUtils.1_1_1_ce((Object)"\u0003\u0007-o=khv\u0004.a=9n\u0005p")).matcher(a).find();
   }

   public static BufferedImage makeRoundedCorner(BufferedImage a, int a) {
      BufferedImage var2 = a;
      int var4 = a.getWidth();
      int var3 = a.getHeight();
      boolean var10006 = true;
      BufferedImage var10000 = new BufferedImage(var4, var3, 2);
      BufferedImage var5 = var10000.createGraphics();
      int var10003 = 3 & 4;
      var5.setComposite(AlphaComposite.Src);
      var5.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      var5.setColor(Color.WHITE);
      var5.fill(new RoundRectangle2D.Float(0.0F, 0.0F, (float)var4, (float)var3, (float)a, (float)a));
      var5.setComposite(AlphaComposite.SrcAtop);
      boolean var10005 = true;
      int var10004 = 5 >> 3;
      var10006 = true;
      var5.drawImage(var2, var10003, var10004, (ImageObserver)null);
      var5.dispose();
      return var10000;
   }

   // $FF: synthetic method
   private static Color __1_1_ce/* $FF was: 1_1_1_ce*/() {
      int var10000 = 5 >> 2;
      int var10002 = 5 >> 2;
      String[] var4 = new String[var10000];
      var10002 = 1;
      var10002 = 3 & 4;
      int var10004 = 1;
      var4[var10002] = DesensitizationUtils.1_1_1_ce("\u0000\r\u001e\u000e\u0003\u0006\u001e\r\u0007\n");
      String[] var0 = var4;
      int var1 = var4.length;
      Random var2 = new Random();
      String[] var3 = var0[var2.nextInt(var1)].split(",");
      int var10003 = 3 ^ 3;
      boolean var10005 = true;
      var10002 = Integer.parseInt(var3[var10003]);
      var10004 = 3 >> 1;
      int var10006 = 3 >> 1;
      var10003 = Integer.parseInt(var3[var10004]);
      boolean var10007 = true;
      return new Color(var10002, var10003, Integer.parseInt(var3[2]));
   }

   public static String getRandomAvatar() {
      boolean var10002 = true;
      return "https://api.multiavatar.com/" + RandomStringUtils.randomAlphanumeric(6) + ".svg";
   }
}
