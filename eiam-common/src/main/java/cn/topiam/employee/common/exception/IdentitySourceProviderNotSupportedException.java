/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.exception;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * 不支持该身份源提供商
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2024/4/10 22:23
 */
public class IdentitySourceProviderNotSupportedException extends TopIamException {
    public IdentitySourceProviderNotSupportedException() {
        super("identity_source_provider_not_supported", "不支持该身份源提供商", DEFAULT_STATUS);
    }
}
