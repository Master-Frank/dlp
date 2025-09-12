/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.storage;

import java.lang.reflect.Constructor;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/10 21:40
 */
public class StorageFactory {

    private StorageFactory() {
    }

    /**
     * 获取实例化
     *
     * @param config {@link StorageConfig}
     * @return {@link Storage}
     */
    public static Storage getStorage(StorageConfig config) {
        try {
            Constructor<? extends Storage> constructor = config.getProvider().getStorage()
                .getDeclaredConstructor(StorageConfig.class);
            return constructor.newInstance(config);
        } catch (Exception e) {
            throw new StorageProviderException(e.getMessage(), e);
        }
    }
}
