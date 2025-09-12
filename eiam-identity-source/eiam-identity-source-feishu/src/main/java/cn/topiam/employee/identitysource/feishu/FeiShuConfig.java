/*
 * eiam-identity-source-feishu - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.feishu;

import java.io.Serial;

import cn.topiam.employee.common.jackjson.encrypt.JsonPropertyEncrypt;
import cn.topiam.employee.identitysource.core.IdentitySourceConfig;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;

/**
 * 飞书配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/28 23:17
 */
@Getter
@Setter
public class FeiShuConfig extends IdentitySourceConfig {

    @Serial
    private static final long serialVersionUID = -834514351515253275L;
    /**
     * 应用App key
     */
    @NotEmpty(message = "AppId不能为空")
    private String            appId;

    /**
     * 应用AppSecret
     */
    @JsonPropertyEncrypt
    @NotEmpty(message = "AppSecret不能为空")
    private String            appSecret;

    /**
     * encryptKey
     */
    @JsonPropertyEncrypt
    private String            encryptKey;

    /**
     * verificationToken
     */
    @JsonPropertyEncrypt
    private String            verificationToken;

    public FeiShuConfig() {
    }
}
