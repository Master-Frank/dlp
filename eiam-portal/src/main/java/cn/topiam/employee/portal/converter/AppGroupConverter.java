/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.converter;

import java.util.List;

import org.mapstruct.Mapper;

import com.google.common.collect.Lists;

import cn.topiam.employee.common.entity.app.po.AppGroupPO;
import cn.topiam.employee.common.entity.app.query.AppGroupQueryParam;
import cn.topiam.employee.portal.pojo.query.GetAppGroupListQuery;
import cn.topiam.employee.portal.pojo.result.AppGroupListResult;

/**
 * 分组映射
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/31 15:45
 */
@Mapper(componentModel = "spring")
public interface AppGroupConverter {

    /**
     * 实体转换为分组列表结果
     *
     * @param appGroupPoList {@link List}
     * @return {@link List}
     */
    default List<AppGroupListResult> entityConvertToAppGroupListResult(List<AppGroupPO> appGroupPoList) {
        List<AppGroupListResult> list = Lists.newArrayList();
        for (AppGroupPO po : appGroupPoList) {
            AppGroupListResult result = entityConvertToAppGroupListResult(po);
            list.add(result);
        }
        return list;
    }

    /**
     * 实体转分组管理列表
     *
     * @param appGroupPo {@link AppGroupPO}
     * @return {@link AppGroupListResult}
     */
    AppGroupListResult entityConvertToAppGroupListResult(AppGroupPO appGroupPo);

    /**
     * 查询参数转换
     *
     * @param query {@link GetAppGroupListQuery}
     * @return {@link AppGroupQueryParam}
     */
    AppGroupQueryParam appGroupQueryToQueryParam(GetAppGroupListQuery query);
}
