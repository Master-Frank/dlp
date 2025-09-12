/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.authentication;

import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import lombok.AllArgsConstructor;

/**
 * 监听登录成功事件
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/9/3
 */
@AllArgsConstructor
public class PortalAuthenticationSuccessEventListener implements
                                                      ApplicationListener<AuthenticationSuccessEvent> {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(@NonNull AuthenticationSuccessEvent event) {

    }
}
