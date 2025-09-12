/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.pojo.request;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * AppAccountRequest 应用账户新增入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/25 22:13
 */
@Data
@Schema(description = "应用账户新增入参")
public class AppAccountRequest {

    /**
     * 应用ID
     */
    @Schema(description = "应用ID")
    @NotNull(message = "应用ID不能为空")
    private String  appId;

    /**
     * 账户名称
     */
    @Schema(description = "账户名称")
    @NotBlank(message = "账户名称不能为空")
    private String  account;

    /**
     * 账户密码
     */
    @Schema(description = "账户密码")
    private String  password;

    /**
     * 默认的
     */
    @Column(name = "default_")
    private Boolean defaulted;
}
