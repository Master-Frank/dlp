/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.message.sms;

import java.io.Serializable;

import cn.topiam.employee.common.message.enums.SmsProvider;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 短信发送返回
 *
 * @author TopIAM
 */
@Data
@AllArgsConstructor
public class SmsResponse implements Serializable {

    /**
     * 消息
     */
    private String      message;

    /**
     * 是否成功
     */
    private Boolean     success;

    /**
     * 提供商
     */
    private SmsProvider provider;
}
