/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.save.authn;

import java.io.Serial;
import java.io.Serializable;

import com.alibaba.fastjson2.JSONObject;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 源创建参数入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/21 21:21
 */
@Data
@Schema(description = "认证源保存入参")
public class IdentityProviderCreateParam implements Serializable {
    @Serial
    private static final long serialVersionUID = -1440230086940289961L;

    /**
     * 认证源名称
     */
    @NotBlank(message = "认证源名称不能为空")
    @Schema(description = "认证源名称")
    private String            name;

    /**
     * 提供商
     */
    @NotNull(message = "提供商不能为空")
    @Schema(description = "提供商")
    private String            type;

    /**
     * 身份源类型
     */
    @NotNull(message = "身份源类型不能为空")
    @Schema(description = "身份源类型")
    private String            category;

    /**
     * 配置
     */
    @NotNull(message = "配置不能为空")
    @Schema(description = "配置JSON")
    private JSONObject        config;

    /**
     * 是否展示
     */
    @Schema(description = "是否展示")
    private Boolean           displayed;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String            remark;

}
