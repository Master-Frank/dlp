/*
 * eiam-authentication-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.common.exception;

import cn.topiam.employee.support.exception.TopIamException;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * 身份提供商模版不存在
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/7/8 22:49
 */
public class IdentityProviderTemplateNotExistException extends TopIamException {

    public IdentityProviderTemplateNotExistException() {
        super("idp_template_not_exist", "身份提供商模版不存在", BAD_REQUEST);
    }
}
