/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.app.impl;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.topiam.employee.application.ApplicationService;
import cn.topiam.employee.application.ApplicationServiceLoader;
import cn.topiam.employee.common.enums.app.AppType;
import cn.topiam.employee.console.pojo.result.app.AppTemplateResult;
import cn.topiam.employee.console.service.app.AppTemplateService;

import lombok.AllArgsConstructor;

/**
 * ApplicationTemplateServiceImpl
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/11/29 22:23
 */
@Service
@AllArgsConstructor
public class AppTemplateServiceImpl implements AppTemplateService {

    /**
     * List
     *
     * @param type {@link AppType}
     * @return {@link List}
     */
    @Override
    public List<AppTemplateResult> getAppTemplateList(AppType type, String name) {
        List<AppTemplateResult> results = new ArrayList<>();
        List<ApplicationService> list = applicationServiceLoader.getApplicationServiceList();
        if (StringUtils.isNotBlank(name)) {
            list = list.stream()
                .filter(applicationService -> applicationService.getName().contains(name)).toList();
        }
        for (ApplicationService protocol : list) {
            if (protocol.getType().equals(type)) {
                AppTemplateResult result = new AppTemplateResult();
                result.setProtocol(protocol.getProtocol());
                result.setCode(protocol.getCode());
                result.setDesc(protocol.getDescription());
                result.setIcon(protocol.getBase64Icon());
                result.setName(protocol.getName());
                result.setType(protocol.getType());
                results.add(result);
            }
        }
        return results;
    }

    /**
     * List
     *
     * @param code {@link Map}
     * @return {@link List}
     */
    @Override
    public Object getAppTemplateFormSchema(String code) {
        ApplicationService applicationService = applicationServiceLoader
            .getApplicationService(code);
        if (!Objects.isNull(applicationService)) {
            return applicationService.getFormSchema();
        }
        return new ArrayList<>();
    }

    private final ApplicationServiceLoader applicationServiceLoader;

}
