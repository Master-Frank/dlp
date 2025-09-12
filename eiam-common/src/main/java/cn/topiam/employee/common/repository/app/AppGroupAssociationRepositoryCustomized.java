/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.app;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.topiam.employee.common.entity.app.AppEntity;
import cn.topiam.employee.common.entity.app.query.AppGroupAssociationListQueryParam;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2023/9/7 21:27
 */
public interface AppGroupAssociationRepositoryCustomized {

    /**
     * 获取应用组应用列表
     *
     * @param query    {@link AppGroupAssociationListQueryParam}
     * @param pageable {@link Pageable}
     * @return {@link Page}
     */
    Page<AppEntity> getAppGroupAssociationList(AppGroupAssociationListQueryParam query,
                                               Pageable pageable);
}
