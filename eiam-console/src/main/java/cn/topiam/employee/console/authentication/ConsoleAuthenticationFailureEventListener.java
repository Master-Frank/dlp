/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.authentication;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;

import cn.topiam.employee.audit.entity.Actor;
import cn.topiam.employee.audit.enums.EventStatus;
import cn.topiam.employee.audit.event.AuditEventPublish;
import cn.topiam.employee.audit.event.type.EventType;
import cn.topiam.employee.common.entity.setting.AdministratorEntity;
import cn.topiam.employee.common.repository.setting.AdministratorRepository;
import cn.topiam.employee.support.context.ApplicationContextService;
import cn.topiam.employee.support.security.userdetails.UserDetails;
import cn.topiam.employee.support.security.userdetails.UserType;
import static cn.topiam.employee.core.security.util.SecurityUtils.getFailureMessage;

/**
 * 认证失败
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/9/3 22:42
 */
public class ConsoleAuthenticationFailureEventListener implements
                                                       ApplicationListener<AbstractAuthenticationFailureEvent> {
    private final Logger logger = LoggerFactory
        .getLogger(ConsoleAuthenticationFailureEventListener.class);

    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(@NonNull AbstractAuthenticationFailureEvent event) {
        //@formatter:off
        AuditEventPublish publish = ApplicationContextService.getBean(AuditEventPublish.class);
        String content = getFailureMessage(event);
        logger.error("Administrator account authentication failed :{}", event.getException().getMessage());
        String principal = getPrincipal(event);
        if (StringUtils.isNotBlank(principal)){
            Optional<AdministratorEntity> optional = getAdministratorRepository().findByUsername(principal);
            if (optional.isEmpty()) {
                // 手机号
                optional = getAdministratorRepository().findByPhone(principal);
                if (optional.isEmpty()) {
                    // 邮箱
                    optional = getAdministratorRepository().findByEmail(principal);
                }
            }
            if (optional.isEmpty()) {
                logger.error("The administrator account does not exist: [{}]", principal);
                return;
            }
            AdministratorEntity administrator = optional.get();
            Actor actor = Actor.builder().id(administrator.getId()).type(UserType.ADMIN).build();
            publish.publish(EventType.LOGIN_CONSOLE,content+"："+administrator.getUsername(), actor,EventStatus.FAIL);
        }
        //@formatter:on
    }

    private static String getPrincipal(AbstractAuthenticationFailureEvent event) {
        String principal = (String) event.getAuthentication().getPrincipal();
        if (event.getAuthentication().getPrincipal() instanceof String) {
            principal = (String) event.getAuthentication().getPrincipal();
        }
        if (event.getAuthentication().getPrincipal() instanceof UserDetails || event
            .getAuthentication()
            .getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            principal = ((UserDetails) event.getAuthentication().getPrincipal()).getUsername();
        }
        return principal;
    }

    private AdministratorRepository getAdministratorRepository() {
        return ApplicationContextService.getBean(AdministratorRepository.class);
    }

}
