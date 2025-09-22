package cn.topiam.employee.support.context;

import cn.topiam.employee.support.security.userdetails.Group;
import cn.topiam.employee.support.util.DesensitizationUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Servlet context service class
 * Provides access and operation functions for Servlet-related objects
 */
public final class ServletContextService {
    @Generated
    private static final Logger logger = LoggerFactory.getLogger(ServletContextService.class);
    private static final List<MediaType> DEFAULT_MEDIA_TYPES;

    /**
     * Get current session
     *
     * @return HttpSession object
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * Get request accepted media types
     *
     * @param request HttpServletRequest object
     * @return media type list
     */
    public static List<MediaType> getAcceptedMediaTypes(HttpServletRequest request) {
        String acceptHeader = request.getHeader("Accept");
        return StringUtils.hasText(acceptHeader) ? MediaType.parseMediaTypes(acceptHeader) : DEFAULT_MEDIA_TYPES;
    }

    /**
     * Get request JSON object
     *
     * @param request HttpServletRequest object
     * @return JSONObject object
     * @throws IOException IO exception
     */
    public static JSONObject getRequestJsonObject(HttpServletRequest request) throws IOException {
        return JSON.parseObject(getRequestJsonString(request));
    }

    static {
        DEFAULT_MEDIA_TYPES = Collections.singletonList(MediaType.ALL);
    }

    /**
     * Get request parameter object
     *
     * @param request HttpServletRequest object
     * @param targetObject target object
     * @return parameter object
     */
    public static Object getParameterObject(HttpServletRequest request, Object targetObject) {
        Map<String, String> parameterMap = getParameterMap(request);
        var propertyAccessor = PropertyAccessorFactory.forBeanPropertyAccess(targetObject);
        propertyAccessor.setAutoGrowNestedPaths(true);
        propertyAccessor.setPropertyValues(parameterMap);
        return propertyAccessor.getWrappedInstance();
    }

    /**
     * Set session attribute
     *
     * @param name attribute name
     * @param value attribute value
     */
    public static void setAttribute(String name, Object value) {
        getSession().setAttribute(name, value);
    }

    /**
     * Set cookie
     *
     * @param response HttpServletResponse object
     * @param name cookie name
     * @param value cookie value
     * @param path cookie path
     * @param maxAge cookie max age
     * @return HttpServletResponse object
     */
    public static HttpServletResponse setCookie(HttpServletResponse response, String name, String value, String path, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path);
        URLEncoder.encode(value, StandardCharsets.UTF_8);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
        return response;
    }

    /**
     * Get current request
     *
     * @return HttpServletRequest object
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * Remove session attribute
     *
     * @param name attribute name
     */
    public static void removeAttribute(String name) {
        getSession().removeAttribute(name);
    }

    /**
     * Get server URL
     *
     * @param request HttpServletRequest object
     * @return server URL
     */
    public static String getServerUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(scheme).append("://").append(serverName);
        
        if ("http".equals(scheme)) {
            if (serverPort == 80) {
                // Use default port, no need to add
            }
        } else if ("https".equals(scheme)) {
            if (serverPort == 443) {
                // Use default port, no need to add
            }
        } else {
            urlBuilder.append(":").append(serverPort);
        }

        urlBuilder.append(contextPath);
        return urlBuilder.toString();
    }

    /**
     * Get server root path
     *
     * @param request HttpServletRequest object
     * @return server root path
     */
    public static String getServerRootPath(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
    }

    /**
     * Get session
     *
     * @param create whether to create a new session
     * @return HttpSession object
     */
    public static HttpSession getSession(boolean create) {
        return getRequest().getSession(create);
    }

    /**
     * Read cookie by name
     *
     * @param request HttpServletRequest object
     * @return cookie map
     */
    private static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
        HashMap<String, Cookie> cookieMap = new HashMap<>(16);
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }

        return cookieMap;
    }

    /**
     * Get request parameter
     *
     * @param name parameter name
     * @return parameter value
     */
    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    /**
     * Get request POST byte data
     *
     * @param request HttpServletRequest object
     * @return byte array
     * @throws IOException IO exception
     */
    public static byte[] getRequestPostBytes(HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        } else {
            byte[] buffer = new byte[contentLength];
            int offset = 0;

            int bytesRead;
            while (offset < contentLength) {
                bytesRead = request.getInputStream().read(buffer, offset, contentLength - offset);
                if (bytesRead == -1) {
                    return buffer;
                }
                offset += bytesRead;
            }

            return buffer;
        }
    }

    /**
     * Get response object
     *
     * @return HttpServletResponse object
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
    }

    /**
     * Check if it is an HTML request
     *
     * @param request HttpServletRequest object
     * @return whether it is an HTML request
     */
    public static boolean isHtmlRequest(HttpServletRequest request) {
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("text/html")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get session attribute
     *
     * @param name attribute name
     * @return attribute value
     */
    public static Object getAttribute(String name) {
        return getSession().getAttribute(name);
    }

    /**
     * Get request POST string data
     *
     * @param request HttpServletRequest object
     * @return string
     * @throws IOException IO exception
     */
    public static String getRequestPostStr(HttpServletRequest request) throws IOException {
        byte[] postData = getRequestPostBytes(request);
        String characterEncoding = request.getCharacterEncoding();
        if (characterEncoding == null) {
            characterEncoding = "UTF-8";
        }

        return new String(postData != null ? postData : new byte[0], characterEncoding);
    }

    /**
     * Read cookie by name
     *
     * @param request HttpServletRequest object
     * @param name cookie name
     * @return cookie object
     */
    public static Cookie readCookieByName(HttpServletRequest request, String name) {
        return readCookieMap(request).getOrDefault(name, null);
    }

    /**
     * Get request parameter map
     *
     * @param request HttpServletRequest object
     * @return parameter map
     */
    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        HashMap<String, String> parameterMap = new HashMap<>(16);
        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                if (!paramValues[0].isEmpty()) {
                    parameterMap.put(paramName, paramValues[0]);
                }
            }
        }

        return parameterMap;
    }

    /**
     * Get request JSON string
     *
     * @param request HttpServletRequest object
     * @return JSON string
     * @throws IOException IO exception
     */
    public static String getRequestJsonString(HttpServletRequest request) throws IOException {
        String method = request.getMethod();
        if (HttpMethod.GET.name().equals(method)) {
            String queryString = request.getQueryString();
            if (queryString != null) {
                return new String(queryString.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8).replaceAll("\\+", " ");
            } else {
                return "";
            }
        } else {
            return getRequestPostStr(request);
        }
    }

    /**
     * Get parameter map from URL
     *
     * @param url URL string
     * @return parameter map
     */
    public static Map<String, String> getParameterForUrl(String url) {
        HashMap<String, String> parameterMap = new HashMap<>(16);

        try {
            String decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8);
            int queryIndex = decodedUrl.indexOf('?'); // "?" character
            if (queryIndex != -1) {
                String[] paramPairs = decodedUrl.substring(queryIndex + 1).split("&"); // "&" character
                
                for (String paramPair : paramPairs) {
                    int equalsIndex = paramPair.indexOf('='); // "=" character
                    if (equalsIndex != -1) {
                        String key = paramPair.substring(0, equalsIndex);
                        String value = paramPair.substring(equalsIndex + 1);
                        parameterMap.put(key, value);
                    }
                }
            }

            return parameterMap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}