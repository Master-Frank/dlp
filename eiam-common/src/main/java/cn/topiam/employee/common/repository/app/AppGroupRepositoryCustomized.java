/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.app;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.topiam.employee.common.entity.app.po.AppGroupPO;
import cn.topiam.employee.common.entity.app.query.AppGroupAssociationListQueryParam;
import cn.topiam.employee.common.entity.app.query.AppGroupQueryParam;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2023/9/8 19:20
 */
public interface AppGroupRepositoryCustomized {

    /**
     * 获取应用组应用列表
     *
     * @param query    {@link AppGroupAssociationListQueryParam}
     * @param pageable {@link Pageable}
     * @return {@link Page}
     */
    Page<AppGroupPO> getAppGroupList(AppGroupQueryParam query, Pageable pageable);

    /**
     * 查询应用组列表
     *
     * @param subjectIds  {@link List}
     * @param query {@link AppGroupQueryParam}
     * @return {@link List}
     */
    List<AppGroupPO> getAppGroupList(List<String> subjectIds, AppGroupQueryParam query);

    /**
     * 根据当前用户和分组获取应用数量
     *
     * @param groupId {@link String}
     * @param subjectIds {@link List}
     * @return {@link Long}
     */
    Long getAppCount(List<String> subjectIds, String groupId);
}
