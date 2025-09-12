/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.entity.app.query;

import java.io.Serializable;

import cn.topiam.employee.common.enums.app.AppGroupType;

import lombok.Data;

/**
 * 查询分组列表入参
 *
 * @author TOPIAM
 * Created by support@topiam.cn on 2024/11/4 14:23
 */
@Data
public class AppGroupQueryParam implements Serializable {

    /**
     * 分组名称
     */
    private String       name;

    /**
     * 应用名称
     */
    private String       appName;

    /**
     * 分组编码
     */
    private String       code;

    /**
     * 分组类型
     */
    private AppGroupType type;

}
