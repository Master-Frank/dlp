/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.event.type;

import java.util.List;

import cn.topiam.employee.audit.event.ConsoleResource;
import cn.topiam.employee.audit.event.Type;
import cn.topiam.employee.support.security.userdetails.UserType;
import static cn.topiam.employee.audit.event.ConsoleResource.IDP_RESOURCE;

/**
 * 认证资源
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/24 22:59
 */
public class AuthenticationEventType {

    /**
     * 添加身份提供商
     */
    public static Type ADD_IDP        = new Type("eiam:event:idp_add", "添加身份提供商", IDP_RESOURCE,
        List.of(UserType.ADMIN));
    /**
     * 编辑身份提供商
     */
    public static Type UPDATE_IDP     = new Type("eiam:event:idp_update", "修改身份提供商", IDP_RESOURCE,
        List.of(UserType.ADMIN));
    /**
     * 启用身份提供商
     */
    public static Type ENABLE_IDP     = new Type("eiam:event:idp_enabled", "启用身份提供商", IDP_RESOURCE,
        List.of(UserType.ADMIN));
    /**
     * 禁用身份提供商
     */
    public static Type DISABLE_IDP    = new Type("eiam:event:idp_disabled", "禁用身份提供商", IDP_RESOURCE,
        List.of(UserType.ADMIN));
    /**
     * 删除身份提供商
     */
    public static Type DELETE_IDP     = new Type("eiam:event:idp_delete", "删除身份提供商", IDP_RESOURCE,
        List.of(UserType.ADMIN));

    /**
     * 登录控制台
     */
    public static Type LOGIN_CONSOLE  = new Type("eiam:event:login:console", "登录控制台",
        ConsoleResource.AUTHENTICATION_RESOURCE, List.of(UserType.ADMIN));

    /**
     * 退出控制台
     */
    public static Type LOGOUT_CONSOLE = new Type("eiam:event:logout:console", "退出控制台",
        ConsoleResource.AUTHENTICATION_RESOURCE, List.of(UserType.ADMIN));

}
