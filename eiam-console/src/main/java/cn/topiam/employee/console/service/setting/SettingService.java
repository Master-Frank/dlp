/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.setting;

import java.util.List;

import cn.topiam.employee.common.entity.setting.SettingEntity;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/9 22:30
 */
public interface SettingService {
    /**
     * 保存设置
     *
     * @param entity {@link SettingEntity}
     * @return {@link Boolean}
     */
    Boolean saveSetting(SettingEntity entity);

    /**
     * 根据KEY获取配置
     *
     * @param name {@link String}
     * @return {@link SettingEntity}
     */
    SettingEntity getSetting(String name);

    /**
     * 根据KEY模糊获取配置
     *
     * @param name {@link String}
     * @return {@link List<SettingEntity>}
     */
    List<SettingEntity> findByNameLike(String name);

    /**
     * 移除配置
     *
     * @param name {@link String}
     * @return {@link Boolean}
     */
    Boolean removeSetting(String name);
}
