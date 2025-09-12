/*
 * eiam-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.core.security.util;

import org.springframework.security.authentication.event.*;

/**
 * Spring Security的实用程序类。
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2019/5/7
 */
public final class SecurityUtils {
    /**
     * 匿名用户
     */
    public static final String ANONYMOUS_USER = "anonymousUser";

    private SecurityUtils() {
    }

    /**
     * 获取错误信息
     *
     * @param event {@link AbstractAuthenticationFailureEvent}
     * @return {@link String}
     */
    public static String getFailureMessage(AbstractAuthenticationFailureEvent event) {
        String message = "未知错误";
        if (event instanceof AuthenticationFailureBadCredentialsEvent) {
            message = "登录密码错误";
        }
        if (event instanceof AuthenticationFailureCredentialsExpiredEvent) {
            message = "账户密码过期";
        }
        if (event instanceof AuthenticationFailureDisabledEvent) {
            message = "账户已禁用";
        }
        if (event instanceof AuthenticationFailureExpiredEvent) {
            message = "帐户已过期";
        }
        if (event instanceof AuthenticationFailureLockedEvent) {
            message = "账户已锁定";
        }
        if (event instanceof AuthenticationFailureProxyUntrustedEvent) {
            message = "代理不受信任";
        }
        if (event instanceof AuthenticationFailureProviderNotFoundEvent) {
            message = "提供商配置错误";
        }
        if (event instanceof AuthenticationFailureServiceExceptionEvent) {
            message = "发生内部异常";
        }
        return message;
    }

}
