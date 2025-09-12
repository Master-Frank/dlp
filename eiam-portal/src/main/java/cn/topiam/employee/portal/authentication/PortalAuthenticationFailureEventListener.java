/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.authentication;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;

import cn.topiam.employee.audit.entity.Actor;
import cn.topiam.employee.audit.enums.EventStatus;
import cn.topiam.employee.audit.event.AuditEventPublish;
import cn.topiam.employee.audit.event.type.EventType;
import cn.topiam.employee.common.entity.account.UserEntity;
import cn.topiam.employee.common.enums.UserStatus;
import cn.topiam.employee.common.repository.account.UserRepository;
import cn.topiam.employee.core.context.ContextService;
import cn.topiam.employee.portal.service.UserService;
import cn.topiam.employee.support.context.ApplicationContextService;
import cn.topiam.employee.support.security.userdetails.UserDetails;
import static cn.topiam.employee.core.context.ContextService.getLoginFailureDuration;
import static cn.topiam.employee.core.security.util.SecurityUtils.getFailureMessage;
import static cn.topiam.employee.support.security.userdetails.UserType.USER;

/**
 * 认证失败
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/9/3 22:42
 */
public class PortalAuthenticationFailureEventListener implements
                                                      ApplicationListener<AbstractAuthenticationFailureEvent> {

    private final Logger logger = LoggerFactory
        .getLogger(PortalAuthenticationFailureEventListener.class);

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
        logger.error("User account authentication failed :{}", event.getException().getMessage());
        String principal = null;
        if (event.getAuthentication().getPrincipal() instanceof String) {
            principal = (String) event.getAuthentication().getPrincipal();
        }
        if (event.getAuthentication().getPrincipal() instanceof UserDetails || event.getAuthentication().getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
            principal = ((UserDetails) event.getAuthentication().getPrincipal()).getUsername();
        }
        if (StringUtils.isNotBlank(principal)) {
            Optional<UserEntity> optional = getUserService().findByUsernameOrPhoneOrEmail(principal);
            if (optional.isEmpty()) {
                logger.error("The account does not exist: [{}]", principal);
                return;
            }
            UserEntity user= optional.get();
            Actor actor = Actor.builder().id(user.getId()).type(USER).build();
            publish.publish(EventType.LOGIN_PORTAL, content + "：" + user.getUsername(), actor, EventStatus.FAIL);
            //更新登录失败计数
            updateLoginFailCount(user);
        }
        //@formatter:on
    }

    /**
     * 更新登录失败计数
     *
     * @param user {@link UserEntity}
     */
    private void updateLoginFailCount(UserEntity user) {
        Integer count = ContextService.getLoginFailureCount();
        UserRepository userRepository = getUserRepository();
        //统计用户登录失败次数
        RAtomicLong rAtomicLong = getRedissonClient().getAtomicLong(user.getId());
        //如果存在该key
        if (rAtomicLong.isExists()) {
            long loginFailCount = rAtomicLong.incrementAndGet();
            if (loginFailCount > count) {
                user.setStatus(UserStatus.LOCKED);
                user.setLockExpiredTime(LocalDateTime.now());
                userRepository.save(user);
                rAtomicLong.deleteAsync();
            }
        }
        //如果不存在该key，并且用户未被锁定
        if (!rAtomicLong.isExists() && !user.getStatus().equals(UserStatus.LOCKED)) {
            rAtomicLong.incrementAndGet();
            Instant expireTime = Instant.now().plus(getLoginFailureDuration(), ChronoUnit.MINUTES);
            rAtomicLong.expire(expireTime);
        }
    }

    private UserRepository getUserRepository() {
        return ApplicationContextService.getBean(UserRepository.class);
    }

    private UserService getUserService() {
        return ApplicationContextService.getBean(UserService.class);
    }

    private RedissonClient getRedissonClient() {
        return ApplicationContextService.getBean(RedissonClient.class);
    }

}
