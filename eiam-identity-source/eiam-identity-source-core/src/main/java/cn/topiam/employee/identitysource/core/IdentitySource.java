/*
 * eiam-identity-source-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.core;

import java.util.Map;

import cn.topiam.employee.common.enums.TriggerType;
import cn.topiam.employee.support.exception.TopIamException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 身份源Provider
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/28 22:35
 */
public interface IdentitySource<T extends IdentitySourceConfig> {
    /**
     * 获取身份源ID
     *
     * @return {@link String}
     */
    String getId();

    /**
     * 获取身份源名称
     *
     * @return {@link String}
     */
    String getName();

    /**
     * 获取身份源配置
     *
     * @return {@link String}
     */
    T getConfig();

    /**
     * 同步
     *
     * @param triggerType {@link TriggerType} 执行方式
     */
    void sync(TriggerType triggerType);

    /**
     * 回调
     *
     * @param request {@link HttpServletRequest}
     * @param body {@link String}
     * @return {@link Map}
     */
    default Object event(HttpServletRequest request, String body) {
        throw new TopIamException("暂未实现");
    }

}
