/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.query.identity;

import java.io.Serial;
import java.io.Serializable;

import org.springdoc.core.annotations.ParameterObject;

import cn.topiam.employee.common.enums.SyncStatus;
import cn.topiam.employee.common.enums.TriggerType;
import cn.topiam.employee.common.enums.identitysource.IdentitySourceObjectType;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 查询身份源列表入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/14 01:29
 */
@Data
@Schema(description = "查询身份源同步历史列表入参")
@ParameterObject
public class IdentitySourceSyncHistoryListQuery implements Serializable {
    @Serial
    private static final long        serialVersionUID = -7110595216804896858L;

    /**
     * 身份源ID
     */
    @NotBlank(message = "身份源ID不能为空")
    @Parameter(description = "身份源ID")
    private String                   identitySourceId;

    /**
     * 对象类型
     */
    @Parameter(description = "对象类型")
    private IdentitySourceObjectType objectType;

    /**
     * 触发类型
     */
    @Parameter(description = "触发类型")
    private TriggerType              triggerType;

    /**
     * 状态
     */
    @Parameter(description = "触发类型")
    private SyncStatus               status;
}
