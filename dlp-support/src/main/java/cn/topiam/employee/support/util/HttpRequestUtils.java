package cn.topiam.employee.support.util;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class HttpRequestUtils {
   public static MultiValueMap<String, String> getFormParameters(HttpServletRequest a) {
      HttpServletRequest var2 = a;
      Map var1 = a.getParameterMap();
      HttpServletRequest var3 = new LinkedMultiValueMap();
      var1.forEach((ax, axx) -> {
         HttpServletRequest var7 = org.springframework.util.StringUtils.hasText(var2.getQueryString()) ? var2.getQueryString() : "";
         if (!var7.contains(ax)) {
            String[] var9;
            HttpServletRequest var8 = (var9 = axx).length;
            int var10000 = 3 >> 2;
            boolean var10002 = true;

            for(int var5 = var10000; var10000 < var8; var10000 = var5) {
               String var6 = var9[var5];
               ++var5;
               var3.add(ax, var6);
            }
         }

      });
      return var3;
   }

   public static MultiValueMap<String, String> getQueryParameters(HttpServletRequest a) {
      HttpServletRequest var2 = a;
      Map var1 = a.getParameterMap();
      HttpServletRequest var3 = new LinkedMultiValueMap();
      var1.forEach((ax, axx) -> {
         HttpServletRequest var7 = org.springframework.util.StringUtils.hasText(var2.getQueryString()) ? var2.getQueryString() : "";
         if (var7.contains(ax)) {
            String[] var9;
            HttpServletRequest var8 = (var9 = axx).length;
            int var10000 = 5 >> 3;
            boolean var10002 = true;

            for(int var5 = var10000; var10000 < var8; var10000 = var5) {
               String var6 = var9[var5];
               ++var5;
               var3.add(ax, var6);
            }
         }

      });
      return var3;
   }

   public static Map<String, String> getRequestHeaders(HttpServletRequest a) {
      HttpServletRequest var3 = a;
      boolean var10004 = true;
      HashMap var1 = new HashMap(16);
      Enumeration var2;
      Enumeration var10000 = var2 = a.getHeaderNames();

      while(var10000.hasMoreElements()) {
         HttpServletRequest var5 = (String)var2.nextElement();
         String var4 = var3.getHeader(var5);
         var10000 = var2;
         var1.put(var5, var4);
      }

      return var1;
   }

   public static Map<String, String> getRequestParameters(HttpServletRequest a) {
      int var10004 = 1;
      HashMap var6 = new HashMap(16);

      Iterator var5;
      for(Iterator var10000 = var5 = a.getParameterMap().entrySet().iterator(); var10000.hasNext(); var10000 = var5) {
         Map.Entry var4;
         HttpServletRequest var7 = (String[])(var4 = (Map.Entry)var5.next()).getValue();
         String var2 = "";
         int var8 = 2 & 5;
         int var10002 = 1;

         for(int var1 = var8; var8 < var7.length; var8 = var1) {
            int var10001 = var7.length;
            var10002 = 3 & 5;
            var10004 = 3 & 5;
            var2 = var1 == var10001 - var10002 ? var2 + var7[var1] : var2 + var7[var1] + ",";
            ++var1;
         }

         var6.put((String)var4.getKey(), var2);
      }

      return var6;
   }

   public HttpRequestUtils() {
      boolean var10000 = true;
      boolean var10001 = true;
      boolean var10002 = true;
      boolean var10003 = true;
      super();
   }
}
