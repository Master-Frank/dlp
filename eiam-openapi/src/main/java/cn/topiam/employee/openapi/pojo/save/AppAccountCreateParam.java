/*
 * eiam-openapi - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.openapi.pojo.save;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * AppAccountCreateParam 应用账户新增入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/24 22:13
 */
@Data
@Schema(description = "应用账户新增入参")
public class AppAccountCreateParam {

    /**
     * 应用ID
     */
    @Schema(description = "应用ID")
    @NotNull(message = "应用ID不能为空")
    private String  appId;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private String  userId;

    /**
     * 账户名称
     */
    @Schema(description = "账户名称")
    @NotBlank(message = "账户名称不能为空")
    private String  account;

    /**
     * 是否默认
     */
    @Schema(description = "是否默认")
    private Boolean defaulted;
}
