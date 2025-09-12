/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.authentication;

import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.LogoutSuccessEvent;

import com.google.common.collect.Lists;

import cn.topiam.employee.audit.entity.Target;
import cn.topiam.employee.audit.enums.EventStatus;
import cn.topiam.employee.audit.enums.TargetType;
import cn.topiam.employee.audit.event.AuditEventPublish;
import cn.topiam.employee.support.context.ApplicationContextService;
import cn.topiam.employee.support.security.userdetails.UserDetails;
import static cn.topiam.employee.audit.event.type.EventType.LOGOUT_CONSOLE;

/**
 * 退出成功
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/9/3 22:42
 */
public class ConsoleLogoutSuccessEventListener implements ApplicationListener<LogoutSuccessEvent> {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(@NonNull LogoutSuccessEvent event) {
        AuditEventPublish auditEventPublish = ApplicationContextService
            .getBean(AuditEventPublish.class);
        // 审计事件
        //@formatter:off
        UserDetails principal = (UserDetails) event.getAuthentication().getPrincipal();
        List<Target> targets= Lists.newArrayList(Target.builder().type(TargetType.CONSOLE).name(principal.getUsername()).id(principal.getId()).build());
        auditEventPublish.publish(LOGOUT_CONSOLE, event.getAuthentication(), EventStatus.SUCCESS,targets);
        //@formatter:on
    }
}
