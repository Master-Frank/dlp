/*
 * eiam-authentication-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.common.exception;

import org.springframework.http.HttpStatus;

import cn.topiam.employee.support.exception.TopIamException;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/8/13 23:58
 */
public class UserBindIdentityProviderException extends TopIamException {
    public static final String USER_BIND_IDP_ERROR = "user_bind_idp_error";

    public UserBindIdentityProviderException() {
        super(USER_BIND_IDP_ERROR, "用户绑定身份提供商错误", HttpStatus.FORBIDDEN);
    }

    public UserBindIdentityProviderException(String description, HttpStatus status) {
        super(USER_BIND_IDP_ERROR, description, status);
    }

    public UserBindIdentityProviderException(String error, String description, HttpStatus status) {
        super(error, description, status);
    }
}
