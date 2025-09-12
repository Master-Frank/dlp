/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.exception.app;

import org.springframework.http.HttpStatus;

import cn.topiam.employee.support.exception.TopIamException;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2024/3/4 16:14
 */
public class AppAccessDeniedException extends TopIamException {

    public AppAccessDeniedException() {
        super("app_access_denied", "应用访问权限不足", HttpStatus.FORBIDDEN);
    }
}
