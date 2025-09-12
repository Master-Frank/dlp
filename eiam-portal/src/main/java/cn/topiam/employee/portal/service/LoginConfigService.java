/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.service;

import cn.topiam.employee.portal.pojo.result.LoginConfigResult;

/**
 * LoginConfigService
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/25 21:49
 */
public interface LoginConfigService {

    /**
     * 获取登录配置
     *
     * @return {@link LoginConfigResult}
     */
    LoginConfigResult getLoginConfig();
}
