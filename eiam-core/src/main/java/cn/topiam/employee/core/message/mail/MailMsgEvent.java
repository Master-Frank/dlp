/*
 * eiam-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.core.message.mail;

import java.util.Map;

import org.springframework.context.ApplicationEvent;

import cn.topiam.employee.common.enums.MailType;

import lombok.Getter;

/**
 * 消息事件
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/25 21:07
 */
@Getter
public class MailMsgEvent extends ApplicationEvent {
    /**
     * 消息类型
     */
    private final MailType            type;
    /**
     * 接收人
     */
    private final String              receiver;
    /**
     * 参数
     */
    private final Map<String, Object> parameter;

    public MailMsgEvent(MailType type, String receiver, Map<String, Object> parameter) {
        super(type);
        this.type = type;
        this.receiver = receiver;
        this.parameter = parameter;
    }
}
