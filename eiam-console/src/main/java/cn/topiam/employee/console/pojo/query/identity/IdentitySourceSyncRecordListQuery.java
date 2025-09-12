/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.query.identity;

import java.io.Serial;
import java.io.Serializable;

import org.springdoc.core.annotations.ParameterObject;

import cn.topiam.employee.common.enums.SyncStatus;
import cn.topiam.employee.common.enums.identitysource.IdentitySourceActionType;
import cn.topiam.employee.common.enums.identitysource.IdentitySourceObjectType;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 查询身份源同步详情入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/14 01:29
 */
@Data
@Schema(description = "查询身份源同步详情入参")
@ParameterObject
public class IdentitySourceSyncRecordListQuery implements Serializable {
    @Serial
    private static final long        serialVersionUID = -7110595216804896858L;

    /**
     * 历史记录ID
     */
    @NotBlank(message = "历史记录ID不能为空")
    @Parameter(description = "历史记录ID")
    private String                   syncHistoryId;

    /**
     * 对象类型
     */
    @Parameter(description = "对象类型")
    private IdentitySourceObjectType objectType;

    /**
     * 操作类型
     */
    @Parameter(description = "操作类型")
    private IdentitySourceActionType actionType;

    /**
     * 触发类型
     */
    @Parameter(description = "状态")
    private SyncStatus               status;

}
