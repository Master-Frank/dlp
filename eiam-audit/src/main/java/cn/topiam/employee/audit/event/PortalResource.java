/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 审计资源
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/29 21:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class PortalResource extends Resource {
    /**
     * 资源编码
     */
    private String code;
    /**
     * 资源名称
     */
    private String name;

    @Override
    public String toString() {
        return String.format("[%s](%s)", name, code);
    }

    /**
     * 组织与用户
     */
    public static PortalResource MY_ACCOUNT_RESOURCE = new PortalResource(
        "eiam:event:resource:my_account", "我的账户");

    /**
     * 用户组管理
     */
    public static PortalResource MY_APP_RESOURCE     = new PortalResource(
        "eiam:event:resource:my_app", "我的应用");

    /**
     * 会话管理
     */
    public static PortalResource SESSION_RESOURCE    = new PortalResource(
        "eiam:event:resource:session", "会话管理");
}