package cn.topiam.employee.support.trace;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * 跟踪工具类
 * 提供跟踪ID的管理功能
 */
public class TraceUtils {
   public static final String REQUEST_ID = "requestId";

   /**
    * 设置跟踪ID
    *
    * @param traceId 跟踪ID
    */
   public static void put(String traceId) {
      MDC.put("TRACE_ID", traceId);
   }

   /**
    * 移除跟踪ID
    */
   public static void remove() {
      MDC.remove("TRACE_ID");
   }

   /**
    * 获取跟踪ID
    *
    * @return 跟踪ID
    */
   public static String get() {
      return MDC.get("TRACE_ID");
   }

   /**
    * 从请求中获取请求ID
    *
    * @param request HTTP请求
    * @return 请求ID
    */
   public static String getRequestId(HttpServletRequest request) {
      HttpServletRequest req = request;
      String requestId;
      if (StringUtils.isNotBlank(requestId = req.getHeader("trace-id"))) {
         return requestId;
      } else if (StringUtils.isNotBlank(requestId = req.getParameter("requestId"))) {
         return requestId;
      } else {
         return StringUtils.isNotBlank(requestId = req.getParameter("traceId")) ? requestId : req.getParameter("trace_id");
      }
   }
}
