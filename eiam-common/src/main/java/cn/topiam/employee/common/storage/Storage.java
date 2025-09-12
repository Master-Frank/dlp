/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.storage;

import java.io.InputStream;

/**
 * 存储
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/10 21:29
 */
public interface Storage {
    /**
     * 上传文件
     *
     * @param fileName {@link String}
     * @param inputStream {@link InputStream}
     * @return path
     * @throws Exception Exception
     */
    String upload(String fileName, InputStream inputStream) throws Exception;

    /**
     * 下载文件
     *
     * @param path {@link String}
     * @return path {@link String}
     * @throws Exception Exception
     */
    String download(String path) throws Exception;
}
