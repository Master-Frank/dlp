/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.app;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.topiam.employee.common.entity.app.AppEntity;
import cn.topiam.employee.common.entity.app.po.AppPO;
import cn.topiam.employee.common.entity.app.query.GetAppListQueryParam;

/**
 * 应用 Repository Customized
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/26 23:40
 */
public interface AppRepositoryCustomized {
    /**
     * 根据主体ID获取应用列表
     *
     * @param subjectIds {@link  List}
     * @return {@link Page}
     */
    List<AppPO> getAppList(List<String> subjectIds);

    /**
     * 根据主体ID，查询参数、分页条件获取应用列表
     *
     * @param subjectIds {@link  List}
     * @param query {@link GetAppListQueryParam}
     * @param pageable    {@link  Pageable}
     * @return {@link Page}
     */
    Page<AppEntity> getAppList(List<String> subjectIds, GetAppListQueryParam query,
                               Pageable pageable);

    /**
     * 获取用户应用数量
     *
     * @param subjectIds {@link List}
     * @return {@link Long}
     */
    Long getAppCount(List<String> subjectIds);
}
