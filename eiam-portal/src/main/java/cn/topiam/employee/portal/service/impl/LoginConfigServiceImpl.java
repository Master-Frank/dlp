/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.topiam.employee.common.entity.authn.IdentityProviderEntity;
import cn.topiam.employee.common.repository.authentication.IdentityProviderRepository;
import cn.topiam.employee.portal.converter.LoginConfigConverter;
import cn.topiam.employee.portal.pojo.result.IdentityProviderResult;
import cn.topiam.employee.portal.pojo.result.LoginConfigResult;
import cn.topiam.employee.portal.service.LoginConfigService;

import lombok.AllArgsConstructor;

/**
 * LoginConfigService
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/25 21:50
 */
@Service
@AllArgsConstructor
public class LoginConfigServiceImpl implements LoginConfigService {

    /**
     * 获取登录配置
     *
     * @return {@link LoginConfigResult}
     */
    @Override
    public LoginConfigResult getLoginConfig() {
        LoginConfigResult.LoginConfigResultBuilder builder = LoginConfigResult.builder();
        //获取IDPS
        List<IdentityProviderEntity> list = identityProviderRepository
            .findByEnabledIsTrueAndDisplayedIsTrue();
        List<IdentityProviderResult> idps = loginConfigConverter
            .entityConverterToLoginConfigListResult(list);
        builder.idps(idps);
        return builder.build();
    }

    /**
     * AuthenticationConverter
     */
    private final LoginConfigConverter       loginConfigConverter;

    /**
     * AuthenticationSourceRepository
     */
    private final IdentityProviderRepository identityProviderRepository;
}
