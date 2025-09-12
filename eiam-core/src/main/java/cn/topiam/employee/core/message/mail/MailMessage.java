/*
 * eiam-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.core.message.mail;

import java.io.Serializable;
import java.util.Map;

import cn.topiam.employee.common.enums.MailType;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息事件
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/25 21:07
 */
@Data
@NoArgsConstructor
public class MailMessage implements Serializable {

    /**
     * 消息类型
     */
    private MailType            type;

    /**
     * 接收人
     */
    private String              receiver;

    /**
     * 参数
     */
    private Map<String, Object> parameter;

    public MailMessage(MailType type, String receiver, Map<String, Object> parameter) {
        this.type = type;
        this.receiver = receiver;
        this.parameter = parameter;
    }
}
