/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import cn.topiam.employee.audit.enums.EventStatus;
import cn.topiam.employee.audit.event.type.EventType;

import lombok.Builder;
import lombok.Data;

/**
 * Event
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/11/5 23:33
 */
@Data
@Builder
public class Event implements Serializable {

    @Serial
    private static final long  serialVersionUID = -1144169992714000310L;

    public static final String EVENT_TYPE       = "event.type.keyword";

    public static final String EVENT_TIME       = "event.time";

    public static final String EVENT_STATUS     = "event.status.keyword";

    /**
     * 审计事件类型
     */
    private EventType          type;

    /**
     * 参数
     */
    private String             param;

    /**
     * 事件内容
     */
    private String             content;

    /**
     * 事件结果
     */
    private String             result;

    /**
     * 事件时间
     */
    private LocalDateTime      time;

    /**
     * 事件状态
     */
    private EventStatus        status;

}
