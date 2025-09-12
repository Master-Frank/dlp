/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import cn.topiam.employee.common.entity.app.AppAccountEntity;
import cn.topiam.employee.portal.pojo.request.AppAccountRequest;

/**
 * 应用账户映射
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/25 21:08
 */
@Mapper(componentModel = "spring")
public interface AppAccountConverter {

    /**
     * 应用账户新增参数转换应用账户实体
     *
     * @param param {@link AppAccountRequest}
     * @return {@link AppAccountEntity}
     */
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    AppAccountEntity appAccountRequestConvertToEntity(AppAccountRequest param);

}
