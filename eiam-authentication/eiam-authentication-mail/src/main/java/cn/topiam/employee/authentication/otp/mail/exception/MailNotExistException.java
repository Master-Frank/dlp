/*
 * eiam-authentication-mail - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.otp.mail.exception;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * 邮箱不存在异常
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/1/2 21:59
 */
public class MailNotExistException extends TopIamException {
    public MailNotExistException() {
        super("mail_not_exist", "邮箱不存在", DEFAULT_STATUS);
    }

}
