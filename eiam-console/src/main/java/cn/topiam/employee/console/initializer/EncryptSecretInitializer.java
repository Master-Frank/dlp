/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.initializer;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.topiam.employee.common.entity.setting.SettingEntity;
import cn.topiam.employee.common.repository.setting.SettingRepository;
import cn.topiam.employee.support.config.AbstractSystemInitializer;
import cn.topiam.employee.support.config.InitializationException;
import cn.topiam.employee.support.util.AesUtils;
import static cn.topiam.employee.common.constant.SettingConstants.AES_SECRET;

/**
 * EncryptSecretInitializer
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2024/04/04 21:24
 */
@Component
public class EncryptSecretInitializer extends AbstractSystemInitializer {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void init() throws InitializationException {
        SettingEntity optional = settingRepository.findByName(AES_SECRET);
        if (Objects.isNull(optional)) {
            SettingEntity setting = new SettingEntity();
            setting.setName(AES_SECRET);
            setting.setValue(AesUtils.generateKey());
            setting.setDesc("Project aes secret");
            setting.setRemark(
                "This aes secret is automatically created during system initialization.");
            settingRepository.save(setting);
        }
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    /**
     * SettingRepository
     */
    private final SettingRepository settingRepository;

    /**
     *
     * @param settingRepository {@link SettingRepository}
     */
    public EncryptSecretInitializer(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }
}
