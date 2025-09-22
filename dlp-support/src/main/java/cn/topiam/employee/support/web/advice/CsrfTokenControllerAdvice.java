package cn.topiam.employee.support.web.advice;

import cn.topiam.employee.support.util.PhoneUtils;
import cn.topiam.employee.support.web.filter.CustomRedirectFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.function.Supplier;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * CSRF令牌控制器通知
 * 用于在控制器中添加CSRF令牌属性
 */
@ControllerAdvice
public class CsrfTokenControllerAdvice {
   
   /**
    * CSRF令牌请求处理器
    */
   private final CsrfTokenRequestHandler requestHandler = new CustomRedirectFilter();

   /**
    * 解码字符串
    * 
    * @param str 待解码字符串
    * @return 解码后的字符串
    */
   public static String decodeString(String str) {
      int key1 = 2 << 3 ^ 2;
      int key2 = 4 << 4 ^ (4 ^ 5) << (4 ^ 5);
      int key3 = 5 << 4;
      int length = str.length();
      char[] chars = new char[length];
      int index = length - (4 ^ 5);
      char[] result = chars;
      int var4 = key2;
      int var5 = index;

      for (int var6 = key1; var5 >= 0; var5 = index) {
         int tempIndex = index;
         int charValue = str.charAt(index);
         --index;
         result[tempIndex] = (char) (charValue ^ var6);
         if (index < 0) {
            break;
         }

         int tempIndex2 = index--;
         result[tempIndex2] = (char) (str.charAt(tempIndex2) ^ var4);
      }

      return new String(result);
   }

   /**
    * 添加CSRF令牌属性
    * 
    * @param request HTTP请求
    * @param response HTTP响应
    */
   @ModelAttribute
   public void addCsrfToken(HttpServletRequest request, HttpServletResponse response) {
      CsrfToken csrfToken = (CsrfToken) request.getAttribute(PhoneUtils.decryptString("\u001e\u000f\u0018\u0018\u001b\u001d\u0018\u001a\u0004\u0011\u0015\u0010\u001e\n\u0012\f\u001a\u001a\u0017\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"));
      if (Objects.nonNull(csrfToken)) {
         this.requestHandler.handle(request, response, () -> csrfToken);
      }
   }
}