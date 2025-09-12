/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.pojo.request;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2022/4/3 22:22
 */
@Data
@AllArgsConstructor
@Schema(description = "账户绑定IDP入参")
public class AccountBindIdpRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -6222816278396139727L;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String            username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String            password;
}
