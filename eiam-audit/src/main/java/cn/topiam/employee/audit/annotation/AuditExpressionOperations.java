/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.annotation;

import org.springframework.security.core.Authentication;

/**
 * Audit 解析器
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/28 22:45
 */
public interface AuditExpressionOperations {

    /**
     * Gets the {@link Authentication} used for evaluating the expressions
     *
     * @return the {@link Authentication} for evaluating the expressions
     */
    Authentication getAuthentication();

    /**
     * 转JSON字符串
     *
     * @param object {@link Object}
     * @return {@link String}
     */
    String toJsonString(Object object);

}
