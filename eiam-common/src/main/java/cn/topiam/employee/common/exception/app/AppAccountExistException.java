/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.exception.app;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * 应用账户已存在
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/7/8 22:49
 */
public class AppAccountExistException extends TopIamException {

    public AppAccountExistException() {
        super("app_account_exist", "应用账户已存在", DEFAULT_STATUS);
    }
}
