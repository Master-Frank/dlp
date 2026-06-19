/*
 * ulp-audit - United Login Platform
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
package cn.frank.ulp.audit.event.type;

import java.util.List;

import cn.frank.ulp.audit.event.ConsoleResource;
import cn.frank.ulp.audit.event.Type;
import cn.frank.ulp.support.security.userdetails.UserType;
import static cn.frank.ulp.audit.event.PortalResource.MY_ACCOUNT_RESOURCE;
import static cn.frank.ulp.audit.event.PortalResource.MY_APP_RESOURCE;

/**
 * 门户端审计事件类型
 *
 * @author Frank Zhang
 */
public class PortalEventType {
    /**
     * 单点登录
     */
    public static Type APP_SSO                 = new Type("ulp:event:app:sso", "单点登录",
        MY_APP_RESOURCE, List.of(UserType.USER));
    /**
     * 退出应用
     */
    public static Type APP_SLO                 = new Type("ulp:event:app:login", "单点登出",
        MY_APP_RESOURCE, List.of(UserType.USER));

    /**
     * 绑定账号
     */
    public static Type BIND_IDP_USER           = new Type("ulp:event:account:bind_idp_user",
        "绑定 IDP", MY_ACCOUNT_RESOURCE, List.of(UserType.USER));
    /**
     * 解绑账号
     */
    public static Type UNBIND_IDP_USER         = new Type("ulp:event:account:unbind_idp_user",
        "解绑 IDP", MY_ACCOUNT_RESOURCE, List.of(UserType.USER));

    /**
     * 修改账户信息
     */
    public static Type MODIFY_ACCOUNT_INFO     = new Type("ulp:event:account:update_account_info",
        "修改账户", MY_ACCOUNT_RESOURCE, List.of(UserType.USER));

    /**
     * 修改邮箱
     */
    public static Type MODIFY_USER_EMAIL       = new Type("ulp:event:account:update_email", "修改邮箱",
        MY_ACCOUNT_RESOURCE, List.of(UserType.USER));
    /**
     * 修改手机号
     */
    public static Type MODIFY_USER_PHONE       = new Type("ulp:event:account:update_phone", "修改手机号",
        MY_ACCOUNT_RESOURCE, List.of(UserType.USER));
    /**
     * 修改密码
     */
    public static Type MODIFY_USER_PASSWORD    = new Type("ulp:event:account:update_password",
        "修改密码", MY_ACCOUNT_RESOURCE, List.of(UserType.USER));

    /**
     * 绑定MFA
     */
    public static Type BIND_MFA                = new Type("ulp:event:account:bind_mfa", "绑定 MFA",
        MY_ACCOUNT_RESOURCE, List.of(UserType.USER));

    /**
     * 准备修改密码
     */
    public static Type PREPARE_MODIFY_PASSWORD = new Type(
        "ulp:event:account:prepare_modify_password", "准备修改密码", MY_ACCOUNT_RESOURCE,
        List.of(UserType.USER));

    /**
     * 准备修改手机
     */
    public static Type PREPARE_MODIFY_PHONE    = new Type("ulp:event:account:prepare_modify_phone",
        "准备修改手机", MY_ACCOUNT_RESOURCE, List.of(UserType.USER));

    /**
     * 准备修改邮箱
     */
    public static Type PREPARE_MODIFY_EMAIL    = new Type("ulp:event:account:prepare_modify_email",
        "准备修改邮箱", MY_ACCOUNT_RESOURCE, List.of(UserType.USER));

    /**
     * 准备绑定MFA
     */
    public static Type PREPARE_BIND_MFA        = new Type("ulp:event:account:prepare_bind_mfa",
        "准备绑定MFA", MY_ACCOUNT_RESOURCE, List.of(UserType.USER));

    /**
     * 解绑mfa
     */
    public static Type UNBIND_MFA              = new Type("ulp:event:account:unbind_mfa", "解绑 MFA",
        MY_ACCOUNT_RESOURCE, List.of(UserType.USER));

    /**
     * MFA 二次验证已要求（成功登录主因子后进入 challenge 流）
     */
    public static Type MFA_CHALLENGE_REQUIRED  = new Type(
        "ulp:event:account:mfa_challenge_required", "MFA 二次验证已要求", MY_ACCOUNT_RESOURCE,
        List.of(UserType.USER));

    /**
     * MFA 二次验证成功
     */
    public static Type MFA_VERIFY_SUCCESS      = new Type("ulp:event:account:mfa_verify_success",
        "MFA 二次验证成功", MY_ACCOUNT_RESOURCE, List.of(UserType.USER));

    /**
     * MFA 二次验证失败
     */
    public static Type MFA_VERIFY_FAILURE      = new Type("ulp:event:account:mfa_verify_failure",
        "MFA 二次验证失败", MY_ACCOUNT_RESOURCE, List.of(UserType.USER));

    /**
     * 备份码已消费
     */
    public static Type BACKUP_CODE_USED        = new Type("ulp:event:account:backup_code_used",
        "MFA 备份码已消费", MY_ACCOUNT_RESOURCE, List.of(UserType.USER));

    /**
     * MFA 锁定（连续失败触发）
     */
    public static Type MFA_LOCKED_OUT          = new Type("ulp:event:account:mfa_locked_out",
        "MFA 已锁定", MY_ACCOUNT_RESOURCE, List.of(UserType.USER));

    /**
     * 登录门户
     */
    public static Type LOGIN_PORTAL            = new Type("ulp:event:login:portal", "登录门户",
        ConsoleResource.AUTHENTICATION_RESOURCE, List.of(UserType.USER));

    /**
     * 退出门户
     */
    public static Type LOGOUT_PORTAL           = new Type("ulp:event:logout:portal", "退出门户",
        ConsoleResource.AUTHENTICATION_RESOURCE, List.of(UserType.USER));
}
