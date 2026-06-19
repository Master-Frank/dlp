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

import cn.frank.ulp.audit.event.Type;
import static cn.frank.ulp.audit.event.ConsoleResource.*;
import static cn.frank.ulp.support.security.userdetails.UserType.ADMIN;

/**
 * 账户资源
 *
 * @author Frank Zhang
 */
public class AccountEventType {

    /**
     * 创建用户
     */
    public static Type CREATE_USER                           = new Type(
        "ulp:event:account:create_user", "创建用户", ORG_ACCOUNT_RESOURCE, List.of(ADMIN));
    /**
     * 编辑用户
     */
    public static Type UPDATE_USER                           = new Type(
        "ulp:event:account:update_user", "修改用户", ORG_ACCOUNT_RESOURCE, List.of(ADMIN));
    /**
     * 删除用户
     */
    public static Type DELETE_USER                           = new Type(
        "ulp:event:account:delete_user", "删除用户", ORG_ACCOUNT_RESOURCE, List.of(ADMIN));
    /**
     * 禁用用户
     */
    public static Type DISABLE_USER                          = new Type(
        "ulp:event:account:disabled_user", "禁用用户", ORG_ACCOUNT_RESOURCE, List.of(ADMIN));
    /**
     * 启用用户
     */
    public static Type ENABLE_USER                           = new Type(
        "ulp:event:account:enabled_user", "启用用户", ORG_ACCOUNT_RESOURCE, List.of(ADMIN));
    /**
     * 创建组织
     */
    public static Type CREATE_ORG                            = new Type(
        "ulp:event:account:create_organization", "创建组织", ORG_ACCOUNT_RESOURCE, List.of(ADMIN));
    /**
     * 编辑组织
     */
    public static Type UPDATE_ORG                            = new Type(
        "ulp:event:account:update_organization", "修改组织", ORG_ACCOUNT_RESOURCE, List.of(ADMIN));
    /**
     * 删除组织
     */
    public static Type DELETE_ORGANIZATION                   = new Type(
        "ulp:event:account:delete_organization", "删除组织", ORG_ACCOUNT_RESOURCE, List.of(ADMIN));

    /**
     * 移动组织
     */
    public static Type MOVE_ORGANIZATION                     = new Type(
        "ulp:event:account:move_organization", "移动组织", ORG_ACCOUNT_RESOURCE, List.of(ADMIN));

    /**
     * 添加组织用户
     */
    public static Type CREATE_ORGANIZATION_MEMBER            = new Type(
        "ulp:event:account:create_organization_member", "添加组织用户", ORG_ACCOUNT_RESOURCE,
        List.of(ADMIN));

    /**
     * 用户离职
     */
    public static Type USER_RESIGN                           = new Type(
        "ulp:event:account:user_resign", "用户离职", ORG_ACCOUNT_RESOURCE, List.of(ADMIN));

    /**
     * 修改邮箱
     */
    public static Type MODIFY_USER_EMAIL                     = new Type(
        "ulp:event:account:update_email", "修改邮箱", ORG_ACCOUNT_RESOURCE, List.of(ADMIN));
    /**
     * 修改手机号
     */
    public static Type MODIFY_USER_PHONE                     = new Type(
        "ulp:event:account:update_phone", "修改手机号", ORG_ACCOUNT_RESOURCE, List.of(ADMIN));
    /**
     * 修改密码
     */
    public static Type MODIFY_USER_PASSWORD                  = new Type(
        "ulp:event:account:update_password", "修改密码", ORG_ACCOUNT_RESOURCE, List.of(ADMIN));

    /**
     * 解锁用户
     */
    public static Type UNLOCK_USER                           = new Type(
        "ulp:event:account:unlock_user", "解锁用户", ORG_ACCOUNT_RESOURCE, List.of(ADMIN));
    /**
     * 创建用户组
     */
    public static Type CREATE_USER_GROUP                     = new Type(
        "ulp:event:account:create_user_group", "创建静态用户组", USER_GROUP_RESOURCE, List.of(ADMIN));
    /**
     * 编辑用户组
     */
    public static Type UPDATE_USER_GROUP                     = new Type(
        "ulp:event:account:update_user_group", "修改静态用户组", USER_GROUP_RESOURCE, List.of(ADMIN));
    /**
     * 删除用户组
     */
    public static Type DELETE_USER_GROUP                     = new Type(
        "ulp:event:account:delete_user_group", "删除静态用户组", USER_GROUP_RESOURCE, List.of(ADMIN));

