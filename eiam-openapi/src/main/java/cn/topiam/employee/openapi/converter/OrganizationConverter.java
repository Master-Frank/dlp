/*
 * eiam-openapi - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.openapi.converter;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.CollectionUtils;

import cn.topiam.employee.common.entity.account.OrganizationEntity;
import cn.topiam.employee.openapi.pojo.result.OrganizationChildResult;
import cn.topiam.employee.openapi.pojo.result.OrganizationResult;
import cn.topiam.employee.openapi.pojo.save.OrganizationCreateParam;
import cn.topiam.employee.openapi.pojo.update.OrganizationUpdateParam;

/**
 * 组织架构数据映射
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/14 21:25
 */
@Mapper(componentModel = "spring")
public interface OrganizationConverter {

    /**
     * 组织实体转换为List结果
     *
     * @param data {@link List}
     * @return {@link List}
     */
    default List<OrganizationChildResult> entityConvertToChildOrgListResult(List<OrganizationEntity> data) {
        List<OrganizationChildResult> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(data)) {
            for (OrganizationEntity entity : data) {
                list.add(entityConvertToChildOrgListResult(entity));
            }
        }
        return list;
    }

    /**
     * 组织实体转换为组织分页结果
     *
     * @param data {@link OrganizationEntity}
     * @return {@link OrganizationResult}
     */
    @Mapping(target = "type", source = "type.desc")
    OrganizationChildResult entityConvertToChildOrgListResult(OrganizationEntity data);

    /**
     * 组织创建参数转换为组织实体
     *
     * @param param {@link OrganizationCreateParam}
     * @return {@link OrganizationEntity}
     */
    @Mapping(target = "identitySourceId", ignore = true)
    @Mapping(target = "path", ignore = true)
    @Mapping(target = "displayPath", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataOrigin", expression = "java(cn.topiam.employee.support.security.userdetails.DataOrigin.INPUT.getType())")
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "leaf", expression = "java(Boolean.TRUE)")
    @Mapping(target = "enabled", expression = "java(Boolean.TRUE)")
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    @Mapping(target = "order", source = "order", defaultValue = "9999L")
    @Mapping(target = "parentId", source = "parentId", defaultExpression = "java(cn.topiam.employee.support.constant.EiamConstants.ROOT_NODE)")
    @Mapping(target = "externalId", source = "externalId", defaultExpression = "java(new org.springframework.util.JdkIdGenerator().generateId().toString())")
    OrganizationEntity orgCreateParamConvertToEntity(OrganizationCreateParam param);

    /**
     * 组织修改参数转换为组织实体
     *
     * @param param {@link OrganizationUpdateParam}
     * @return {@link OrganizationEntity}
     */
    @Mapping(target = "identitySourceId", ignore = true)
    @Mapping(target = "path", ignore = true)
    @Mapping(target = "displayPath", ignore = true)
    @Mapping(target = "dataOrigin", ignore = true)
    @Mapping(target = "parentId", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "leaf", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "remark", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    OrganizationEntity orgUpdateParamConvertToEntity(OrganizationUpdateParam param);

    /**
     * 实体转组织详情结果
     *
     * @param organization {@link OrganizationEntity}
     * @return {@link OrganizationResult}
     */
    OrganizationResult entityConvertToOrgDetailResult(OrganizationEntity organization);
}
