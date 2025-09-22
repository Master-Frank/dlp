package cn.topiam.employee.support.util;

import cn.topiam.employee.support.jackjson.SupportJackson2Module;
import cn.topiam.employee.support.repository.page.domain.PageModel;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpResponseUtils {
   // $FF: synthetic field
   private static final String b = "null";
   // $FF: synthetic field
   private static final ObjectMapper 1_1_1_ce = new ObjectMapper();

   static {
      1_1_1_ce.registerModule(new JavaTimeModule());
      1_1_1_ce.setSerializationInclusion(Include.NON_NULL);
   }

   public static void flushResponseJson(HttpServletResponse a, int a, Object a) {
      HttpServletResponse var10000 = a;
      int var4 = a;
      int a = (int)var10000;

      try {
         a.setCharacterEncoding("UTF-8");
         a.setHeader(PageModel.1_1_1_ce("/:\u000f3\tv/4\u0002/\u001e4\u0000"), SupportJackson2Module.1_1_1_ce("\u0001wB{\u000e{\u0007}"));
         a.setContentType(PageModel.1_1_1_ce(":\u001c+\u00002\u000f:\u00182\u00035C1\u001f4\u0002`\u000f3\r)\u001f>\u0018f9\u000f*vT"));
         a.setStatus(var4);
         HttpServletResponse var5 = a.getWriter();
         if (ObjectUtils.isNotEmpty(a)) {
            Object var7 = 1_1_1_ce.writeValueAsString(a);
            var5.write(var7);
         } else {
            var5.write("");
         }
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> a) {
      return wrapOrNotFound(a, (HttpHeaders)null);
   }

   public static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> a, HttpHeaders a) {
      Optional var10000 = a;
      HttpHeaders var2 = a;
      HttpHeaders a = var10000;
      return (ResponseEntity)((Optional)a).map((ax) -> {
         HttpHeaders var10000 = var2;
         Object var2x = ax;
         ax = var10000;
         return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers((HttpHeaders)ax)).body(var2x);
      }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
   }

   public static void flushResponse(HttpServletResponse a, String a) {
      HttpServletResponse var2 = a;

      try {
         var2.setCharacterEncoding("UTF-8");
         var2.setHeader(SupportJackson2Module.1_1_1_ce("[\u000e{\u0007}B[\u0000v\u001bj\u0000t"), PageModel.1_1_1_ce("5\u0003v\u000f:\u000f3\t"));
         var2.setContentType(SupportJackson2Module.1_1_1_ce("l\n`\u001b7\u0007l\u0002tT{\u0007y\u001dk\nlRM;^B "));
         HttpServletResponse var4 = var2.getWriter();
         int var10000 = 3 >> 1;
         int var10002 = 3 >> 1;
         CharSequence[] var5 = new CharSequence[var10000];
         var10002 = 1;
         var10002 = 3 >> 2;
         boolean var10004 = true;
         var5[var10002] = a;
         if (org.apache.commons.lang3.StringUtils.isNoneBlank(var5)) {
            var4.write(a);
         } else {
            var4.write("");
         }
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public static void flushResponseJson(HttpServletResponse a, int a, ObjectMapper a, Object a) {
      HttpServletResponse var10000 = a;
      int var5 = a;
      int a = (int)var10000;

      try {
         a.setCharacterEncoding("UTF-8");
         a.setHeader(SupportJackson2Module.1_1_1_ce("[\u000e{\u0007}B[\u0000v\u001bj\u0000t"), PageModel.1_1_1_ce("5\u0003v\u000f:\u000f3\t"));
         a.setContentType(SupportJackson2Module.1_1_1_ce("\u000eh\u001ft\u0006{\u000el\u0006w\u00017\u0005k\u0000vT{\u0007y\u001dk\nlRM;^B "));
         a.setStatus(var5);
         HttpServletResponse var6 = a.getWriter();
         if (ObjectUtils.isNotEmpty(a)) {
            ObjectMapper var8 = a.writeValueAsString(a);
            var6.write(var8);
         } else {
            var6.write("");
         }
      } catch (IOException var4) {
         throw new RuntimeException(var4);
      }
   }
}
