/*
 * eiam-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.core.message.sms;

import java.time.LocalDateTime;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;

import cn.topiam.employee.common.entity.message.SmsSendRecordEntity;
import cn.topiam.employee.common.enums.MessageCategory;
import cn.topiam.employee.common.exception.SmsMessageSendException;
import cn.topiam.employee.common.message.sms.SendSmsRequest;
import cn.topiam.employee.common.message.sms.SmsProviderSend;
import cn.topiam.employee.common.message.sms.SmsResponse;
import cn.topiam.employee.common.repository.message.SmsSendRecordRepository;

/**
 * 短信消息通知事件
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/25 21:07
 */
@Async
@Component
public class SmsMsgEventListener implements ApplicationListener<SmsMsgEvent> {
    /**
     * Logger
     */
    private final Logger logger = LoggerFactory.getLogger(SmsMsgEventListener.class);

    /**
     * 发送事件通知
     *
     * @param event {@link SmsMsgEvent}
     */
    @Override
    public void onApplicationEvent(@NonNull SmsMsgEvent event) {
        SendSmsRequest smsParam = new SendSmsRequest();
        try {
            //@formatter:off
            // 手机号
            smsParam.setPhone(event.getParameter().get(SmsMsgEventPublish.PHONE));
            // 模版编码
            smsParam.setTemplate(event.getParameter().get(SmsMsgEventPublish.TEMPLATE_CODE));
            // Content 记录参数值
            String content = event.getParameter().get(SmsMsgEventPublish.CONTENT);
            // 移除手机号，模版编码和Content
            event.getParameter().remove(SmsMsgEventPublish.PHONE);
            event.getParameter().remove(SmsMsgEventPublish.TEMPLATE_CODE);
            event.getParameter().remove(SmsMsgEventPublish.CONTENT);
            // 短信模版参数
            smsParam.setParameters(event.getParameter());

            //@formatter:on
            SmsResponse send = smsProviderSend.send(smsParam);
            // 保存发送记录
            if (!Objects.isNull(send)) {
                //@formatter:off
                SmsSendRecordEntity record = new SmsSendRecordEntity()
                        .setContent(content)
                        .setResult(send.getMessage())
                        .setSuccess(send.getSuccess())
                        .setSendTime(LocalDateTime.now())
                        .setProvider(send.getProvider())
                        .setCategory(MessageCategory.CODE)
                        .setType(event.getType())
                        .setPhone(smsParam.getPhone());
                record.setRemark(JSON.toJSONString(send));
                if (!send.getSuccess()) {
                    logger.error("发送短信失败: params: {}, response: {}", smsParam, send);
                }
                //@formatter:on
                smsSendLogRepository.save(record);
            } else {
                logger.error("发送短信失败,返回值为空: params: {}, ", smsParam);
            }
        } catch (Exception e) {
            logger.error("发送短信消息异常 params:{}, error: {}", smsParam, e.getMessage());
            throw new SmsMessageSendException(e);
        }
    }

    /**
     * SmsSend
     */
    private final SmsProviderSend         smsProviderSend;

    private final SmsSendRecordRepository smsSendLogRepository;

    public SmsMsgEventListener(SmsProviderSend smsProviderSend,
                               SmsSendRecordRepository smsSendLogRepository) {
        this.smsProviderSend = smsProviderSend;
        this.smsSendLogRepository = smsSendLogRepository;
    }
}
