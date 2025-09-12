/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.converter;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import cn.topiam.employee.authentication.common.IdentityProviderType;
import cn.topiam.employee.common.entity.authn.IdentityProviderEntity;
import cn.topiam.employee.portal.pojo.result.IdentityProviderResult;

/**
 * AuthenticationConverter
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/25 21:52
 */
@Mapper(componentModel = "spring")
public interface LoginConfigConverter {

    /**
     * 实体转身份提供商列表
     *
     * @param list {@link List}
     * @return {@link List}
     */
    default List<IdentityProviderResult> entityConverterToLoginConfigListResult(List<IdentityProviderEntity> list) {
        List<IdentityProviderResult> result = new ArrayList<>();
        for (IdentityProviderEntity entity : list) {
            IdentityProviderResult idp = new IdentityProviderResult();
            idp.setCode(entity.getCode());
            idp.setName(entity.getName());
            idp.setType(entity.getType());
            idp.setCategory(entity.getCategory());
            idp.setAuthorizationUri(IdentityProviderType.getIdentityProviderType(entity.getType())
                .getAuthorizationPathPrefix() + "/" + entity.getCode());
            result.add(idp);
        }
        return result;
    }
}
