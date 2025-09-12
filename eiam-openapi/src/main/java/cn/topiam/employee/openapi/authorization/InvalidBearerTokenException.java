/*
 * eiam-openapi - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.openapi.authorization;

import org.springframework.security.core.AuthenticationException;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/25 22:23
 */
public class InvalidBearerTokenException extends AuthenticationException {

    /**
     * Constructs an {@code AuthenticationException} with the specified message and root
     * cause.
     *
     * @param msg   the detail message
     * @param cause the root cause
     */
    public InvalidBearerTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and no
     * root cause.
     *
     * @param msg the detail message
     */
    public InvalidBearerTokenException(String msg) {
        super(msg);
    }

}
