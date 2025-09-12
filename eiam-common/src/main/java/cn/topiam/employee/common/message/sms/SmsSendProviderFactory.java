/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.message.sms;

import cn.topiam.employee.common.message.enums.SmsProvider;
import cn.topiam.employee.common.message.sms.aliyun.AliyunSmsProviderConfig;
import cn.topiam.employee.common.message.sms.aliyun.AliyunSmsProviderSend;
import cn.topiam.employee.common.message.sms.qiniu.QiNiuSmsProviderConfig;
import cn.topiam.employee.common.message.sms.qiniu.QiNiuSmsProviderSend;
import cn.topiam.employee.common.message.sms.tencent.TencentSmsProviderConfig;
import cn.topiam.employee.common.message.sms.tencent.TencentSmsProviderSend;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/10 21:40
 */
public class SmsSendProviderFactory {

    private SmsSendProviderFactory() {
    }

    /**
     * 获取实例化
     *
     * @param config {@link SmsProviderConfig}
     * @return {@link SmsProviderSend}
     */
    public static SmsProviderSend newInstance(SmsProviderConfig config) {
        SmsProvider provider = config.getProvider();
        //阿里云
        if (SmsProvider.ALIYUN.equals(provider)) {
            return new AliyunSmsProviderSend((AliyunSmsProviderConfig) config);
        }
        //腾讯
        if (SmsProvider.TENCENT.equals(provider)) {
            return new TencentSmsProviderSend((TencentSmsProviderConfig) config);
        }
        //七牛
        if (SmsProvider.QINIU.equals(provider)) {
            return new QiNiuSmsProviderSend((QiNiuSmsProviderConfig) config);
        }
        throw new IllegalArgumentException("暂未支持该短信提供商类型");
    }
}
