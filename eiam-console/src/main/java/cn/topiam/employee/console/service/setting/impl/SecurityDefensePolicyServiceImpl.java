/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.setting.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.topiam.employee.common.entity.setting.SettingEntity;
import cn.topiam.employee.common.repository.setting.SettingRepository;
import cn.topiam.employee.console.converter.setting.SecurityDefensePolicyConverter;
import cn.topiam.employee.console.pojo.result.setting.EmailProviderConfigResult;
import cn.topiam.employee.console.pojo.result.setting.SecurityDefensePolicyConfigResult;
import cn.topiam.employee.console.pojo.save.setting.SecurityDefensePolicyParam;
import cn.topiam.employee.console.service.setting.SecurityDefensePolicyService;
import cn.topiam.employee.support.context.ApplicationContextService;
import cn.topiam.employee.support.exception.TopIamException;
import cn.topiam.employee.support.security.util.ContentSecurityPolicyUtils;

import lombok.extern.slf4j.Slf4j;
import static cn.topiam.employee.common.constant.ConfigBeanNameConstants.DEFAULT_SECURITY_FILTER_CHAIN;
import static cn.topiam.employee.core.setting.SecuritySettingConstants.SECURITY_DEFENSE_POLICY_KEY;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2023-03-09
 */
@Slf4j
@Service
public class SecurityDefensePolicyServiceImpl extends SettingServiceImpl
                                              implements SecurityDefensePolicyService {

    /**
     * 创建品牌化
     *
     * @param param {@link SecurityDefensePolicyParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveSecurityDefensePolicyConfig(SecurityDefensePolicyParam param) {
        String contentSecurityPolicy = param.getContentSecurityPolicy();
        try {
            ContentSecurityPolicyUtils.parse(contentSecurityPolicy);
        } catch (ContentSecurityPolicyUtils.ContentSecurityPolicyPolicyException e) {
            ContentSecurityPolicyUtils.PolicyError error = e.getError();
            log.error("解析内容安全策略异常：{}", error.toString());
            throw new TopIamException("内容安全策略格式错误");
        }
        //删除安全防御策略
        SECURITY_DEFENSE_POLICY_KEY.forEach(settingRepository::deleteByName);
        //保存
        List<SettingEntity> list = securityDefensePolicyConverter
            .securityDefensePolicyParamToEntity(param);
        Boolean save = settingRepository.save(list);
        ApplicationContextService.refresh(DEFAULT_SECURITY_FILTER_CHAIN);
        return save;
    }

    /**
     * 获取安全防御策略配置
     *
     * @return {@link EmailProviderConfigResult}
     */
    @Override
    public SecurityDefensePolicyConfigResult getSecurityPolicyConfig() {
        //内容安全策略
        List<SettingEntity> list = settingRepository.findByNameIn(SECURITY_DEFENSE_POLICY_KEY);
        return securityDefensePolicyConverter.entityToSecurityDefensePolicyConfigResult(list);
    }

    /**
     * SecurityPolicyConverter
     */
    private final SecurityDefensePolicyConverter securityDefensePolicyConverter;
    private final SettingRepository              settingRepository;

    /**
     * BrandServiceImpl
     *
     * @param settingRepository {@link SettingRepository}
     * @param securityDefensePolicyConverter  {@link SecurityDefensePolicyConverter}
     */
    public SecurityDefensePolicyServiceImpl(SettingRepository settingRepository,
                                            SecurityDefensePolicyConverter securityDefensePolicyConverter) {
        super(settingRepository);
        this.securityDefensePolicyConverter = securityDefensePolicyConverter;
        this.settingRepository = settingRepository;
    }
}
