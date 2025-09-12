/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.annotation;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.alibaba.fastjson2.JSON;

import lombok.AllArgsConstructor;

/**
 * AuditExpressionRoot
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/28 22:48
 */
@AllArgsConstructor
public class AuditExpressionRoot implements AuditExpressionOperations {

    /**
     * Gets the {@link Authentication} used for evaluating the expressions
     *
     * @return the {@link Authentication} for evaluating the expressions
     */
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 转为JSON字符串
     *
     * @param object {@link Object}
     * @return {@link String}
     */
    @Override
    public String toJsonString(Object object) {
        return JSON.toJSONString(object);
    }

}
