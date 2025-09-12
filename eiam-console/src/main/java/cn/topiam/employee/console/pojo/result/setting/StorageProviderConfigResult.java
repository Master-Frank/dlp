/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.setting;

import java.io.Serial;
import java.io.Serializable;

import cn.topiam.employee.common.storage.StorageConfig;
import cn.topiam.employee.common.storage.enums.StorageProvider;

import lombok.Builder;
import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 存储配置查询结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/1 23:40
 */
@Data
@Builder
@Schema(description = "存储配置查询响应")
public class StorageProviderConfigResult implements Serializable {

    @Serial
    private static final long    serialVersionUID = -2667374916357438335L;
    /**
     * 服务商
     */
    @Parameter(description = "服务商")
    private StorageProvider      provider;
    /**
     * 启用
     */
    @Parameter(description = "是否启用")
    private Boolean              enabled;
    /**
     * 配置信息
     */
    @Parameter(description = "配置信息")
    private StorageConfig.Config config;

}
