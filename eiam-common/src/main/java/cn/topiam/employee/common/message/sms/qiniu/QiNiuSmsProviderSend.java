/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.message.sms.qiniu;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.sms.SmsManager;
import com.qiniu.util.Auth;

import cn.topiam.employee.common.message.enums.SmsProvider;
import cn.topiam.employee.common.message.sms.SendSmsRequest;
import cn.topiam.employee.common.message.sms.SmsProviderSend;
import cn.topiam.employee.common.message.sms.SmsResponse;

/**
 * 七牛短信发送
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/4/14 23:04
 */
public class QiNiuSmsProviderSend implements SmsProviderSend {
    private final SmsManager smsManager;

    public QiNiuSmsProviderSend(QiNiuSmsProviderConfig config) {
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        // 实例化一个SmsManager对象
        this.smsManager = new SmsManager(auth);
    }

    @Override
    public SmsResponse send(SendSmsRequest sendSmsParam) {
        try {
            Response resp = smsManager.sendSingleMessage(sendSmsParam.getTemplate(),
                sendSmsParam.getPhone(), sendSmsParam.getParameters());
            JSONObject response = JSON.parseObject(resp.bodyString());
            if (response.containsKey("message_id")
                && StringUtils.hasText(response.getString("message_id"))) {
                return new SmsResponse("成功", Boolean.TRUE, SmsProvider.QINIU);
            }
        } catch (QiniuException e) {
            log.error("七牛发送短信异常: [{}]", e.getMessage());
        }
        return new SmsResponse("失败", Boolean.FALSE, SmsProvider.QINIU);
    }

    @Override
    public SmsProvider getProvider() {
        return SmsProvider.QINIU;
    }
}
