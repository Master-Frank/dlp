/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.app;

import cn.topiam.employee.common.entity.app.po.AppFormConfigPO;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2022/12/13 22:58
 */
public interface AppFormConfigRepositoryCustomized {
    /**
     * 根据应用ID获取
     *
     * @param appId {@link Long}
     * @return {@link AppFormConfigPO}
     */
    AppFormConfigPO getByAppId(Long appId);

    /**
     * 根据应用 Client 获取
     *
     * @param clientId {@link String}
     * @return {@link AppFormConfigPO}
     */
    AppFormConfigPO getByClientId(String clientId);

    /**
     * 根据应用编码查询应用配置
     *
     * @param appCode {@link String}
     * @return {@link AppFormConfigPO}
     */
    AppFormConfigPO findByAppCode(String appCode);
}
