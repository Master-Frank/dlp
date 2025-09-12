/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.event;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSONObject;

import cn.topiam.employee.audit.entity.*;
import cn.topiam.employee.audit.repository.AuditRepository;

/**
 * 事件监听
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/12 22:49
 */
@Component
@Async
public class AuditEventListener implements ApplicationListener<AuditEvent> {

    private final Logger logger = LoggerFactory.getLogger(AuditEventListener.class);

    /**
     * onApplicationEvent
     *
     * @param auditEvent {@link AuditEvent}
     */
    @Override
    public void onApplicationEvent(@NonNull AuditEvent auditEvent) {
        Event event = auditEvent.getEvent();
        Actor actor = auditEvent.getActor();
        List<Target> target = auditEvent.getTargets();
        GeoLocation geoLocation = auditEvent.getGeoLocation();
        UserAgent userAgent = auditEvent.getUserAgent();
        //保存数据库
        AuditEntity entity = new AuditEntity();
        try {
            entity.setRequestId(auditEvent.getRequestId());
            entity.setSessionId(auditEvent.getSessionId());
            //事件
            entity.setEventType(event.getType());
            entity.setEventContent(event.getContent());
            entity.setEventParam(event.getParam());
            entity.setEventStatus(event.getStatus());
            entity.setEventResult(event.getResult());
            entity.setEventTime(event.getTime());
            //操作目标
            entity.setTargets(target);
            entity.setGeoLocation(geoLocation);
            entity.setUserAgent(userAgent);
            entity.setActorId(actor.getId());
            entity.setActorType(actor.getType());
            entity.setActorAuthType(actor.getAuthType());
            auditRepository.save(entity);
        } catch (Exception e) {
            logger.error("Audit record saving failed: {}", JSONObject.toJSONString(entity), e);
        }

    }

    /**
     * AuditRepository
     */
    private final AuditRepository auditRepository;

    public AuditEventListener(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

}
