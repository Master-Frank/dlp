/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.message.sms;

import cn.topiam.employee.common.message.enums.SmsProvider;

/**
 * None
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/4/20 23:48
 */
public class SmsNoneProviderSend implements SmsProviderSend {
    @Override
    public SmsResponse send(SendSmsRequest request) {
        return null;
    }

    @Override
    public SmsProvider getProvider() {
        return null;
    }
}
