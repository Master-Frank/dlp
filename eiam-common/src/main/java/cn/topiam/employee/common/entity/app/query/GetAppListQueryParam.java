/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.entity.app.query;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

/**
 * 查询应用列表
 *
 * @author TOPIAM
 * Created by support@topiam.cn on 2024/11/4 14:22
 */
@Data
public class GetAppListQueryParam implements Serializable {

    @Serial
    private static final long serialVersionUID = -4981513177967939516L;

    /**
     * 应用名称
     */
    private String            name;

    /**
     * 应用分组ID
     */
    private String            groupId;

}
