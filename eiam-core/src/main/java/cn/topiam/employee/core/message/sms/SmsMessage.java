/*
 * eiam-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.core.message.sms;

import java.io.Serializable;
import java.util.Map;

import cn.topiam.employee.common.enums.SmsType;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短信消息事件
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/25 21:07
 */
@Data
@NoArgsConstructor
public class SmsMessage implements Serializable {
    /**
     * 消息类型
     */
    private SmsType             type;
    /**
     * 参数
     */
    private Map<String, String> parameter;

    public SmsMessage(SmsType type, Map<String, String> parameter) {
        this.type = type;
        this.parameter = parameter;
    }
}
