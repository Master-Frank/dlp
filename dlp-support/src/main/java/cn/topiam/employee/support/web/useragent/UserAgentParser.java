package cn.topiam.employee.support.web.useragent;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户代理解析器接口
 * 用于解析HTTP请求中的User-Agent信息
 */
public interface UserAgentParser {
   
   /**
    * 获取用户代理信息
    *
    * @param request HTTP请求
    * @return 用户代理信息
    */
   UserAgent getUserAgent(HttpServletRequest request);
}