/*
 * eiam-protocol-form - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.form;

import cn.topiam.employee.protocol.code.LoginAccount;
import cn.topiam.employee.protocol.form.client.FormRegisteredClient;
import cn.topiam.employee.support.security.userdetails.UserDetails;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/8 00:23
 */
public interface FormAuthorizationService {

    /**
     * 用户是否具有登录权限
     *
     * @param client {@link FormRegisteredClient}
     * @param user {@link UserDetails}
     * @return {@link Boolean}
     */
    Boolean hasLoginPermission(FormRegisteredClient client, UserDetails user);

    /**
     * 根据客户端&用户ID查询默认应用账户
     *
     * @param client {@link String} 客户端
     * @param  user {@link UserDetails} 用户
     * @return {@link String}
     */
    LoginAccount getDefaultLoginAccount(FormRegisteredClient client, UserDetails user);
}
