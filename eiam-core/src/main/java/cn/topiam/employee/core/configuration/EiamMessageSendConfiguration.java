/*
 * eiam-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.core.configuration;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import cn.topiam.employee.common.entity.setting.config.SmsConfig;
import cn.topiam.employee.common.message.mail.DefaultMailProviderSendImpl;
import cn.topiam.employee.common.message.mail.MailProviderConfig;
import cn.topiam.employee.common.message.mail.MailProviderSend;
import cn.topiam.employee.common.message.sms.SmsNoneProviderSend;
import cn.topiam.employee.common.message.sms.SmsProviderSend;
import cn.topiam.employee.common.message.sms.SmsSendProviderFactory;
import static org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME;

import static cn.topiam.employee.common.constant.ConfigBeanNameConstants.MAIL_PROVIDER_SEND;
import static cn.topiam.employee.common.constant.ConfigBeanNameConstants.SMS_PROVIDER_SEND;
import static cn.topiam.employee.core.context.ContextService.getMailProviderConfig;
import static cn.topiam.employee.core.context.ContextService.getSmsProviderConfig;

/**
 * 消息发送配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/25 23:48
 */
@Configuration
public class EiamMessageSendConfiguration {

    /**
     * 短信发送
     *
     * @return {@link SmsProviderSend}
     */
    @Bean(SMS_PROVIDER_SEND)
    @RefreshScope
    public SmsProviderSend smsProviderSend() {
        //查询当前启用的短信提供商
        SmsConfig config = getSmsProviderConfig();
        if (Objects.isNull(config.getConfig())) {
            return new SmsNoneProviderSend();
        }
        return SmsSendProviderFactory.newInstance(config.getConfig());
    }

    /**
     * 邮件发送
     *
     * @param taskExecutor {@link TaskExecutor}
     * @return {@link MailProviderSend}
     */
    @Bean(MAIL_PROVIDER_SEND)
    @RefreshScope
    public MailProviderSend mailProviderSend(@Qualifier(value = APPLICATION_TASK_EXECUTOR_BEAN_NAME) TaskExecutor taskExecutor) {

        MailProviderConfig config = getMailProviderConfig();
        if (Objects.isNull(config)) {
            return null;
        }
        return new DefaultMailProviderSendImpl(config, taskExecutor);
    }
}
