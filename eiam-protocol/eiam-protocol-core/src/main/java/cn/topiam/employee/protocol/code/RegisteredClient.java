/*
 * eiam-protocol-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.code;

import java.io.Serializable;
import java.time.Instant;

/**
 * 注册客户端
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/12/18 08:35
 */
public interface RegisteredClient extends Serializable {
    /**
     * 应用ID
     */
    String getId();

    /**
     * 应用code
     */
    String getCode();

    /**
     * 客户端ID
     */
    String getClientId();

    /**
     * 客户端名称
     */
    String getClientName();

    /**
     * 客户端ID创建时间
     */
    Instant getClientIdIssuedAt();

    /**
     * 客户端秘钥
     */
    String getClientSecret();

    /**
     * 客户端秘钥创建时间
     */
    Instant getClientSecretExpiresAt();
}
