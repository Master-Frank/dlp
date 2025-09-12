/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.exception;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * OTP 发送异常
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/8/7 23:03
 */
public class OtpSendException extends TopIamException {

    public OtpSendException(String message) {
        super("otp_send_error", message, DEFAULT_STATUS);
    }

}
