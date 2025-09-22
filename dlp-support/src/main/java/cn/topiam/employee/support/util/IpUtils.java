package cn.topiam.employee.support.util;

import cn.topiam.employee.support.repository.page.domain.ExampleRequest;
import com.google.common.net.InetAddresses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.Generated;
import org.apache.commons.net.util.SubnetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IpUtils {
   // $FF: synthetic field
   @Generated
   private static final Logger b = LoggerFactory.getLogger(IpUtils.class);
   public static final String IPV4 = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
   public static final String IPV6 = "^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|(::[fF]{4}(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";
   // $FF: synthetic field
   private static final String 1_1_1_ce = "Unknown";
   public static final String IPV6_CIDR = "^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|(::[fF]{4}(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))/([0-9]|[1-9][0-9]|1[0-1][0-9]|12[0-8])$";
   public static final String IPV4_CIDR = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/(?:[12]?[0-9]|3[0-2])$";

   // $FF: synthetic method
   private static boolean __1_1_ce/* $FF was: 1_1_1_ce*/(byte[] a) {
      int var10002 = 3 & 4;
      boolean var10004 = true;
      byte var1 = a[var10002];
      int var10001 = 3 >> 1;
      int var10003 = 3 >> 1;
      byte[] var3 = a[var10001];
      int var10000 = 10;
      var10002 = 1;
      var10000 = (boolean)-84;
      var10002 = 1;
      var10000 = (boolean)16;
      var10002 = 1;
      var10000 = (boolean)31;
      var10002 = 1;
      var10000 = (boolean)-64;
      var10002 = 1;
      var10000 = (boolean)-88;
      var10002 = 1;
      switch (var1) {
         case -84:
            while(false) {
            }

            var10003 = 1;
            if (var3 >= 16) {
               var10003 = 1;
               if (var3 <= 31) {
                  var10000 = 3 >> 1;
                  var10002 = 3 >> 1;
                  return (boolean)var10000;
               }
            }
         case -64:
            var10003 = 1;
            if (var3 == -88) {
               var10000 = 2 ^ 3;
               var10002 = 2 ^ 3;
               return (boolean)var10000;
            }

            var10002 = 1;
            return false;
         case 10:
            var10002 = 1;
            return true;
         default:
            var10002 = 1;
            return false;
      }
   }

   public static boolean isInRange(String a, String a, String a) {
      String var3 = a;

      int var23;
      try {
         InetAddress var7 = InetAddress.getByName(var3);
         String var13 = InetAddress.getByName(a);
         String var15 = InetAddress.getByName(a);
         byte[] var4 = var7.getAddress();
         String var14 = var13.getAddress();
         String var16 = var15.getAddress();
         var23 = 5 >> 2;
         int var25 = 5 >> 2;
         int var5 = var23;
         var23 = 2 ^ 3;
         var25 = 2 ^ 3;
         int var6 = var23;
         var23 = 5 >> 3;
         var25 = 1;
         String var12 = var23;

         while(true) {
            if (var23 >= var4.length) {
               var23 = var5;
               break;
            }

            var25 = var4[var12];
            boolean var10005 = true;
            int var8 = var25 & 255;
            byte var10001 = var14[var12];
            boolean var10004 = true;
            int var9 = var10001 & 255;
            var23 = var16[var12];
            boolean var10003 = true;
            int var10 = var23 & 255;
            if (var8 < var9) {
               var23 = 2 & 5;
               var25 = 1;
               break;
            }

            if (var8 > var10) {
               var23 = 3 & 4;
               var25 = 1;
               var6 = var23;
               var23 = var5;
               break;
            }

            ++var12;
            var23 = var12;
         }

         if (var23 != 0 && var6 != 0) {
            var23 = 5 >> 2;
            var25 = 5 >> 2;
            return (boolean)var23;
         }

         var23 = 0;
      } catch (UnknownHostException var11) {
         var23 = 5 >> 3;
         boolean var10002 = true;
         return (boolean)var23;
      }

      boolean var31 = true;
      return (boolean)var23;
   }

   public static String getHostIp() {
      try {
         return InetAddress.getLocalHost().getHostAddress();
      } catch (UnknownHostException var1) {
         return DesensitizationUtils.1_1_1_ce("\u000e\u0000\b\u001c\u000f\u001c\u000f\u001c\u000e");
      }
   }

   public static String getIpAddr(HttpServletRequest a) {
      HttpServletRequest var1 = a;

      try {
         if (var1 == null) {
            return "Unknown";
         } else {
            String var5;
            if ((var5 = var1.getHeader(DesensitizationUtils.1_1_1_ce("J\u0012]M[X[QSS\u001fY]ME^@[W[\u001fY]M"))) == null || var5.isEmpty() || "Unknown".equalsIgnoreCase(var5)) {
               var5 = var1.getHeader(ExampleRequest.1_1_1_ce("I\u0014WVCNPKU\\U\u0014WVC"));
            }

            if (var5 == null || var5.isEmpty() || "Unknown".equalsIgnoreCase(var5)) {
               var5 = var1.getHeader(DesensitizationUtils.1_1_1_ce("o@PJF\u001f|^VWQF\u0012{o"));
            }

            if (var5 == null || var5.isEmpty() || "Unknown".equalsIgnoreCase(var5)) {
               var5 = var1.getHeader(ExampleRequest.1_1_1_ce("i\u0014wVCNPKU\\U\u0014wVC"));
            }

            if (var5 == null || var5.isEmpty() || "Unknown".equalsIgnoreCase(var5)) {
               var5 = var1.getHeader(DesensitizationUtils.1_1_1_ce("es\u001fo@PJF\u001f|^VWQF\u0012{o"));
            }

            if (var5 == null || var5.isEmpty() || "Unknown".equalsIgnoreCase(var5)) {
               var5 = var1.getHeader(ExampleRequest.1_1_1_ce("i\u0014c\\PU\u001cpa"));
            }

            if (var5 == null || var5.isEmpty() || "Unknown".equalsIgnoreCase(var5)) {
               var5 = var1.getRemoteAddr();
               String var2 = DesensitizationUtils.1_1_1_ce("\u000e\u0000\b\u001c\u000f\u001c\u000f\u001c\u000e");
               String var3 = ExampleRequest.1_1_1_ce("\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0000");
               int var10003 = 1;
               CharSequence[] var10001 = new CharSequence[2];
               var10003 = (boolean)1;
               var10003 = 2 & 5;
               int var10005 = 1;
               var10001[var10003] = var2;
               var10003 = 3 >> 1;
               var10005 = 3 >> 1;
               var10001[var10003] = var3;
               if (org.apache.commons.lang3.StringUtils.equalsAny(var5, var10001)) {
                  var5 = Objects.toString(InetAddress.getLocalHost().getHostAddress(), var2);
               }
            }

            return org.apache.commons.lang3.StringUtils.substringBefore(var5, ",");
         }
      } catch (UnknownHostException var4) {
         throw new RuntimeException(var4);
      }
   }

   public static boolean isIpOrCidr(Set<String> a) {
      Iterator var1 = a.iterator();

      while(var1.hasNext()) {
         String var3;
         int var10000 = (var3 = (String)var1.next()).split("/").length;
         int var10001 = 4 ^ 5;
         int var10003 = 4 ^ 5;
         if (var10000 == var10001 && !var3.matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$") && !var3.matches("^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|(::[fF]{4}(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$")) {
            var10000 = 3 & 4;
            boolean var6 = true;
            return (boolean)var10000;
         }

         if (!var3.matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/(?:[12]?[0-9]|3[0-2])$") && var3.matches("^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|(::[fF]{4}(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))/([0-9]|[1-9][0-9]|1[0-1][0-9]|12[0-8])$")) {
            var10000 = 3 & 4;
            boolean var10002 = true;
            return (boolean)var10000;
         }
      }

      boolean var7 = true;
      return true;
   }

   public static String getHostName() {
      try {
         return InetAddress.getLocalHost().getHostName();
      } catch (UnknownHostException var1) {
         return "Unknown";
      }
   }

   public static boolean isInternalIp(String a) {
      if (!1_1_1_ce(InetAddresses.forString(a).getAddress()) && !a.startsWith(ExampleRequest.1_1_1_ce("\u0000\u000b\u0006"))) {
         boolean var1 = true;
         return false;
      } else {
         boolean var10002 = true;
         return true;
      }
   }

   public static boolean isInRange(@NotNull String ip, @NotNull List<String> ipScopes) {
      String var10000 = ip;
      List var3 = ipScopes;
      List ipScopes = var10000;

      for(String var2 : var3) {
         if (isInRange(ipScopes, var2)) {
            int var6 = 5 >> 2;
            int var10002 = 5 >> 2;
            return (boolean)var6;
         }
      }

      boolean var7 = true;
      return false;
   }

   public static boolean isInRange(@NotNull String ip, @NotNull String ipRange) {
      String var10000 = ip;
      ip = ipRange;
      ipRange = var10000;
      int var4 = ip.split("/").length;
      boolean var10002 = true;
      int var10003 = -10514;
      boolean var10004 = true;
      boolean var10005 = true;
      int var10001 = 4 ^ 5;
      var10003 = 4 ^ 5;
      return var4 == var10001 ? ipRange.equals(ip) : (new SubnetUtils(ip)).getInfo().isInRange(ipRange);
   }
}
