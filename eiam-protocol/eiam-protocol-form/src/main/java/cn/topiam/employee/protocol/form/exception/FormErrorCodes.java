/*
 * eiam-protocol-form - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.form.exception;

/**
 * 异常CODE
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/19 21:19
 */
public final class FormErrorCodes {

    /**
     * 无效请求
     */
    public static final String INVALID_REQUEST       = "invalid_request";

    /**
     * 应用账户不存在
     */
    public static final String APP_ACCOUNT_NOT_EXIST = "app_account_not_exist";

    /**
     * 无效授权
     */
    public static final String INVALID_GRANT         = "invalid_grant";

    /**
     * 服务器异常
     */
    public static final String SERVER_ERROR          = "server_error";

}
