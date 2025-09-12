/*
 * eiam-protocol-form - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.form.configurers;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import cn.topiam.employee.application.ApplicationServiceLoader;
import cn.topiam.employee.protocol.code.configurer.AuthenticationUtils;
import cn.topiam.employee.protocol.form.FormAuthorizationService;
import cn.topiam.employee.protocol.form.InMemoryFormAuthorizationService;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/8 23:19
 */
public class FormAuthenticationUtils {

    public static FormAuthorizationService getAuthorizationService(HttpSecurity httpSecurity) {
        FormAuthorizationService authorizationService = httpSecurity
            .getSharedObject(FormAuthorizationService.class);
        if (authorizationService == null) {
            authorizationService = AuthenticationUtils.getOptionalBean(httpSecurity,
                FormAuthorizationService.class);
            if (authorizationService == null) {
                ApplicationServiceLoader applicationServiceLoader = AuthenticationUtils
                    .getOptionalBean(httpSecurity, ApplicationServiceLoader.class);
                authorizationService = new InMemoryFormAuthorizationService(
                    applicationServiceLoader);
            }
            httpSecurity.setSharedObject(FormAuthorizationService.class, authorizationService);
        }
        return authorizationService;
    }
}
