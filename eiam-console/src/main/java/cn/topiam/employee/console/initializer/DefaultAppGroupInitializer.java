/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.initializer;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.topiam.employee.common.entity.app.AppGroupEntity;
import cn.topiam.employee.common.enums.app.AppDefaultGroup;
import cn.topiam.employee.common.enums.app.AppGroupType;
import cn.topiam.employee.common.repository.app.AppGroupRepository;
import cn.topiam.employee.support.config.AbstractSystemInitializer;
import cn.topiam.employee.support.config.InitializationException;

/**
 * DefaultAppGroupInitialize
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/9/11 21:44
 */
@Order(2)
@Component
public class DefaultAppGroupInitializer extends AbstractSystemInitializer {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void init() throws InitializationException {
        //@formatter:off
        Arrays.stream(AppDefaultGroup.values()).toList().forEach(i -> {
            Optional<AppGroupEntity> optional = appGroupRepository.findByCode(i.getCode());
            if (optional.isEmpty()) {
                AppGroupEntity appGroup = new AppGroupEntity();
                appGroup.setCode(i.getCode());
                appGroup.setName(i.getDesc());
                appGroup.setType(AppGroupType.DEFAULT);
                appGroup.setRemark(
                        "This app group is automatically created during system initialization.");
                appGroupRepository.save(appGroup);
            }
        });
    }
    @Override
    public int getOrder() {
        return 4;
    }

    private final AppGroupRepository        appGroupRepository;


    public DefaultAppGroupInitializer(AppGroupRepository appGroupRepository) {
        this.appGroupRepository = appGroupRepository;
    }
}
