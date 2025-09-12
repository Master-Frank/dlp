/*
 * eiam-openapi - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.openapi.exception.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import cn.topiam.employee.openapi.common.OpenApiResponse;
import cn.topiam.employee.openapi.constant.OpenApiStatus;
import cn.topiam.employee.openapi.exception.OpenApiException;

import lombok.AllArgsConstructor;

import jakarta.validation.ConstraintViolationException;

/**
 * 全局异常处理
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/20 21:55
 */
@AllArgsConstructor
@RestControllerAdvice
public class OpenApiGlobalExceptionHandler {
    /**
     * Exception
     *
     * @return {@link OpenApiResponse}
     */
    @ExceptionHandler(value = Exception.class)
    public OpenApiResponse exception(Exception e) {
        return new OpenApiResponse(OpenApiStatus.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
    }

    /**
     * ConstraintViolationException
     *
     * @return {@link ModelAndView}
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public OpenApiResponse constraintViolationException(ConstraintViolationException e) {
        return new OpenApiResponse(OpenApiStatus.INVALID_PARAMETER.getCode(), e.getMessage());
    }

    /**
     * OpenApiException
     *
     * @return {@link OpenApiResponse}
     */
    @ExceptionHandler(value = OpenApiException.class)
    public OpenApiResponse exception(OpenApiException e) {
        return new OpenApiResponse(e.getCode(), e.getMessage());
    }
}
