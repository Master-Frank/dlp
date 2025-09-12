/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.message.sms;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

/**
 * 短信发送入参
 *
 * @author TopIAM
 */
@Data
public class SendSmsRequest implements Serializable {

    /**
     * 手机号
     */
    private String              phone;

    /**
     * 模板
     */
    private String              template;

    /**
     * 参数
     */
    private Map<String, String> parameters;
}
