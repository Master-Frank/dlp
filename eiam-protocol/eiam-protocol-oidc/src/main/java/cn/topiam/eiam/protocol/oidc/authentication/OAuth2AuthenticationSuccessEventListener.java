/*
 * eiam-protocol-oidc - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.eiam.protocol.oidc.authentication;

import java.security.Principal;
import java.util.Objects;

import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcLogoutAuthenticationToken;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

import cn.topiam.employee.audit.entity.Target;
import cn.topiam.employee.audit.enums.EventStatus;
import cn.topiam.employee.audit.enums.TargetType;
import cn.topiam.employee.audit.event.AuditEventPublish;
import cn.topiam.employee.audit.event.type.EventType;
import static cn.topiam.eiam.protocol.oidc.constant.OidcProtocolConstants.ID_TOKEN;

/**
 * 监听登录成功事件
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/9/3
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class OAuth2AuthenticationSuccessEventListener implements
                                                      ApplicationListener<AuthenticationSuccessEvent> {

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(@NonNull AuthenticationSuccessEvent event) {
        //@formatter:off
        if (event.getAuthentication() instanceof OAuth2AccessTokenAuthenticationToken authorization){
            OAuth2Authorization token = authorizationService.findByToken(authorization.getAccessToken().getTokenValue(), OAuth2TokenType.ACCESS_TOKEN);
            if (!Objects.isNull(token) && !Objects.isNull(token.getAttribute(Principal.class.getName()))){
                Authentication principal = token.getAttribute(Principal.class.getName());
                if (!Objects.isNull(principal)) {
                    Target target = Target.builder().id(authorization.getRegisteredClient().getId()).type(TargetType.APPLICATION).name(authorization.getRegisteredClient().getClientName()).build();
                    auditEventPublish.publish(EventType.APP_SSO, principal, EventStatus.SUCCESS, Lists.newArrayList(target));
                }
            }
        }
        if (event.getAuthentication() instanceof OidcLogoutAuthenticationToken authorization) {
            OAuth2Authorization token = authorizationService.findByToken(authorization.getIdTokenHint(), ID_TOKEN);
            if (!Objects.isNull(token) && !Objects.isNull(token.getAttribute(Principal.class.getName()))){
                Authentication principal = token.getAttribute(Principal.class.getName());
                if (!Objects.isNull(principal)) {
                    Target target = Target.builder().id(token.getRegisteredClientId()).name(token.getPrincipalName()).type(TargetType.APPLICATION).build();
                    auditEventPublish.publish(EventType.APP_SLO, principal, EventStatus.SUCCESS, Lists.newArrayList(target));
                }
            }
        }
        //@formatter:on
    }

    private final AuditEventPublish          auditEventPublish;

    private final OAuth2AuthorizationService authorizationService;

    public OAuth2AuthenticationSuccessEventListener(AuditEventPublish auditEventPublish,
                                                    OAuth2AuthorizationService authorizationService) {
        Assert.notNull(auditEventPublish, "auditEventPublish must not be null ");
        Assert.notNull(authorizationService, "authorizationService must not be null ");
        this.auditEventPublish = auditEventPublish;
        this.authorizationService = authorizationService;
    }
}
