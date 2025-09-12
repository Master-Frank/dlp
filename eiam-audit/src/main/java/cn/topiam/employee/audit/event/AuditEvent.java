/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.event;

import java.io.Serial;
import java.util.List;

import org.springframework.context.ApplicationEvent;

import cn.topiam.employee.audit.entity.*;

import lombok.Getter;

/**
 * 审计事件
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/8/1 21:56
 */
@Getter
public class AuditEvent extends ApplicationEvent {
    @Serial
    private static final long  serialVersionUID = 3425943796938543659L;

    private final String       requestId;
    private final String       sessionId;
    private final Actor        actor;
    private final Event        event;
    private final List<Target> targets;
    private final UserAgent    userAgent;
    private final GeoLocation  geoLocation;

    public AuditEvent(String requestId, String sessionId, Actor actor, Event event,
                      UserAgent userAgent, GeoLocation geoLocation, List<Target> targets) {
        super(requestId);
        this.requestId = requestId;
        this.sessionId = sessionId;
        this.actor = actor;
        this.event = event;
        this.targets = targets;
        this.userAgent = userAgent;
        this.geoLocation = geoLocation;
    }
}
