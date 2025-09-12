/*
 * eiam-protocol-jwt - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.jwt.token;

import java.time.Duration;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/10 22:43
 */
@Data
@Builder
@RequiredArgsConstructor
public class IdTokenContext {

    @NonNull
    private String   issuer;

    @NonNull
    private String   subject;

    @NonNull
    private String   audience;

    @NonNull
    private String   sessionId;

    /**
     * Token 过期时间（秒）
     */
    @NonNull
    private Duration idTokenTimeToLive;

    @NonNull
    private String   privateKey;
}
