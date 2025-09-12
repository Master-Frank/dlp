/*
 * eiam-authentication-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.common.exception;

import org.springframework.http.HttpStatus;

/**
 * idp 用户存在绑定
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/8/13 23:58
 */
public class IdentityProviderUserExistBindException extends UserBindIdentityProviderException {
    public IdentityProviderUserExistBindException() {
        super("idp_user_exist_bind", "IDP用户存在绑定", HttpStatus.BAD_REQUEST);
    }
}
