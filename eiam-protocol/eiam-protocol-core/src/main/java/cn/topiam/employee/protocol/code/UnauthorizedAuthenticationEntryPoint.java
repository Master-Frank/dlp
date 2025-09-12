/*
 * eiam-protocol-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.code;

import java.io.IOException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;

import cn.topiam.employee.core.context.ContextService;
import cn.topiam.employee.support.result.ApiRestResult;
import cn.topiam.employee.support.security.savedredirect.HttpSessionRedirectCache;
import cn.topiam.employee.support.security.savedredirect.RedirectCache;
import cn.topiam.employee.support.security.web.AbstractAuthenticationEntryPoint;
import cn.topiam.employee.support.util.HttpResponseUtils;
import cn.topiam.employee.support.web.useragent.UserAgentParser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import static cn.topiam.employee.support.context.ServletContextService.isHtmlRequest;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/5 21:24
 */
public class UnauthorizedAuthenticationEntryPoint extends AbstractAuthenticationEntryPoint {
    private final Logger        logger        = LoggerFactory.getLogger(this.getClass());
    private final RedirectCache redirectCache = new HttpSessionRedirectCache();

    public UnauthorizedAuthenticationEntryPoint(UserAgentParser userAgentParser) {
        super(userAgentParser);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        //记录
        redirectCache.saveRedirect(request, response, RedirectCache.RedirectType.REQUEST);
        //判断请求
        boolean isHtmlRequest = isHtmlRequest(request);
        // HTML
        if (isHtmlRequest) {
            //跳转前端SESSION过期路由
            response.sendRedirect(ContextService.getPortalLoginUrl());
            return;
        }
        // JSON
        ApiRestResult<Object> result = ApiRestResult.builder()
            .status(String.valueOf(UNAUTHORIZED.value()))
            .message(Objects.toString(authException.getMessage(), UNAUTHORIZED.getReasonPhrase()))
            .build();
        HttpResponseUtils.flushResponseJson(response, UNAUTHORIZED.value(), result);
    }
}
