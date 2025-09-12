/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.entity.app.query;

import lombok.Data;

/**
 * 应用账户查询入参
 *
 * @author TOPIAM
 * Created by support@topiam.cn on 2024/11/4 14:26
 */
@Data
public class AppAccountQueryParam {

    /**
     * appId
     */
    private String appId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 账户名称
     */
    private String account;

    /**
     * 应用名称
     */
    private String appName;
}
