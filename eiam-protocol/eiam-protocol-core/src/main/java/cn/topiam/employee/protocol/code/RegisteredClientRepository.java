/*
 * eiam-protocol-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.code;

import org.springframework.lang.Nullable;

/**
 * 客户端存储库
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/12/17 20:11
 */
public interface RegisteredClientRepository<T extends RegisteredClient> {
    /**
     * save 客户端
     *
     * @param client {@link T}
     */
    void save(T client);

    /**
     * 根据ID查询已注册客户端
     *
     * @param id {@link String}
     * @return {@link T}
     */
    @Nullable
    T findById(String id);

    /**
     * 根据客户端ID查询已注册客户端
     *
     * @param clientId {@link String}
     * @return {@link T}
     */
    @Nullable
    T findByClientId(String clientId);

}
