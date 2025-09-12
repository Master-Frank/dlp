/*
 * eiam-identity-source-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.core.exception;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * 身份源不存在异常
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/7/8 22:23
 */
public class IdentitySourceNotExistException extends TopIamException {
    public IdentitySourceNotExistException() {
        super("identity_source_not_exist", "身份源不存在", DEFAULT_STATUS);
    }
}
