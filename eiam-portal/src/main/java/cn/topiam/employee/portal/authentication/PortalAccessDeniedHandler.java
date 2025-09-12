/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.authentication;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.web.csrf.CsrfException;

import cn.topiam.employee.support.security.util.SecurityUtils;

import lombok.AllArgsConstructor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 访问拒绝处理程序
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/9/2 22:11
 */
@AllArgsConstructor
public class PortalAccessDeniedHandler implements
                                       org.springframework.security.web.access.AccessDeniedHandler {
    /**
     * 日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Handles an access denied failure.
     *
     * @param request               that resulted in an <code>AccessDeniedException</code>
     * @param response              so that the user agent can be advised of the failure
     * @param accessDeniedException that caused the invocation
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        logger.info("----------------------------------------------------------");
        if (!(accessDeniedException.getCause() instanceof CsrfException)) {
            logger.info(accessDeniedException.getMessage());
        }
        if (!(accessDeniedException.getCause() instanceof AuthorizationServiceException)) {
            logger.info("用户 [{}] 权限不足", SecurityUtils.getCurrentUser());
        }
        response.sendError(HttpStatus.FORBIDDEN.value(),
            accessDeniedException.getLocalizedMessage());
        logger.info("----------------------------------------------------------");
    }

}
