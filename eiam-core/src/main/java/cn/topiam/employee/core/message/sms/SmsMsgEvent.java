/*
 * eiam-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.core.message.sms;

import java.util.Map;

import org.springframework.context.ApplicationEvent;

import cn.topiam.employee.common.enums.SmsType;

import lombok.Getter;

/**
 * 短信消息事件
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/25 21:07
 */
@Getter
public class SmsMsgEvent extends ApplicationEvent {
    /**
     * 消息类型
     */
    private final SmsType             type;
    /**
     * 参数
     */
    private final Map<String, String> parameter;

    public SmsMsgEvent(SmsType type, Map<String, String> parameter) {
        super(parameter);
        this.type = type;
        this.parameter = parameter;
    }
}
