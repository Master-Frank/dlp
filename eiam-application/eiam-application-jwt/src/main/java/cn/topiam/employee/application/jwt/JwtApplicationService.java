/*
 * eiam-application-jwt - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.application.jwt;

import cn.topiam.employee.application.ApplicationService;
import cn.topiam.employee.application.jwt.model.JwtProtocolConfig;

/**
 * 应用接口
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/8/20 23:20
 */
public interface JwtApplicationService extends ApplicationService {

    /**
     * 获取协议配置
     *
     * @param appCode {@link String}
     * @return {@link JwtProtocolConfig}
     */
    JwtProtocolConfig getProtocolConfig(String appCode);
}
