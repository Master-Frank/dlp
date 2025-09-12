/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.message.mail;

import cn.topiam.employee.common.message.enums.MailProvider;

/**
 * None
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/27 21:06
 */
public class MailNoneProviderSend implements MailProviderSend {

    @Override
    public void sendMail(SendMailRequest sendMailParam) {

    }

    @Override
    public void sendMailHtml(SendMailRequest sendMailParam) {

    }

    @Override
    public MailProvider getProvider() {
        return null;
    }
}
