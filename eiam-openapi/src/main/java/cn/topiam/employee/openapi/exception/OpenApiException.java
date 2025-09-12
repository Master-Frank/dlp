/*
 * eiam-openapi - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.openapi.exception;

import cn.topiam.employee.openapi.constant.OpenApiStatus;

/**
 * OpenApiException
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/25 03:40
 */
public class OpenApiException extends RuntimeException {

    private final String code;
    private final String message;

    public OpenApiException(OpenApiStatus status) {
        super(status.getDesc());
        this.code = status.getCode();
        this.message = status.getDesc();
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
