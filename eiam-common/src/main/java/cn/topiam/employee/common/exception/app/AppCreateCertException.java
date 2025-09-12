/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.exception.app;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2024/2/18 18:13
 */
public class AppCreateCertException extends TopIamException {

    public AppCreateCertException() {
        super("app_create_cert_error", "创建应用证书失败", DEFAULT_STATUS);
    }
}
