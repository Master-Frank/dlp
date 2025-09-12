/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.message.sms;

import java.io.Serial;
import java.io.Serializable;

import cn.topiam.employee.common.message.enums.SmsProvider;

import jakarta.validation.constraints.NotNull;

/**
 * 验证码提供商配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/10/1 21:10
 */
public class SmsProviderConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = 5611656522133230183L;

    /**
     * 平台
     */
    @NotNull(message = "平台类型不能为空")
    private SmsProvider       provider;

    public SmsProvider getProvider() {
        return provider;
    }

    public void setProvider(SmsProvider provider) {
        this.provider = provider;
    }

    public SmsProviderConfig() {
    }
}
