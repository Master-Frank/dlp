/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.setting.impl;

import java.util.List;
import java.util.concurrent.Executor;

import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.topiam.employee.common.entity.setting.SettingEntity;
import cn.topiam.employee.common.repository.setting.SettingRepository;
import cn.topiam.employee.console.converter.setting.SecuritySettingConverter;
import cn.topiam.employee.console.pojo.result.setting.SecurityBasicConfigResult;
import cn.topiam.employee.console.pojo.save.setting.SecurityBasicSaveParam;
import cn.topiam.employee.console.service.setting.SecuritySettingService;
import cn.topiam.employee.core.security.session.Session;
import cn.topiam.employee.support.context.ApplicationContextService;
import cn.topiam.employee.support.context.ServletContextService;
import static cn.topiam.employee.common.constant.ConfigBeanNameConstants.DEFAULT_SECURITY_FILTER_CHAIN;
import static cn.topiam.employee.core.setting.SecuritySettingConstants.SECURITY_BASIC_KEY;

/**
 * <p>
 * 安全设置表 服务实现类
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-10-01
 */
@Service
public class SecuritySettingServiceImpl extends SettingServiceImpl
                                        implements SecuritySettingService {

    /**
     * 获取基础配置
     *
     * @return {@link SecurityBasicConfigResult}
     */
    @Override
    public SecurityBasicConfigResult getBasicConfig() {
        //查询数据库配置
        List<SettingEntity> list = settingRepository.findByNameIn(SECURITY_BASIC_KEY);
        return securitySettingConverter.entityConvertToSecurityBasicConfigResult(list);
    }

    /**
     * 保存基础配置
     *
     * @param param {@link SecurityBasicSaveParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveBasicConfig(SecurityBasicSaveParam param) {

        //删除密码配置
        SECURITY_BASIC_KEY.forEach(settingRepository::deleteByName);
        //保存
        List<SettingEntity> list = securitySettingConverter
            .securityBasicSaveParamConvertToEntity(param);
        Boolean save = settingRepository.save(list);
        String currentSessionId = ServletContextService.getSession().getId();
        //异步下线所有用户（排除当前操作用户）
        executor.execute(() -> {
            List<Object> principals = sessionRegistry.getAllPrincipals();
            principals.forEach(i -> {
                if (i instanceof Session) {
                    if (!((Session) i).getSessionId().equals(currentSessionId)) {
                        sessionRegistry.getSessionInformation(((Session) i).getSessionId())
                            .expireNow();
                    }
                }
            });
        });
        // refresh
        ApplicationContextService.refresh(DEFAULT_SECURITY_FILTER_CHAIN);
        return save;
    }

    /**
     * SecurityBasicDataConverter
     */
    private final SecuritySettingConverter securitySettingConverter;

    private final SettingRepository        settingRepository;

    private final SessionRegistry          sessionRegistry;

    private final Executor                 executor;

    public SecuritySettingServiceImpl(SettingRepository settingsRepository,
                                      SecuritySettingConverter securitySettingConverter,
                                      SettingRepository settingRepository,
                                      SessionRegistry sessionRegistry,
                                      AsyncConfigurer asyncConfigurer) {
        super(settingsRepository);
        this.securitySettingConverter = securitySettingConverter;
        this.settingRepository = settingRepository;
        this.sessionRegistry = sessionRegistry;
        this.executor = asyncConfigurer.getAsyncExecutor();
    }
}
