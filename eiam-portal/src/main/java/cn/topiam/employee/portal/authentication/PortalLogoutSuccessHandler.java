/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.authentication;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import cn.topiam.employee.core.context.ContextService;
import cn.topiam.employee.support.result.ApiRestResult;
import cn.topiam.employee.support.util.HttpResponseUtils;
import cn.topiam.employee.support.util.UrlUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static cn.topiam.employee.support.context.ServletContextService.isHtmlRequest;
import static cn.topiam.employee.support.result.ApiRestResult.SUCCESS;

/**
 * 注销成功
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/9/2 22:11
 */
public class PortalLogoutSuccessHandler implements
                                        org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
    /**
     * 日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException {
        //@formatter:off
        boolean isHtmlRequest = isHtmlRequest(request);
        if (response.isCommitted()) {
            return;
        }
        if (!isHtmlRequest) {
            HttpResponseUtils.flushResponseJson(response, HttpStatus.OK.value(),
                    ApiRestResult.builder().status(SUCCESS).build());
            return;
        }
        response.sendRedirect(UrlUtils.format(ContextService.getPortalLoginUrl()));
        //@formatter:on
    }
}
