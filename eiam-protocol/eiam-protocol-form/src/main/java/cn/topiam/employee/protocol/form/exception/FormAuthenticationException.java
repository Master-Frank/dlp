/*
 * eiam-protocol-form - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.form.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.util.Assert;

import lombok.Getter;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/8 00:27
 */
@Getter
public class FormAuthenticationException extends AuthenticationException {

    private final FormError error;

    public FormAuthenticationException(FormError error) {
        super(error.getDescription());
        this.error = error;
    }

    public FormAuthenticationException(FormError error, Throwable cause) {
        this(error, error.getDescription(), cause);
    }

    public FormAuthenticationException(FormError error, String message, Throwable cause) {
        super(message, cause);
        Assert.notNull(error, "error cannot be null");
        this.error = error;
    }
}
