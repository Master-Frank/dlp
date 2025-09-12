/*
 * eiam-application-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.application.exception;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * 应用未启用
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/7/8 22:23
 */
public class AppNotEnableException extends TopIamException {
    public AppNotEnableException() {
        super("app_not_enable", "应用未启用", DEFAULT_STATUS);
    }
}
