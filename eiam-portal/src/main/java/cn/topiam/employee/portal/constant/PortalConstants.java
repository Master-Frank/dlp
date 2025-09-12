/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.constant;

import static cn.topiam.employee.support.constant.EiamConstants.V1_API_PATH;

/**
 * 认证常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/8 21:29
 */
public class PortalConstants {

    public static final String ACCOUNT_PATH            = V1_API_PATH + "/account";

    /**
     * 忘记密码预认证
     */
    public static final String PREPARE_FORGET_PASSWORD = "/prepare_forget_password";

    /**
     * 忘记密码
     */
    public static final String FORGET_PASSWORD         = "/forget_password";

    /**
     * 忘记密码发送验证码
     */
    public static final String FORGET_PASSWORD_CODE    = "/forget_password_code";
}
