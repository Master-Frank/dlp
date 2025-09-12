/*
 * eiam-protocol-form - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.form.client;

import java.time.Instant;

import cn.topiam.employee.protocol.code.RegisteredClient;

import lombok.Builder;
import lombok.Data;

/**
 * Form 已注册的客户端
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/12/17 20:10
 */
@Data
@Builder
public class FormRegisteredClient implements RegisteredClient {
    /**
     * 应用ID
     */
    private String  id;

    /**
     * 应用code
     */
    private String  code;

    /**
     * 客户端ID
     */
    private String  clientId;

    /**
     * 客户端名称
     */
    private String  clientName;

    /**
     * 客户端ID创建时间
     */
    private Instant clientIdIssuedAt;

    /**
     * 客户端秘钥
     */
    private String  clientSecret;

    /**
     * 客户端秘钥创建时间
     */
    private Instant clientSecretExpiresAt;

}
