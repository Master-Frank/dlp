/*
 * eiam-application-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.application.context;

import java.util.Map;

/**
 * 应用上下文
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/30 23:52
 */
public interface ApplicationContext {
    /**
     * 获取应用ID
     *
     * @return {@link String}
     */
    String getAppId();

    /**
     * 获取客户端ID
     *
     * @return {@link String}
     */
    String getClientId();

    /**
     * 获取应用编码
     *
     * @return {@link String}
     */
    String getAppCode();

    /**
     * 获取应用模版
     *
     * @return {@link String}
     */
    String getAppTemplate();

    /**
     * 获取协议配置
     *
     * @return {@link Map}
     */
    Map<String, Object> getConfig();
}