    /**
     * 添加用户组成员
     */
    public static Type ADD_USER_GROUP_MEMBER                 = new Type(
        "ulp:event:account:add_user_group_member", "添加静态用户组成员", USER_GROUP_RESOURCE,
        List.of(ADMIN));
    /**
     * 移除用户组成员
     */
    public static Type REMOVE_USER_GROUP_MEMBER              = new Type(
        "ulp:event:account:remove_user_group_member", "移除静态用户组成员", USER_GROUP_RESOURCE,
        List.of(ADMIN));

    /**
     * 创建动态用户组
     */
    public static Type CREATE_USER_DYNAMIC_GROUP             = new Type(
        "ulp:event:account:create_user_dynamic_group", "创建动态用户组", USER_GROUP_RESOURCE,
        List.of(ADMIN));
    /**
     * 编辑动态用户组
     */
    public static Type UPDATE_USER_DYNAMIC_GROUP             = new Type(
        "ulp:event:account:update_user_dynamic_group", "修改动态用户组", USER_GROUP_RESOURCE,
        List.of(ADMIN));

    /**
     * 编辑动态用户组过滤规则
     */
    public static Type UPDATE_USER_DYNAMIC_GROUP_FILTER_RULE = new Type(
        "ulp:event:account:update_user_dynamic_group:filter_rule", "编辑动态用户组过滤规则",
        USER_GROUP_RESOURCE, List.of(ADMIN));

    /**
     * 删除动态用户组
     */
    public static Type DELETE_USER_DYNAMIC_GROUP             = new Type(
        "ulp:event:account:delete_user_dynamic_group", "删除动态用户组", USER_GROUP_RESOURCE,
        List.of(ADMIN));

    /**
     * 创建身份源
     */
    public static Type CREATE_IDENTITY_RESOURCE              = new Type(
        "ulp:event:account:create_identity_resource", "创建身份源", IDENTITY_SOURCE_RESOURCE,
        List.of(ADMIN));
    /**
     * 编辑身份源
     */
    public static Type UPDATE_IDENTITY_RESOURCE              = new Type(
        "ulp:event:account:update_identity_resource", "修改身份源", IDENTITY_SOURCE_RESOURCE,
        List.of(ADMIN));
    /**
     * 删除身份源
     */
    public static Type DELETE_IDENTITY_RESOURCE              = new Type(
        "ulp:event:account:delete_identity_resource", "删除身份源", IDENTITY_SOURCE_RESOURCE,
        List.of(ADMIN));

    /**
     * 启用身份源
     */
    public static Type ENABLE_IDENTITY_RESOURCE              = new Type(
        "ulp:event:account:enable_identity_resource", "启用身份源", IDENTITY_SOURCE_RESOURCE,
        List.of(ADMIN));

    /**
     * 禁用身份源
     */
    public static Type DISABLE_IDENTITY_RESOURCE             = new Type(
        "ulp:event:account:disable_identity_resource", "禁用身份源", IDENTITY_SOURCE_RESOURCE,
        List.of(ADMIN));

    /**
     * 禁用身份源
     */
    public static Type IDENTITY_RESOURCE_SYNC                = new Type(
        "ulp:event:account:identity_resource_sync", "身份源同步", IDENTITY_SOURCE_RESOURCE,
        List.of(ADMIN));

    /**
     * 管理员重置 MFA（target 可以是 user 或 admin）
     */
    public static Type ADMIN_RESET_USER_MFA                  = new Type(
        "ulp:event:account:admin_reset_user_mfa", "管理员重置 MFA", ORG_ACCOUNT_RESOURCE,
        List.of(ADMIN));

    /**
     * 组织强制 MFA 策略变更
     */
    public static Type ORG_MFA_POLICY_CHANGED                = new Type(
        "ulp:event:account:org_mfa_policy_changed", "组织强制 MFA 策略变更", ORG_ACCOUNT_RESOURCE,
        List.of(ADMIN));

}
