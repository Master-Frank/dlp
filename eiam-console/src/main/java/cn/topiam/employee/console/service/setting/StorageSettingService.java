/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.setting;

import cn.topiam.employee.console.pojo.result.setting.StorageProviderConfigResult;
import cn.topiam.employee.console.pojo.save.setting.StorageConfigSaveParam;

/**
 * 存储设置接口
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/10/1 21:19
 */
public interface StorageSettingService extends SettingService {
    /**
     * 更改存储启用禁用
     *
     * @return {@link Boolean}
     */
    Boolean disableStorage();

    /**
     * 保存存储配置
     *
     * @param param {@link StorageConfigSaveParam}
     * @return {@link Boolean}
     */
    Boolean saveStorageConfig(StorageConfigSaveParam param);

    /**
     * 获取存储配置
     *
     * @return {@link StorageProviderConfigResult}
     */
    StorageProviderConfigResult getStorageConfig();
}
