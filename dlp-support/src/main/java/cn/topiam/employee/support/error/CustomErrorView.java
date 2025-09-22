package cn.topiam.employee.support.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.View;
import org.springframework.web.util.HtmlUtils;

/**
 * 自定义错误视图类
 * 用于处理和渲染系统错误页面
 */
public class CustomErrorView implements View {
    /**
     * 响应内容类型
     */
    private static final MediaType CONTENT_TYPE;
    
    /**
     * 日志记录器
     */
    private static final Log logger;

    static {
        // 初始化内容类型为text/html;charset=UTF-8
        CONTENT_TYPE = new MediaType("text", "html", StandardCharsets.UTF_8);
        logger = LogFactory.getLog(CustomErrorView.class);
    }

    /**
     * 获取视图的内容类型
     * 
     * @return 内容类型字符串
     */
    @Override
    public String getContentType() {
        return "text/html;charset=UTF-8";
    }

    /**
     * 记录错误页面无法渲染的日志信息
     * 
     * @param request HTTP请求
     * @param response HTTP响应
     * @param map 数据映射
     * @param ex 异常
     */
    private void logWarningIfNotRendered(@NonNull HttpServletRequest request, 
                                        @NonNull HttpServletResponse response, 
                                        @NonNull Map<String, Object> map, 
                                        Exception ex) {
        if (logger.isWarnEnabled()) {
            String message = "Warning: requestId=" + ObjectUtils.identityToString(this) + 
                    ", error message=[" + map.get("message") + "]" + 
                    ", status code=[" + response.getStatus() + "]" + 
                    ", exception=[" + ex.getClass().getName() + ": " + ex.getMessage() + "]" + 
                    ", request URI=[" + request.getRequestURI() + "]" + 
                    ", request method=[" + request.getMethod() + "]" + 
                    ", timestamp=[" + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") + "]";
            logger.warn(message, ex);
        }
    }

    /**
     * 渲染视图
     * 
     * @param map 数据映射
     * @param request HTTP请求
     * @param response HTTP响应
     * @throws Exception 渲染过程中可能抛出的异常
     */
    @Override
    public void render(@NonNull Map<String, Object> map, 
                      @NonNull HttpServletRequest request, 
                      @NonNull HttpServletResponse response) throws Exception {
        if (response.isCommitted()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Response already committed. Cannot render error page.");
            }
            return;
        }

        response.setContentType(Objects.toString(CONTENT_TYPE));
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n")
            .append("<html>\n")
            .append("<head>\n")
            .append("    <title>Error</title>\n")
            .append("    <meta charset=\"UTF-8\">\n")
            .append("    <style>\n")
            .append("        body { font-family: Arial, sans-serif; margin: 40px; }\n")
            .append("        .error-container { border: 1px solid #ddd; padding: 20px; border-radius: 5px; }\n")
            .append("        .error-title { color: #d9534f; }\n")
            .append("        .error-message { color: #333; }\n")
            .append("    </style>\n")
            .append("</head>\n")
            .append("<body>\n")
            .append("    <div class=\"error-container\">\n")
            .append("        <h1 class=\"error-title\">系统错误</h1>\n")
            .append("        <p class=\"error-message\">").append(HtmlUtils.htmlEscape((String) map.get("message"))).append("</p>\n")
            .append("        <p>状态码: ").append(map.get("status")).append("</p>\n")
            .append("        <p>时间戳: ").append(new Date()).append("</p>\n")
            .append("    </div>\n")
            .append("</body>\n")
            .append("</html>");

        try {
            response.getWriter().write(html.toString());
        } catch (Exception ex) {
            this.logWarningIfNotRendered(request, response, map, ex);
        }
    }
}