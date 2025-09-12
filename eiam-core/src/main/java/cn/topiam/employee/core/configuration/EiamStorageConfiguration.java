/*
 * eiam-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.core.configuration;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.topiam.employee.common.entity.setting.SettingEntity;
import cn.topiam.employee.common.jackjson.encrypt.EncryptionModule;
import cn.topiam.employee.common.repository.setting.SettingRepository;
import cn.topiam.employee.common.storage.Storage;
import cn.topiam.employee.common.storage.StorageConfig;
import cn.topiam.employee.common.storage.StorageFactory;
import cn.topiam.employee.common.storage.impl.NoneStorage;
import cn.topiam.employee.core.setting.StorageProviderSettingConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * 存储配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/10 21:01
 */
@Slf4j
@Configuration
public class EiamStorageConfiguration {

    /**
     * 存储配置
     *
     * @return {@link Storage}
     */
    @Bean(name = StorageProviderSettingConstants.STORAGE_BEAN_NAME)
    @RefreshScope
    public Storage storage() {
        SettingEntity setting = repository
            .findByName(StorageProviderSettingConstants.STORAGE_PROVIDER_KEY);
        ObjectMapper objectMapper = EncryptionModule.deserializerDecrypt();
        // 指定序列化输入的类型
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
            ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        try {
            if (!Objects.isNull(setting) && StringUtils.isNotEmpty(setting.getValue())) {
                return StorageFactory
                    .getStorage(objectMapper.readValue(setting.getValue(), StorageConfig.class));
            }
        } catch (Exception e) {
            log.error("Create storage Exception: {}", e.getMessage(), e);
        }
        return new NoneStorage();
    }

    private final SettingRepository repository;

    public EiamStorageConfiguration(SettingRepository settingRepository) {
        this.repository = settingRepository;
    }

}
