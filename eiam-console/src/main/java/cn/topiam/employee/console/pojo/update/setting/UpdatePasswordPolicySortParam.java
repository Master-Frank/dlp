/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.update.setting;

import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 *更新密码策略排序入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/11 22:03
 */
@Data
@Schema(description = "更新密码策略排序入参")
public class UpdatePasswordPolicySortParam implements Serializable {

    @Parameter(description = "ID")
    @NotNull(message = "ID不能为空")
    private String  id;

    @Parameter(description = "优先级")
    @NotNull(message = "优先级不能为空")
    private Integer order;

}
