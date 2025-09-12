/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.app;

import cn.topiam.employee.common.enums.app.AppCertUsingType;

import lombok.AllArgsConstructor;
import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 获取应用证书列表结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/31 23:29
 */
@Data
@AllArgsConstructor
@Schema(description = "获取应用证书列表响应")
public class AppCertListResult {
    /**
     * ID
     */
    @Parameter(description = "证书ID")
    private String           id;

    /**
     * 应用ID
     */
    @Parameter(description = "应用ID")
    private String           appId;

    /**
     * 签名算法
     */
    @Parameter(description = "签名算法")
    private String           signAlgo;

    /**
     * 私钥长度
     */
    @Parameter(description = "私钥长度")
    private Integer          keyLong;

    /**
     * 证书
     */
    @Parameter(description = "证书")
    private String           cert;

    /**
     * 使用类型
     */
    @Parameter(description = "使用类型")
    private AppCertUsingType usingType;
}
