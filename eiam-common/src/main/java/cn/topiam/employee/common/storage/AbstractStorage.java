/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.storage;

import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/10 21:36
 */
public class AbstractStorage implements Storage {
    protected StorageConfig    config;
    public static final String SEPARATOR      = "/";
    public static final String JOINER         = "-";
    public static final int    EXPIRY_SECONDS = 3600;
    /**
     * url 正则
     */
    public static final String URL_REGEXP     = "^https?://[\\w.-]+(:\\d+)?$";

    public AbstractStorage(StorageConfig config) {
        this.config = config;
    }

    /**
     * 判断域名是否为https
     *
     * @param url {@link String}
     * @return {@link Boolean}
     */
    public boolean getUrlSecure(String url) {
        return "https:".equals(url.split("//")[0]);
    }

    public String getFileName(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            fileName = "";
        }
        return UUID.randomUUID().toString().replace(JOINER, "").toLowerCase() + JOINER + fileName;
    }

    @Override
    public String upload(@NotNull String fileName,
                         InputStream inputStream) throws StorageProviderException {
        return null;
    }

    @Override
    public String download(String path) throws StorageProviderException {
        return null;
    }
}
