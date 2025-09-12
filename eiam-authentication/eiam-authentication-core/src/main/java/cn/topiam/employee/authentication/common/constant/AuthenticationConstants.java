/*
 * eiam-authentication-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.common.constant;

/**
 * 认证常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/20 23:19
 */
public final class AuthenticationConstants {

    /**
     * 提供商ID
     */
    public static final String PROVIDER_CODE                              = "provider_code";
    public static final String INVALID_STATE_PARAMETER_ERROR_CODE         = "invalid_state_parameter";
    public static final String INVALID_NONCE_PARAMETER_ERROR_CODE         = "invalid_nonce_parameter";
    public static final String INVALID_CODE_PARAMETER_ERROR_CODE          = "invalid_code_parameter";
    public static final String BIND_AFTER_AUTH                            = "bind_after_auth";
    public static final String AUTHORIZATION_REQUEST_NOT_FOUND_ERROR_CODE = "authorization_request_not_found";

    /**
     * Session Attribute
     */
    public static final String BIND_REDIRECT                              = "bind_redirect";

}
