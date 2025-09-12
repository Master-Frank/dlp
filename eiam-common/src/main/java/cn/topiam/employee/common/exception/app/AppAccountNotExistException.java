/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.exception.app;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * 应用账户不存在异常
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/7/8 22:49
 */
public class AppAccountNotExistException extends TopIamException {

    public AppAccountNotExistException() {
        super("app_account_not_exist", "应用账户不存在", DEFAULT_STATUS);
    }
}
