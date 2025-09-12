/*
 * eiam-application-form - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.application.form;

import java.util.ArrayList;
import java.util.List;

import cn.topiam.employee.application.AbstractApplicationService;
import cn.topiam.employee.application.form.model.FormProtocolConfig;
import cn.topiam.employee.common.entity.app.AppFormConfigEntity;
import cn.topiam.employee.common.entity.app.po.AppFormConfigPO;
import cn.topiam.employee.common.repository.app.AppAccountRepository;
import cn.topiam.employee.common.repository.app.AppFormConfigRepository;
import cn.topiam.employee.common.repository.app.AppGroupAssociationRepository;
import cn.topiam.employee.common.repository.app.AppRepository;

/**
 * Form 应用配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/8/23 21:58
 */
public abstract class AbstractFormApplicationService extends AbstractApplicationService
                                                     implements FormApplicationService {

    @Override
    public void delete(String appId) {
        //删除应用
        appRepository.deleteById(appId);
        //删除应用账户
        appAccountRepository.deleteAllByAppId(appId);
        // 删除应用配置
        appFormConfigRepository.deleteByAppId(appId);
    }

    @Override
    public FormProtocolConfig getProtocolConfig(String appCode) {
        AppFormConfigPO configPo = appFormConfigRepository.findByAppCode(appCode);

        FormProtocolConfig.FormProtocolConfigBuilder<?, ?> configBuilder = FormProtocolConfig
            .builder();
        if (configPo.getAppId() != null) {
            configBuilder.appId(String.valueOf(configPo.getAppId()));
        }

        configBuilder.clientId(configPo.getClientId());
        configBuilder.clientSecret(configPo.getClientSecret());
        configBuilder.appCode(configPo.getAppCode());
        configBuilder.appName(configPo.getAppName());
        configBuilder.appTemplate(configPo.getAppTemplate());
        configBuilder.loginUrl(configPo.getLoginUrl());
        configBuilder.usernameField(configPo.getUsernameField());
        configBuilder.passwordField(configPo.getPasswordField());
        configBuilder.usernameEncryptType(configPo.getUsernameEncryptType());
        configBuilder.usernameEncryptKey(configPo.getUsernameEncryptKey());
        configBuilder.passwordEncryptType(configPo.getPasswordEncryptType());
        configBuilder.passwordEncryptKey(configPo.getPasswordEncryptKey());
        configBuilder.submitType(configPo.getSubmitType());
        List<AppFormConfigEntity.OtherField> list = configPo.getOtherField();
        if (list != null) {
            configBuilder.otherField(new ArrayList<>(list));
        }
        configBuilder.configured(configPo.getConfigured());
        return configBuilder.build();
    }

    /**
     * AppFormConfigRepository
     */
    protected final AppFormConfigRepository appFormConfigRepository;

    protected AbstractFormApplicationService(AppRepository appRepository,
                                             AppGroupAssociationRepository appGroupAssociationRepository,
                                             AppAccountRepository appAccountRepository,
                                             AppFormConfigRepository appFormConfigRepository) {
        super(appAccountRepository, appGroupAssociationRepository, appRepository);
        this.appFormConfigRepository = appFormConfigRepository;
    }
}
