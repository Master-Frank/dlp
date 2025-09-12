/*
 * eiam-authentication-sms - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.otp.sms.constant;

import static cn.topiam.employee.support.security.constant.SecurityConstants.LOGIN_PATH;

/**
 * 验证码认证常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/19 23:19
 */
public final class SmsOtpAuthenticationConstants {

    public static final String RECIPIENT_KEY = "recipient";
    public static final String CODE_KEY      = "code";
    /**
     * 验证码登录路径
     */
    public static final String OTP_LOGIN     = LOGIN_PATH + "/otp";
}