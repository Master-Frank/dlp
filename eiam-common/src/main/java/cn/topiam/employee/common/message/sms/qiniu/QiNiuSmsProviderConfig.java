/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.message.sms.qiniu;

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
public class QiNiuSmsProviderConfig extends SmsProviderConfig {
    public QiNiuSmsProviderConfig() {
    }

    /**
     * accessKey
     */
    @NotEmpty(message = "accessKey不能为空")
    private String accessKey;
    /**
     * secretKey
     */
    @JsonPropertyEncrypt
    @NotEmpty(message = "secretKey不能为空")
    private String secretKey;
}
