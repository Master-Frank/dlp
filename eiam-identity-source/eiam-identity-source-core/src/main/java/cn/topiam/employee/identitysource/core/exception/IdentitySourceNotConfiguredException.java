/*
 * eiam-identity-source-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.core.exception;

import org.springframework.http.HttpStatus;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * 身份源未配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/7/8 22:23
 */
public class IdentitySourceNotConfiguredException extends TopIamException {
    public IdentitySourceNotConfiguredException() {
        super("identity_source_not_configured", "身份源未配置", HttpStatus.BAD_REQUEST);
    }
}
