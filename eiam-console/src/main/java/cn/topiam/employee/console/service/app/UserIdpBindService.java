/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.app;

import java.util.List;

import cn.topiam.employee.console.pojo.result.app.UserIdpBindListResult;

/**
 * 用户身份提供商绑定
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/11 21:10
 */
public interface UserIdpBindService {
    /**
     * 解绑用户IDP
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean unbindUserIdpBind(String id);

    /**
     * 查询用户身份提供商绑定
     *
     * @param userId {@link String}
     * @return {@link List<UserIdpBindListResult>}
     */
    List<UserIdpBindListResult> getUserIdpBindList(String userId);
}
