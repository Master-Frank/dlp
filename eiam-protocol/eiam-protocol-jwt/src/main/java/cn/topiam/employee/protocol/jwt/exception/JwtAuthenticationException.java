/*
 * eiam-protocol-jwt - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.jwt.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

import lombok.Getter;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/8 00:27
 */
@Getter
public class JwtAuthenticationException extends AuthenticationException {

    private final JwtError error;

    public JwtAuthenticationException(JwtError error) {
        super(error.getDescription());
        this.error = error;
    }

    public JwtAuthenticationException(JwtError error, Throwable cause) {
        this(error, error.getDescription(), cause);
    }

    public JwtAuthenticationException(JwtError error, String message, Throwable cause) {
        super(message, cause);
        Assert.notNull(error, "error cannot be null");
        this.error = error;
    }
}
