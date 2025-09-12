/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.message.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.topiam.employee.common.message.enums.SmsProvider;

/**
 * 短信发送
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/27 21:03
 */
public interface SmsProviderSend {

    Logger log = LoggerFactory.getLogger(SmsProviderSend.class);

    /**
     * 发送短信验证码
     * <p>
     * 如果是腾讯云，params要用LinkedHashMap，保证顺序
     *
     * @param request {@link SendSmsRequest}
     * @return {@link SmsResponse}
     */
    SmsResponse send(SendSmsRequest request);

    /**
     * 服务商类型
     *
     * @return {@link SmsProvider}
     */
    SmsProvider getProvider();
}
