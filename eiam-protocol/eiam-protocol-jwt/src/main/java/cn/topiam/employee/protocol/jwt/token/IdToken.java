/*
 * eiam-protocol-jwt - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.jwt.token;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/10 23:24
 */
@Data
@Builder
@Jacksonized
public class IdToken {

    @NonNull
    private final String  tokenValue;
    @NonNull
    private final Instant issuedAt;
    @NonNull
    private final Instant expiresAt;
}
