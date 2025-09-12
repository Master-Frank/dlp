/*
 * eiam-protocol-form - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.form.endpoint.authentication;

import org.apache.commons.collections4.MapUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;

import cn.topiam.employee.application.context.ApplicationContext;
import cn.topiam.employee.application.context.ApplicationContextHolder;
import cn.topiam.employee.application.form.model.FormProtocolConfig;
import cn.topiam.employee.protocol.form.authentication.FormRequestAuthenticationToken;
import cn.topiam.employee.protocol.form.exception.FormAuthenticationException;
import cn.topiam.employee.protocol.form.exception.FormError;

import jakarta.servlet.http.HttpServletRequest;
import static cn.topiam.employee.protocol.form.constant.FormProtocolConstants.FORM_ERROR_URI;
import static cn.topiam.employee.protocol.form.exception.FormErrorCodes.SERVER_ERROR;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/8 00:14
 */
public final class FormAuthenticationTokenConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        ApplicationContext context = ApplicationContextHolder.getApplicationContext();
        if (MapUtils.isEmpty(context.getConfig())
            | !context.getConfig().containsKey(FormProtocolConfig.class.getName())) {
            FormError error = new FormError(SERVER_ERROR, null, FORM_ERROR_URI);
            throw new FormAuthenticationException(error);
        }
        FormProtocolConfig config = (FormProtocolConfig) context.getConfig()
            .get(FormProtocolConfig.class.getName());
        return new FormRequestAuthenticationToken(authentication, config);
    }

}
