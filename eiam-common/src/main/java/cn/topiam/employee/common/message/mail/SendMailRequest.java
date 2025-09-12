/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.message.mail;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 发送邮件参数
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/27 21:08
 */
@Data
@Accessors(chain = true)
public class SendMailRequest {
    /**
     * 发送人
     */
    private String sender;
    /**
     * 收件人
     */
    private String receiver;
    /**
     * 主题
     */
    private String subject;
    /**
     * 内容
     */
    private String body;
}
