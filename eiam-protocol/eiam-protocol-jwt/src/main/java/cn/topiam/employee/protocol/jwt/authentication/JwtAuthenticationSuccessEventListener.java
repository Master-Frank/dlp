/*
 * eiam-protocol-jwt - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.jwt.authentication;

import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

import cn.topiam.employee.application.jwt.model.JwtProtocolConfig;
import cn.topiam.employee.audit.entity.Target;
import cn.topiam.employee.audit.enums.EventStatus;
import cn.topiam.employee.audit.enums.TargetType;
import cn.topiam.employee.audit.event.AuditEventPublish;
import static cn.topiam.employee.audit.event.type.EventType.APP_SSO;

/**
 * 监听登录成功事件
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/9/3
 */
public class JwtAuthenticationSuccessEventListener implements
                                                   ApplicationListener<AuthenticationSuccessEvent> {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(@NonNull AuthenticationSuccessEvent event) {
        //@formatter:off
        if (event.getAuthentication() instanceof JwtAuthenticationToken authentication){
            JwtProtocolConfig config = authentication.getConfig();
            auditEventPublish.publish(APP_SSO, (Authentication) authentication.getPrincipal(),EventStatus.SUCCESS,Lists.newArrayList(Target.builder().id(config.getAppId()).type(TargetType.APPLICATION).name(config.getAppName()).build()));
        }
        //@formatter:on
    }

    private final AuditEventPublish auditEventPublish;

    public JwtAuthenticationSuccessEventListener(AuditEventPublish auditEventPublish) {
        Assert.notNull(auditEventPublish, "auditEventPublish must not be null ");
        this.auditEventPublish = auditEventPublish;
    }
}
