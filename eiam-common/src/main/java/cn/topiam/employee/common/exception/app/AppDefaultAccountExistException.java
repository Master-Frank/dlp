/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.exception.app;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * 默认应用账户已存在
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/7/8 22:49
 */
public class AppDefaultAccountExistException extends TopIamException {

    public AppDefaultAccountExistException() {
        super("app_default_account_exist", "默认应用账户已存在", DEFAULT_STATUS);
    }
}
