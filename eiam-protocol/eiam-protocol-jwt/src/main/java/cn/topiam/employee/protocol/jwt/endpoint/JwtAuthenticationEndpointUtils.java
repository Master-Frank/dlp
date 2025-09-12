/*
 * eiam-protocol-jwt - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.jwt.endpoint;

import cn.topiam.employee.protocol.jwt.exception.JwtAuthenticationException;
import cn.topiam.employee.protocol.jwt.exception.JwtError;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/9/4 13:05
 */
public class JwtAuthenticationEndpointUtils {

    public static void throwError(JwtError jwtError) {
        throw new JwtAuthenticationException(jwtError);
    }
}
