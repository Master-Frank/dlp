/*
 * ulp-support - ULP support library
 * Copyright (c) 2022-Present Frank Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.frank.ulp.support.security.constant;

/**
 * 安全常量类
 * 定义安全相关的常量
 */
public class SecurityConstants {
    /**
    * 表单登录路径
    */
    public static final String FORM_LOGIN                    = "/api/v1/login";

    /**
    * 登出路径
    */
    public static final String LOGOUT_PATH                   = "/api/v1/logout";

    /**
    * 登录路径
    */
    public static final String LOGIN_PATH                    = "/api/v1/login";

    /**
    * 需要重置密码标识
    */
    public static final String REQUIRE_RESET_PASSWORD        = "require_reset_password";

    /**
    * 重置密码路径
    */
    public static final String RESET_PASSWORD_PATH           = "/api/v1/reset_password";

    /**
    * 密码无效错误
    */
    public static final String PASSWORD_INVALID_ERROR        = "password_invalid_error";

    /**
    * 密码校验失败错误
    */
    public static final String PASSWORD_VALIDATED_FAIL_ERROR = "password_validated_fail_error";

    /**
    * 未知认证类型
    */
    public static final String UNKNOWN_AUTHENTICATION_TYPE   = "unknown_authentication_type";

    /**
    * MFA 第二因子挑战中标识：成功响应附带此 status，前端据此跳 {@link #MFA_CHALLENGE_PATH}。
    */
    public static final String MFA_REQUIRED                  = "mfa_required";

    /**
    * MFA 第二因子挑战页路径（前端路由 + 302 跳转目标）。
    */
    public static final String MFA_CHALLENGE_PATH            = "/mfa/challenge";

    /**
    * MFA 待挑战 Cookie 名：HttpOnly + Secure + SameSite=Strict + Path=/，5min TTL，
    * 内容为 {@code ULP_MFA_PENDING:{uuid}} 中的 UUID。
    */
    public static final String MFA_PENDING_COOKIE            = "ulp-mfa-pending";

    /**
    * 组织强制 MFA 但用户未绑定，需要先去 setup 标识。Phase 4 portal 使用，console 不会触发。
    */
    public static final String MFA_SETUP_REQUIRED            = "mfa_setup_required";

    /**
    * MFA setup 页路径。
    */
    public static final String MFA_SETUP_PATH                = "/mfa/setup";
}