/*
 * eiam-protocol-jwt - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.jwt.exception;

/**
 * 异常CODE
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/19 21:19
 */
public final class JwtErrorCodes {

    /**
     * 无效请求
     */
    public static final String INVALID_REQUEST         = "invalid_request";

    /**
     * 服务器异常
     */
    public static final String SERVER_ERROR            = "server_error";

    /**
     * 生成ID token 异常
     */
    public static final String GENERATE_ID_TOKEN_ERROR = "generate_id_token_error";

    /**
     * 配置错误
     */
    public static final String CONFIG_ERROR            = "config_error";

    /**
     * 应用账户不存在
     */
    public static final String APP_ACCOUNT_NOT_EXIST   = "app_account_not_exist";

}
