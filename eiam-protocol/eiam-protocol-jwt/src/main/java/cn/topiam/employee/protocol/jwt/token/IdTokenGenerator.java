/*
 * eiam-protocol-jwt - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.jwt.token;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/10 22:43
 */
@FunctionalInterface
public interface IdTokenGenerator {

    IdToken generate(IdTokenContext context);

}
