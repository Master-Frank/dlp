/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.message.sms.tencent;

import cn.topiam.employee.common.jackjson.encrypt.JsonPropertyEncrypt;
import cn.topiam.employee.common.message.sms.SmsProviderConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotEmpty;

/**
 * 验证码提供商配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/10/1 21:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TencentSmsProviderConfig extends SmsProviderConfig {
    public TencentSmsProviderConfig() {
    }

    /**
     * secretId
     */
    @NotEmpty(message = "SecretId不能为空")
    private String secretId;

    /**
     * secretKey
     */
    @JsonPropertyEncrypt
    @NotEmpty(message = "SecretKey不能为空")
    private String secretKey;

    /**
     * 短信应用ID
     */
    @NotEmpty(message = "短信应用ID不能为空")
    private String sdkAppId;

    /**
     * 短信签名内容
     */
    @NotEmpty(message = "短信签名内容不能为空")
    private String signName;

    /**
     * Region
     */
    @NotEmpty(message = "Region不能为空")
    private String region;
}
