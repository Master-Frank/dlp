/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.converter.app;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.springframework.util.CollectionUtils;

import cn.topiam.employee.application.ApplicationService;
import cn.topiam.employee.application.ApplicationServiceLoader;
import cn.topiam.employee.common.entity.app.AppAccessPolicyEntity;
import cn.topiam.employee.common.entity.app.po.AppAccessPolicyPO;
import cn.topiam.employee.common.entity.app.query.AppAccessPolicyQueryParam;
import cn.topiam.employee.console.pojo.query.app.AppAccessPolicyQuery;
import cn.topiam.employee.console.pojo.result.app.AppAccessPolicyResult;
import cn.topiam.employee.console.pojo.save.app.AppAccessPolicyCreateParam;
import cn.topiam.employee.support.context.ApplicationContextService;
import cn.topiam.employee.support.repository.page.domain.Page;

/**
 * 应用授权策略 Converter
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/6/4 21:57
 */
@Mapper(componentModel = "spring")
public interface AppAccessPolicyConverter {

    /**
     * 应用授权策略分页列表转换为应用授权策略分页结果
     *
     * @param page {@link AppAccessPolicyEntity}
     * @return {@link AppAccessPolicyResult}
     */
    default Page<AppAccessPolicyResult> appPolicyEntityListConvertToAppPolicyResult(org.springframework.data.domain.Page<AppAccessPolicyPO> page) {
        cn.topiam.employee.support.repository.page.domain.Page<AppAccessPolicyResult> result = new cn.topiam.employee.support.repository.page.domain.Page<>();
        if (!CollectionUtils.isEmpty(page.getContent())) {
            List<AppAccessPolicyResult> list = new ArrayList<>();
            for (AppAccessPolicyPO po : page.getContent()) {
                AppAccessPolicyResult policy = entityConvertToAppPolicyResult(po);
                //图标未配置，所以先从模版中拿
                if (StringUtils.isBlank(policy.getAppIcon())) {
                    ApplicationService applicationService = getApplicationServiceLoader()
                        .getApplicationService(policy.getAppTemplate());
                    policy.setAppIcon(applicationService.getBase64Icon());
                }
                list.add(policy);
            }
            //@formatter:off
            result.setPagination(cn.topiam.employee.support.repository.page.domain.Page.Pagination.builder()
                    .total(page.getTotalElements())
                    .totalPages(page.getTotalPages())
                    .current(page.getPageable().getPageNumber() + 1)
                    .build());
            //@formatter:on
            result.setList(list);
        }
        return result;
    }

    /**
     * 应用授权策略实体转换为应用授权策略结果
     *
     * @param entity {@link AppAccessPolicyEntity}
     * @return {@link AppAccessPolicyEntity}
     */
    AppAccessPolicyResult entityConvertToAppPolicyResult(AppAccessPolicyPO entity);

    /**
     * 应用授权策略添加参数转换为应用授权策略实体
     *
     * @param param {@link  AppAccessPolicyCreateParam}
     * @return {@link AppAccessPolicyEntity}
     */
    default List<AppAccessPolicyEntity> appPolicyCreateParamConvertToEntity(AppAccessPolicyCreateParam param) {
        if (param == null) {
            return new ArrayList<>();
        }
        List<AppAccessPolicyEntity> list = new ArrayList<>();
        for (String subjectId : param.getSubjectIds()) {
            AppAccessPolicyEntity entity = new AppAccessPolicyEntity();
            entity.setAppId(param.getAppId());
            entity.setSubjectType(param.getSubjectType());
            entity.setEnabled(true);
            entity.setSubjectId(subjectId);
            list.add(entity);
        }
        return list;
    }

    /**
     * 获取 ApplicationServiceLoader
     *
     * @return {@link ApplicationServiceLoader}
     */
    private ApplicationServiceLoader getApplicationServiceLoader() {
        return ApplicationContextService.getBean(ApplicationServiceLoader.class);
    }

    /**
     * 转查询参数
     *
     * @param query {@link AppAccessPolicyQuery}
     * @return {@link AppAccessPolicyQueryParam}
     */
    AppAccessPolicyQueryParam appAccessPolicyQueryToQueryParam(AppAccessPolicyQuery query);

}
