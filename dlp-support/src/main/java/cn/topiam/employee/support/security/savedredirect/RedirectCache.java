package cn.topiam.employee.support.security.savedredirect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 重定向缓存接口
 * 定义重定向缓存的操作方法
 */
public interface RedirectCache {
   /**
    * 保存重定向的会话属性名
    */
   String TOPIAM_SECURITY_SAVED_REDIRECT = "TOPIAM_SECURITY_SAVED_REDIRECT";

   /**
    * 获取重定向信息
    *
    * @param request  HTTP请求
    * @param response HTTP响应
    * @return 保存的重定向信息
    */
   SavedRedirect getRedirect(HttpServletRequest request, HttpServletResponse response);

   /**
    * 移除重定向信息
    *
    * @param request  HTTP请求
    * @param response HTTP响应
    */
   void removeRedirect(HttpServletRequest request, HttpServletResponse response);

   /**
    * 保存重定向信息
    *
    * @param request  HTTP请求
    * @param response HTTP响应
    * @param redirectType 重定向类型
    */
   void saveRedirect(HttpServletRequest request, HttpServletResponse response, RedirectType redirectType);

   /**
    * 重定向类型枚举
    */
   public static enum RedirectType {
      /**
       * 参数类型
       */
      PARAMETER("PARAMETER", 0),
      
      /**
       * 请求类型
       */
      REQUEST("REQUEST", 1);

      /**
       * 构造函数
       *
       * @param name 名称
       * @param ordinal 序号
       */
      private RedirectType(String name, int ordinal) {
      }

      static {
         PARAMETER = new RedirectType("PARAMETER", 0);
         REQUEST = new RedirectType("REQUEST", 1);
      }

      // $FF: synthetic method
      private static RedirectType[] initValues() {
         RedirectType[] values = new RedirectType[2];
         values[0] = PARAMETER;
         values[1] = REQUEST;
         return values;
      }
   }
}