/*
 * eiam-authentication-sms - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.otp.sms.exception;

import cn.topiam.employee.support.exception.TopIamException;

/**
 * 手机号不存在异常
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/1/2 21:59
 */
public class PhoneNotExistException extends TopIamException {
    public PhoneNotExistException() {
        super("phone_not_exist", "手机号不存在", DEFAULT_STATUS);
    }

}
