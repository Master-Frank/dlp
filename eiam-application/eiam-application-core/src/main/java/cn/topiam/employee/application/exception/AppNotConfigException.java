/*
 * eiam-application-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.application.exception;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * 应用未配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/7/8 22:23
 */
public class AppNotConfigException extends TopIamException {
    public AppNotConfigException() {
        super("app_not_config", "应用未配置", DEFAULT_STATUS);
    }
}
