/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.setting;

import cn.topiam.employee.console.pojo.result.setting.GeoIpProviderResult;
import cn.topiam.employee.console.pojo.save.setting.GeoIpProviderSaveParam;
import cn.topiam.employee.support.geo.GeoLocation;

/**
 * ip设置接口
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/10/1 21:19
 */
public interface GeoLocationSettingService extends SettingService {

    /**
     * 保存配置
     *
     * @param param {@link GeoIpProviderSaveParam}
     * @return {@link Boolean}
     */
    Boolean saveGeoIpLibrary(GeoIpProviderSaveParam param);

    /**
     * 获取配置
     *
     * @return {@link GeoIpProviderResult}
     */
    GeoIpProviderResult getGeoIpLibrary();

    /**
     * 查询ip详细信息
     *
     * @param ip {@link  String}
     * @return {@link GeoLocation}
     */
    GeoLocation getGeoLocation(String ip);
}
