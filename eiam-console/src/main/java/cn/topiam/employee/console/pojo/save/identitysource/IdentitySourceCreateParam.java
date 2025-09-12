/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.save.identitysource;

import java.io.Serial;
import java.io.Serializable;

import cn.topiam.employee.common.enums.identitysource.IdentitySourceProvider;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * 身份源保存入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/1 01:37
 */
@Data
@Schema(description = "身份源保存入参")
public class IdentitySourceCreateParam implements Serializable {

    @Serial
    private static final long      serialVersionUID = 4217715726184249167L;
    /**
     * 名称
     */
    @Parameter(description = "名称")
    @NotEmpty(message = "名称不能为空")
    private String                 name;

    /**
     * 身份源提供商
     */
    @Parameter(description = "身份源提供商")
    @NotNull(message = "身份源提供商不能为空")
    private IdentitySourceProvider provider;

    /**
     * 备注
     */
    @Parameter(description = "备注")
    private String                 remark;

}
