/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.app;

import java.util.List;

import cn.topiam.employee.console.pojo.query.app.AppCertQuery;
import cn.topiam.employee.console.pojo.result.app.AppCertListResult;

/**
 * APP 证书
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/31 21:45
 */
public interface AppCertService {
    /**
     * 获取应用证书列表
     *
     * @param query {@link AppCertQuery}
     * @return {@link List}
     */
    List<AppCertListResult> getAppCertListResult(AppCertQuery query);
}
