/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.event.type;

import java.util.List;

import cn.topiam.employee.audit.event.Type;
import cn.topiam.employee.support.security.userdetails.UserType;
import static cn.topiam.employee.audit.event.ConsoleResource.*;

/**
 * 系统设置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/24 22:58
 */
public class SettingEventType {

    /**
     * 保存安全基础设置
     */
    public static Type SAVE_LOGIN_SECURITY_BASIC_SETTINGS = new Type(
        "eiam:event:setting:save_security_basic", "保存安全基础设置", SECURITY_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 保存密码策略
     */
    public static Type SAVE_PASSWORD_POLICY               = new Type(
        "eiam:event:setting:save_password_policy", "保存密码策略", PASSWORD_POLICY_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 修改密码策略
     */
    public static Type UPDATE_PASSWORD_POLICY             = new Type(
        "eiam:event:setting:update_password_policy", "修改密码策略", PASSWORD_POLICY_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 删除密码策略
     */
    public static Type DELETE_PASSWORD_POLICY             = new Type(
        "eiam:event:setting:delete_password_policy", "删除密码策略", PASSWORD_POLICY_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 启用密码策略
     */
    public static Type ENABLE_PASSWORD_POLICY             = new Type(
        "eiam:event:setting:enable_password_policy", "启用密码策略", PASSWORD_POLICY_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 禁用密码策略
     */
    public static Type DISABLE_PASSWORD_POLICY            = new Type(
        "eiam:event:setting:disable_password_policy", "禁用密码策略", PASSWORD_POLICY_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 更新密码策略优先级
     */
    public static Type SORT_PASSWORD_POLICY               = new Type(
        "eiam:event:setting:sort_password_policy", "更新密码策略优先级", PASSWORD_POLICY_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 保存安全策略
     */
    public static Type SAVE_SECURITY_POLICY_SETTINGS      = new Type(
        "eiam:event:setting:save_security_policy", "保存安全防御策略", SECURITY_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 添加角色
     */
    public static Type ADD_ADMINISTRATOR_ROLE             = new Type(
        "eiam:event:setting:add_administrator_role", "添加角色", ADMINISTRATOR_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 删除角色
     */
    public static Type DELETE_ADMINISTRATOR_ROLE          = new Type(
        "eiam:event:setting:delete_administrator_role", "删除角色", ADMINISTRATOR_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 修改角色
     */
    public static Type UPDATE_ADMINISTRATOR_ROLE          = new Type(
        "eiam:event:setting:update_administrator_role", "修改角色", ADMINISTRATOR_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 启用角色
     */
    public static Type ENABLE_ADMINISTRATOR_ROLE          = new Type(
        "eiam:event:setting:enable_administrator_role", "启用角色", ADMINISTRATOR_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 禁用角色
     */
    public static Type DISABLE_ADMINISTRATOR_ROLE         = new Type(
        "eiam:event:setting:disable_administrator_role", "禁用角色", ADMINISTRATOR_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 添加管理员
     */
    public static Type ADD_ADMINISTRATOR                  = new Type(
        "eiam:event:setting:add_administrator", "添加管理员", ADMINISTRATOR_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 删除管理员
     */
    public static Type DELETE_ADMINISTRATOR               = new Type(
        "eiam:event:setting:delete_administrator", "删除管理员", ADMINISTRATOR_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 修改管理员
     */
    public static Type UPDATE_ADMINISTRATOR               = new Type(
        "eiam:event:setting:update_administrator", "修改管理员", ADMINISTRATOR_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 启用管理员
     */
    public static Type ENABLE_ADMINISTRATOR               = new Type(
        "eiam:event:setting:enable_administrator", "启用管理员", ADMINISTRATOR_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 禁用管理员
     */
    public static Type DISABLE_ADMINISTRATOR              = new Type(
        "eiam:event:setting:disable_administrator", "禁用管理员", ADMINISTRATOR_RESOURCE,
        List.of(UserType.ADMIN));
    /**
     * 重置管理员密码
     */
    public static Type RESET_ADMINISTRATOR_PASSWORD       = new Type(
        "eiam:event:setting:reset_administrator_password", "重置管理员密码", ADMINISTRATOR_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 保存邮件模板
     */
    public static Type SAVE_MAIL_TEMPLATE                 = new Type(
        "eiam:event:setting:save_mail_template", "保存邮件模板", MESSAGE_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 关闭邮件服务
     */
    public static Type OFF_MAIL_SERVICE                   = new Type(
        "eiam:event:setting:off_mail_service", "关闭邮件服务", MESSAGE_RESOURCE, List.of(UserType.ADMIN));

    /**
     * 保存邮件服务
     */
    public static Type SAVE_MAIL_SERVICE                  = new Type(
        "eiam:event:setting:save_mail_service", "保存邮件服务", MESSAGE_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 关闭短信验证服务
     */
    public static Type OFF_SMS_SERVICE                    = new Type(
        "eiam:event:setting:off_sms_verify_service", "关闭短信验证服务", MESSAGE_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 保存短信验证服务
     */
    public static Type SAVE_SMS_SERVICE                   = new Type(
        "eiam:event:setting:save_sms_verify_service", "保存短信验证服务", MESSAGE_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 关闭存储服务
     */
    public static Type OFF_STORAGE_SERVICE                = new Type(
        "eiam:event:setting:off_storage_service", "关闭存储服务", STORAGE_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 保存存储服务
     */
    public static Type SAVE_STORAGE_SERVICE               = new Type(
        "eiam:event:setting:save_storage_service", "保存存储服务", STORAGE_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 关闭地理位置服务
     */
    public static Type OFF_GEO_LOCATION_SERVICE           = new Type(
        "eiam:event:setting:off_geoip_service", "关闭地理位置服务", GEO_LOCATION_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 保存地理位置服务
     */
    public static Type SAVE_GEO_LOCATION_SERVICE          = new Type(
        "eiam:event:setting:save_geoip_service", "保存地理位置服务", GEO_LOCATION_RESOURCE,
        List.of(UserType.ADMIN));
}
