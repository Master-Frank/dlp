/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.app.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.topiam.employee.common.entity.app.AppCertEntity;
import cn.topiam.employee.common.repository.app.AppCertRepository;
import cn.topiam.employee.console.converter.app.AppCertConverter;
import cn.topiam.employee.console.pojo.query.app.AppCertQuery;
import cn.topiam.employee.console.pojo.result.app.AppCertListResult;
import cn.topiam.employee.console.service.app.AppCertService;

import lombok.AllArgsConstructor;

/**
 * 应用证书
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/31 21:46
 */
@Service
@AllArgsConstructor
public class AppCertServiceImpl implements AppCertService {

    /**
     * 获取应用证书列表
     *
     * @param query {@link AppCertQuery}
     * @return {@link List}
     */
    @Override
    public List<AppCertListResult> getAppCertListResult(AppCertQuery query) {
        List<AppCertEntity> list = appCertRepository
            .findAll(appCertConverter.queryAppCertListParamConvertToExample(query));
        return appCertConverter.entityConvertToAppCertListResult(list);
    }

    private final AppCertRepository appCertRepository;

    private final AppCertConverter  appCertConverter;
}
