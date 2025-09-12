/*
 * eiam-identity-source-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.core.event;

/**
 * 身份源监听
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/9/20 22:24
 */
public interface IdentitySourceEventListener {
    /**
     * 注册
     * @param id {@link String}
     */
    void register(String id);

    /**
     * 注册
     * @param id {@link String}
     */
    void destroy(String id);

    /**
     * 同步
     *
     * @param id {@link String}
     */
    void sync(String id);
}
