/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.storage;

import java.io.Serial;

import org.springframework.http.HttpStatus;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * 存储服务异常
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/19 22:53
 */
public class StorageProviderException extends TopIamException {
    @Serial
    private static final long serialVersionUID = 6249098979022610064L;

    public StorageProviderException(String msg, Throwable t) {
        super(msg, t);
    }

    public StorageProviderException(String msg) {
        super(msg);
    }

    public StorageProviderException(String msg, HttpStatus status) {
        super(msg, status);
    }

    public StorageProviderException(String error, String description, HttpStatus status) {
        super(error, description, status);
    }

    public StorageProviderException(Throwable cause, String error, String description,
                                    HttpStatus status) {
        super(cause, error, description, status);
    }
}
