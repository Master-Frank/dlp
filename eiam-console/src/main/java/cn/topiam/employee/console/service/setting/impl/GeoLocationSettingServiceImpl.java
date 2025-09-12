/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.setting.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.topiam.employee.common.entity.setting.SettingEntity;
import cn.topiam.employee.common.repository.setting.SettingRepository;
import cn.topiam.employee.console.converter.setting.GeoLocationSettingConverter;
import cn.topiam.employee.console.pojo.result.setting.GeoIpProviderResult;
import cn.topiam.employee.console.pojo.save.setting.GeoIpProviderSaveParam;
import cn.topiam.employee.console.service.setting.GeoLocationSettingService;
import cn.topiam.employee.support.context.ApplicationContextService;
import cn.topiam.employee.support.geo.GeoLocation;
import cn.topiam.employee.support.geo.GeoLocationParser;

import lombok.extern.slf4j.Slf4j;
import static cn.topiam.employee.common.constant.ConfigBeanNameConstants.GEO_LOCATION;
import static cn.topiam.employee.core.setting.GeoIpProviderConstants.IPADDRESS_SETTING_NAME;

/**
 * ip设置接口
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/1 21:43
 */
@Slf4j
@Service
public class GeoLocationSettingServiceImpl extends SettingServiceImpl
                                           implements GeoLocationSettingService {

    /**
     * 保存配置
     *
     * @param param {@link GeoIpProviderSaveParam}
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveGeoIpLibrary(GeoIpProviderSaveParam param) {
        SettingEntity settingEntity = geoLocationSettingsConverter
            .geoLocationProviderConfigToEntity(param);
        Boolean success = saveSetting(settingEntity);
        ApplicationContextService.refresh(GEO_LOCATION);
        return success;
    }

    /**
     * 获取配置
     *
     * @return {@link SettingEntity}
     */
    @Override
    public GeoIpProviderResult getGeoIpLibrary() {
        return geoLocationSettingsConverter
            .entityToGeoLocationProviderConfig(getSetting(IPADDRESS_SETTING_NAME));
    }

    /**
     * 查询ip详细信息
     *
     * @param ip {@link String}
     * @return {@link GeoLocation}
     */
    @Override
    public GeoLocation getGeoLocation(String ip) {
        return geoLocationParser.getGeoLocation(ip);
    }

    private final GeoLocationParser           geoLocationParser;
    private final GeoLocationSettingConverter geoLocationSettingsConverter;

    public GeoLocationSettingServiceImpl(SettingRepository settingsRepository,
                                         GeoLocationParser geoLocationParser,
                                         GeoLocationSettingConverter geoLocationSettingsConverter) {
        super(settingsRepository);
        this.geoLocationParser = geoLocationParser;
        this.geoLocationSettingsConverter = geoLocationSettingsConverter;
    }
}
