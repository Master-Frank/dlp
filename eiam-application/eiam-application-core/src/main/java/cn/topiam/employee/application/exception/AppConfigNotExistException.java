/*
 * eiam-application-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.application.exception;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * 应用配置不存在
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/7/8 22:21
 */
public class AppConfigNotExistException extends TopIamException {
    public AppConfigNotExistException() {
        super("app_config_not_exist", "应用配置不存在", DEFAULT_STATUS);
    }
}
