/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.entity.app.query;

import cn.topiam.employee.common.enums.app.AppPolicySubjectType;

import lombok.Data;

/**
 * 应用授权策略查询参数
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/9/27 21:29
 */
@Data
public class AppAccessPolicyQueryParam {

    /**
     * 应用id
     */
    private String               appId;

    /**
     * 授权主体
     */
    private String               subjectName;

    /**
     * 授权主体ID
     */
    private String               subjectId;

    /**
     * 主体类型（用户、分组、组织机构）
     */
    private AppPolicySubjectType subjectType;

    /**
     * 应用名称
     */
    private String               appName;
}
