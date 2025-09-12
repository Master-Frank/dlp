/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.identitysource;

import java.io.Serializable;

import cn.topiam.employee.common.enums.identitysource.IdentitySourceActionType;
import cn.topiam.employee.common.enums.identitysource.IdentitySourceObjectType;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 身份源同步详情列表
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/25 23:05
 */
@Data
@Schema(description = "身份源同步记录列表")
public class IdentitySourceSyncRecordListResult implements Serializable {
    /**
     * ID
     */
    @Parameter(description = "ID")
    private String                   id;

    /**
     * 动作类型
     */
    @Parameter(description = "动作类型")
    private IdentitySourceActionType actionType;

    /**
     * 对象ID
     */
    @Parameter(description = "对象ID")
    private String                   objectId;

    /**
     * 对象名称
     */
    @Parameter(description = "对象名称")
    private String                   objectName;

    /**
     * 对象类型
     */
    @Parameter(description = "对象类型")
    private IdentitySourceObjectType objectType;

    /**
     * 状态
     */
    @Parameter(description = "状态")
    private String                   status;

    /**
     * 描述
     */
    @Parameter(description = "描述")
    private String                   desc;
}
