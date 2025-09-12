/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.message.mail;

import cn.topiam.employee.common.message.enums.MailProvider;

/**
 * 邮件收发统一接口
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/27 21:06
 */
public interface MailProviderSend {

    /**
     * 发送普通邮件
     *
     * @param sendMailParam 发送邮件的参数
     */
    void sendMail(SendMailRequest sendMailParam);

    /**
     * 发送html的邮件
     *
     * @param sendMailParam 发送邮件的参数
     */
    void sendMailHtml(SendMailRequest sendMailParam);

    /**
     * 服务商类型
     * @return {@link MailProvider}
     */
    MailProvider getProvider();
}
