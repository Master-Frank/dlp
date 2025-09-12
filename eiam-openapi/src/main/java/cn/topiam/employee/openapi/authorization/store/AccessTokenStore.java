/*
 * eiam-openapi - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.openapi.authorization.store;

import java.io.Serializable;
import java.util.List;

import cn.topiam.employee.openapi.authorization.AccessToken;

/**
 * TokenStorage
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/5/8 21:42
 */
public interface AccessTokenStore extends Serializable {

    /**
     * save
     *
     * @param token {@link AccessToken}
     */
    void save(AccessToken token);

    /**
     * 根据token删除
     *
     * @param token {@link String}
     */
    void deleteByToken(String token);

    /**
     * 根据token查询
     *
     * @param token {@link String}
     * @return {@link AccessToken}
     */
    AccessToken findByToken(String token);

    /**
     * 根据 clientId 查询
     *
     * @param clientId {@link String}
     * @return {@link List}
     */
    AccessToken findByClientId(String clientId);
}
