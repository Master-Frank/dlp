/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.exception;

import java.io.Serial;

import org.springframework.http.HttpStatus;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * 邮件模板异常
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/18 21:36
 */
public class MailTemplateException extends TopIamException {
    @Serial
    private static final long serialVersionUID = -6497956209061617684L;

    public MailTemplateException(String msg, Throwable t) {
        super(msg, t);
    }

    public MailTemplateException(String msg) {
        super(msg);
    }

    public MailTemplateException(String msg, HttpStatus status) {
        super(msg, status);
    }

    public MailTemplateException(String error, String description, HttpStatus status) {
        super(error, description, status);
    }

    public MailTemplateException(Throwable cause, String error, String description,
                                 HttpStatus status) {
        super(cause, error, description, status);
    }
}
