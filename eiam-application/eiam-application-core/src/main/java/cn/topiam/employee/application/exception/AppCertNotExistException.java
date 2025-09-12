/*
 * eiam-application-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.application.exception;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * 应用证书不存在
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/7/8 22:21
 */
public class AppCertNotExistException extends TopIamException {
    public AppCertNotExistException() {
        super("应用证书不存在");
    }
}
