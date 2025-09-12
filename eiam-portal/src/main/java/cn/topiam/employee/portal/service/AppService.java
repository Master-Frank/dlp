/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.service;

import java.util.List;

import cn.topiam.employee.portal.pojo.query.GetAppGroupListQuery;
import cn.topiam.employee.portal.pojo.result.AppGroupListResult;
import cn.topiam.employee.portal.pojo.result.GetAppListResult;
import cn.topiam.employee.support.repository.page.domain.Page;
import cn.topiam.employee.support.repository.page.domain.PageModel;

/**
 * AppService
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/7/6 22:35
 */
public interface AppService {
    /**
     * 获取应用列表
     *
     * @param query     {@link cn.topiam.employee.portal.pojo.query.GetAppListQuery}
     * @param pageModel {@link PageModel}
     * @return {@link Page}
     */
    Page<GetAppListResult> getAppList(cn.topiam.employee.portal.pojo.query.GetAppListQuery query,
                                      PageModel pageModel);

    /**
     * 查询应用分组
     *
     * @param appGroupQuery {@link GetAppGroupListQuery}
     * @return {@link AppGroupListResult}
     */
    List<AppGroupListResult> getAppGroupList(GetAppGroupListQuery appGroupQuery);

    /**
     * 获取应用数量
     *
     * @param groupId {@link String}
     * @return {@link Long}
     */
    Long getAppCount(String groupId);

    /**
     * 获取所有应用
     *
     * @return {@link Long}
     */
    Long getAppCount();
}
