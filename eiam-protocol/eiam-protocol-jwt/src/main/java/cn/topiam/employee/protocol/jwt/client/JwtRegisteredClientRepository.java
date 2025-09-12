/*
 * eiam-protocol-jwt - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.jwt.client;

import cn.topiam.employee.protocol.code.RegisteredClientRepository;

/**
 * 客户端存储库
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/12/17 20:11
 */
public interface JwtRegisteredClientRepository extends
                                               RegisteredClientRepository<JwtRegisteredClient> {

}
