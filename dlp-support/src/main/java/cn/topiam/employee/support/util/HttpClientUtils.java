package cn.topiam.employee.support.util;

import cn.topiam.employee.support.s;
import cn.topiam.employee.support.repository.page.domain.Page;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class HttpClientUtils {
   // $FF: synthetic field
   private static final int E = 3000000;
   // $FF: synthetic field
   public static final boolean G;
   // $FF: synthetic field
   @Generated
   private static final Logger D;
   // $FF: synthetic field
   private static final int b = 3000000;
   // $FF: synthetic field
   private static final String 1_1_1_ce = "UTF-8";

   public static String put(String a, Map<String, String> a) throws IOException {
      String var10000 = a;
      Map var4 = a;
      Map a = var10000;
      CloseableHttpClient var2 = HttpClientBuilder.create().build();
      HttpPut var3 = new HttpPut(a);
      String var5 = 1_1_1_ce(var4);
      String var6 = new UrlEncodedFormEntity(var5, "UTF-8");
      var3.setEntity(var6);
      String var7 = 1_1_1_ce(var2.execute(var3));
      var3.abort();
      return var7;
   }

   public static String delete(String a) throws IOException {
      String var1 = a;
      String var3 = HttpClientBuilder.create().build();
      HttpDelete var2 = new HttpDelete();
      var2.setURI(URI.create(var1));
      String var4 = 1_1_1_ce(var3.execute(var2));
      var2.abort();
      return var4;
   }

   public static String postRequestByFormEntity(String a, UrlEncodedFormEntity a) {
      String var3 = a;
      String var2 = null;

      try {
         HttpPost var6;
         (var6 = new HttpPost(var3)).setEntity(a);
         D.info(s.1_1_1_ce("c\n"), var6);
         CloseableHttpResponse var4;
         CloseableHttpClient var7;
         if ((var4 = (var7 = HttpClients.createDefault()).execute(var6)).getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
            var2 = org.apache.http.util.EntityUtils.toString(var4.getEntity(), "UTF-8");
            var4.close();
            var7.close();
         }

         return var2;
      } catch (Exception var5) {
         throw new RuntimeException(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
      }
   }

   public static String doPost(String a, Map<String, String> a) {
      String var2 = a;
      CloseableHttpClient var4 = HttpClients.createDefault();
      CloseableHttpResponse var3 = null;

      try {
         try {
            String var17 = new HttpPost(var2);
            if (a != null) {
               ArrayList var5 = new ArrayList();

               Iterator var6;
               for(Iterator var21 = var6 = a.keySet().iterator(); var21.hasNext(); var21 = var6) {
                  String var7 = (String)var6.next();
                  var5.add(new BasicNameValuePair(var7, (String)a.get(var7)));
               }

               UrlEncodedFormEntity var20 = new UrlEncodedFormEntity(var5);
               var17.setEntity(var20);
            }

            var18 = org.apache.http.util.EntityUtils.toString((var3 = var4.execute(var17)).getEntity(), "UTF-8");
         } catch (Exception var15) {
            D.error(s.1_1_1_ce("c\n"), var15.getStackTrace());
            throw new RuntimeException(HttpStatus.INTERNAL_SERVER_ERROR.toString());
         }
      } catch (Throwable var16) {
         Throwable var10000;
         try {
            if (!G && var3 == null) {
               throw new AssertionError();
            }

            var3.close();
         } catch (IOException var14) {
            D.error(Page.1_1_1_ce("%W"), var14.getStackTrace());
            var10000 = var16;
            throw var10000;
         }

         var10000 = var16;
         throw var10000;
      }

      try {
         if (!G && var3 == null) {
            throw new AssertionError();
         } else {
            var3.close();
            return var18;
         }
      } catch (IOException var13) {
         D.error(Page.1_1_1_ce("%W"), var13.getStackTrace());
         return var18;
      }
   }

   public static String doGet(String a, Map<String, String> a) {
      String var3 = a;
      CloseableHttpClient var5 = HttpClients.createDefault();
      String var4 = "";
      String var19 = null;

      label134: {
         try {
            try {
               URIBuilder var2 = new URIBuilder(var3);
               Iterator var6;
               if (a != null) {
                  for(Iterator var23 = var6 = a.keySet().iterator(); var23.hasNext(); var23 = var6) {
                     String var7 = (String)var6.next();
                     var2.addParameter(var7, (String)a.get(var7));
                  }
               }

               URI var21 = var2.build();
               HttpGet var22 = new HttpGet(var21);
               if ((var19 = var5.execute(var22)).getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                  var4 = org.apache.http.util.EntityUtils.toString(var19.getEntity(), "UTF-8");
               }
               break label134;
            } catch (Exception var17) {
               D.error(s.1_1_1_ce("c\n"), var17.getStackTrace());
            }
         } catch (Throwable var18) {
            Throwable var10000;
            try {
               if (var19 != null) {
                  var19.close();
               }

               var5.close();
            } catch (IOException var16) {
               D.error(s.1_1_1_ce("c\n"), var16.getStackTrace());
               var10000 = var18;
               throw var10000;
            }

            var10000 = var18;
            throw var10000;
         }

         try {
            if (var19 != null) {
               var19.close();
            }

            var5.close();
            return var4;
         } catch (IOException var14) {
            D.error(Page.1_1_1_ce("%W"), var14.getStackTrace());
            return var4;
         }
      }

      try {
         if (var19 != null) {
            var19.close();
         }

         var5.close();
         return var4;
      } catch (IOException var15) {
         D.error(Page.1_1_1_ce("%W"), var15.getStackTrace());
         return var4;
      }
   }

   public static String delete(String a, Map<String, String> a) throws IOException {
      String var3 = a;
      CloseableHttpClient var2 = HttpClientBuilder.create().build();
      String var4 = new HttpDelete();
      Map var5 = URLEncodedUtils.format(1_1_1_ce(a), "UTF-8");
      var4.setURI(URI.create(var3 + "?" + var5));
      String var10000 = 1_1_1_ce(var2.execute(var4));
      var4.abort();
      return var10000;
   }

   // $FF: synthetic method
   private static String __1_1_ce/* $FF was: 1_1_1_ce*/(HttpResponse a) throws IOException {
      HttpEntity var4;
      if ((var4 = a.getEntity()) == null) {
         return "";
      } else {
         HttpResponse var5 = var4.getContent();
         BufferedReader var6;
         String var2 = (var6 = new BufferedReader(new InputStreamReader(var5, StandardCharsets.UTF_8))).readLine();
         StringBuilder var3 = new StringBuilder();

         for(String var10000 = var2; var10000 != null; var10000 = var2) {
            var3.append(var2).append(s.1_1_1_ce("}"));
            var2 = var6.readLine();
         }

         return var3.toString();
      }
   }

   static {
      int var10000;
      if (!HttpClientUtils.class.desiredAssertionStatus()) {
         var10000 = 5 >> 2;
         int var10002 = 5 >> 2;
      } else {
         var10000 = 0;
         boolean var0 = true;
      }

      G = (boolean)var10000;
      D = LoggerFactory.getLogger(HttpClientUtils.class);
   }

   public static String post(String a, String a) throws IOException {
      CloseableHttpClient var2 = HttpClientBuilder.create().build();
      String var4 = new HttpPost(a);
      var4.setHeader(s.1_1_1_ce("[\u0018v\u0003}\u0019lZL\u000eh\u0012"), Page.1_1_1_ce("*O&^q@-E0\u0011~I6K,Y;^c_*Ls\u0012"));
      var4.setEntity(new StringEntity(URLEncoder.encode(a, StandardCharsets.UTF_8)));
      a = 1_1_1_ce(var2.execute(var4));
      var4.abort();
      return a;
   }

   public static String doPostJson(String a, String a) {
      String var3 = a;
      CloseableHttpClient var5 = HttpClients.createDefault();
      String var17 = null;
      String var4 = "";

      label110: {
         try {
            try {
               HttpPost var2 = new HttpPost(var3);
               String var19 = new StringEntity(a, ContentType.APPLICATION_JSON);
               var2.setEntity(var19);
               var4 = org.apache.http.util.EntityUtils.toString((var17 = var5.execute(var2)).getEntity(), "UTF-8");
               break label110;
            } catch (Exception var15) {
               D.error(Page.1_1_1_ce("%W"), var15.getStackTrace());
            }
         } catch (Throwable var16) {
            Throwable var10000;
            try {
               if (!G && var17 == null) {
                  throw new AssertionError();
               }

               var17.close();
            } catch (IOException var14) {
               D.error(Page.1_1_1_ce("%W"), var14.getStackTrace());
               var10000 = var16;
               throw var10000;
            }

            var10000 = var16;
            throw var10000;
         }

         try {
            if (!G && var17 == null) {
               throw new AssertionError();
            }

            var17.close();
            return var4;
         } catch (IOException var12) {
            D.error(s.1_1_1_ce("c\n"), var12.getStackTrace());
            return var4;
         }
      }

      try {
         if (!G && var17 == null) {
            throw new AssertionError();
         } else {
            var17.close();
            return var4;
         }
      } catch (IOException var13) {
         D.error(s.1_1_1_ce("c\n"), var13.getStackTrace());
         return var4;
      }
   }

   public static String client(String a, HttpMethod a, MultiValueMap<String, String> a) {
      String var10000 = a;
      HttpMethod var6 = a;
      HttpMethod a = var10000;
      RestTemplate var4 = new RestTemplate();
      HttpHeaders var3 = new HttpHeaders();

      try {
         var3.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
         MultiValueMap var8 = new org.springframework.http.HttpEntity(a, var3);
         int var10005 = 2 & 5;
         boolean var10007 = true;
         Object[] var9 = new Object[var10005];
         var10007 = true;
         return (String)var4.exchange(a, var6, var8, String.class, var9).getBody();
      } catch (RestClientException var5) {
         D.error(s.1_1_1_ce("?L#H讀汚彵帠ｭc\n"), var5.getLocalizedMessage());
         return Page.1_1_1_ce("\u0007");
      }
   }

   public static String doGet(String a) {
      return doGet(a, (Map)null);
   }

   public static String get(String a, Map<String, String> a, BasicHeader... a) {
      String var10000 = a;
      BasicHeader[] var7 = a;
      BasicHeader[] a = var10000;

      try {
         CloseableHttpClient var3 = HttpClientBuilder.create().build();
         HttpGet var4 = new HttpGet();
         RequestConfig.Builder var13 = RequestConfig.custom();
         boolean var10003 = true;
         var13 = var13.setConnectTimeout(5000);
         var10003 = true;
         RequestConfig var5 = var13.setConnectionRequestTimeout(1000).setSocketTimeout(60000).build();
         var4.setConfig(var5);
         var4.setHeaders(var7);
         String var8 = URLEncodedUtils.format(1_1_1_ce(a), "UTF-8");
         String var9 = new URL(a + "?" + var8);
         String var10 = new URI(var9.getProtocol(), var9.getHost(), var9.getPath(), var9.getQuery(), (String)null);
         var4.setURI(var10);
         Map var11 = 1_1_1_ce(var3.execute(var4));
         var4.abort();
         return var11;
      } catch (Exception var6) {
         throw new RuntimeException();
      }
   }

   public static String post(String a, Map<String, String> a) throws IOException {
      String var10000 = a;
      Map var4 = a;
      Map a = var10000;
      CloseableHttpClient var2 = HttpClientBuilder.create().build();
      HttpPost var3 = new HttpPost(a);
      String var5 = 1_1_1_ce(var4);
      String var6 = new UrlEncodedFormEntity(var5, "UTF-8");
      var3.setEntity(var6);
      String var7 = 1_1_1_ce(var2.execute(var3));
      var3.abort();
      return var7;
   }

   public static String doPost(String a) {
      return doPost(a, (Map)null);
   }

   public static String get(String a) throws IOException {
      String var1 = a;
      String var3 = HttpClientBuilder.create().build();
      HttpGet var2 = new HttpGet();
      var2.setURI(URI.create(var1));
      String var4 = 1_1_1_ce(var3.execute(var2));
      var2.abort();
      return var4;
   }

   // $FF: synthetic method
   private static List<NameValuePair> __1_1_ce/* $FF was: 1_1_1_ce*/(Map<String, String> a) {
      ArrayList var1 = new ArrayList();

      Iterator var2;
      for(Iterator var10000 = var2 = a.entrySet().iterator(); var10000.hasNext(); var10000 = var2) {
         Map var4 = (Map.Entry)var2.next();
         var1.add(new BasicNameValuePair((String)var4.getKey(), (String)var4.getValue()));
      }

      return var1;
   }

   public static void outPrint(HttpServletResponse a, Object a) {
      HttpServletResponse var2 = a;
      HttpServletResponse var8 = null;

      try {
         D.info("<<<<-------outPrint返回数据------->>>>" + String.valueOf(a));
         var2.setContentType(s.1_1_1_ce("l\u0012`\u00037\u001fl\u001atL{\u001fy\u0005k\u0012lJM#^O"));
         var2.setHeader(Page.1_1_1_ce(".X1M3K"), s.1_1_1_ce("v\u00185\u0014y\u0014p\u0012"));
         var2.setHeader(Page.1_1_1_ce("i?I6Osi1D*X1F"), s.1_1_1_ce("v\u00185\u0014y\u0014p\u0012"));
         (var8 = var2.getWriter()).print(a);
         return;
      } catch (IOException var6) {
         D.error(Page.1_1_1_ce("%W"), var6.getStackTrace());
      } finally {
         if (!G && var8 == null) {
            throw new AssertionError();
         }

         var8.flush();
         var8.close();
      }

   }

   public static UrlEncodedFormEntity buildPairList(Map<String, String> a) {
      Map var1 = a;

      try {
         Map var5 = new ArrayList();

         Iterator var2;
         for(Iterator var10000 = var2 = var1.entrySet().iterator(); var10000.hasNext(); var10000 = var2) {
            Map.Entry var3 = (Map.Entry)var2.next();
            var5.add(new BasicNameValuePair((String)var3.getKey(), (String)var3.getValue()));
         }

         D.info(Page.1_1_1_ce("%W"), var5);
         return new UrlEncodedFormEntity(var5, "UTF-8");
      } catch (Exception var4) {
         throw new RuntimeException(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
      }
   }
}
