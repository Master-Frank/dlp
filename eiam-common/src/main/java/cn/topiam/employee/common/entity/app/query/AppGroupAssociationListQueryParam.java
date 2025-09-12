/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.entity.app.query;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

/**
 * 查询应用列表入参
 *
 * @author TOPIAM
 * Created by support@topiam.cn on 2024/11/4 14:24
 */
@Data
public class AppGroupAssociationListQueryParam implements Serializable {
    @Serial
    private static final long serialVersionUID = -7110595216804896858L;
    /**
     * 组ID
     */
    private String            id;

    /**
     * 应用名称
     */
    private String            appName;
}
