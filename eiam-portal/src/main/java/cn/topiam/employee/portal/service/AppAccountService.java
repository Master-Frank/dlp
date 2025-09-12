/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.service;

import java.util.List;

import cn.topiam.employee.application.AppAccount;
import cn.topiam.employee.portal.pojo.request.AppAccountRequest;

/**
 * 应用账户
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/25 21:07
 */
public interface AppAccountService {

    /**
     * 新增应用账户
     *
     * @param param {@link AppAccountRequest}
     * @return {@link Boolean}
     */
    Boolean createAppAccount(AppAccountRequest param);

    /**
     * 删除应用账户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean deleteAppAccount(String id);

    /**
     * 获取当前登陆者应用账户
     * @param appId {@link String}
     * @return {@link List <AppAccount>}
     */
    List<AppAccount> getAppAccountList(String appId);

    /**
     * 设置默认应用账户
     *
     * @param id {@link String}
     * @param defaulted {@link Boolean}
     * @return {@link Boolean}
     */
    Boolean updateAppAccountDefault(String id, Boolean defaulted);
}
