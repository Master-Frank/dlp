/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.storage.impl;

import java.io.InputStream;

import org.jetbrains.annotations.NotNull;

import cn.topiam.employee.common.storage.Storage;
import cn.topiam.employee.common.storage.StorageProviderException;

/**
 * 本地存储配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/10 21:32
 */
public class NoneStorage implements Storage {

    public NoneStorage() {
    }

    @Override
    public String upload(@NotNull String fileName,
                         InputStream inputStream) throws StorageProviderException {
        throw new StorageProviderException("暂未配置存储提供商");
    }

    @Override
    public String download(String path) throws StorageProviderException {
        throw new StorageProviderException("暂未配置存储提供商");
    }
}
