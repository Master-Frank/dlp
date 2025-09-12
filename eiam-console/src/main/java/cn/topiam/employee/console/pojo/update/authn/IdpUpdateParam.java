/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.update.authn;

import java.io.Serial;
import java.io.Serializable;

import com.alibaba.fastjson2.JSONObject;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 认证源修改参数入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/21 21:21
 */
@Data
@Schema(description = "认证源修改参数")
public class IdpUpdateParam implements Serializable {
    @Serial
    private static final long serialVersionUID = -1440230086940289961L;
    /**
     * ID
     */
    @NotBlank(message = "ID不能为空")
    @Schema(description = "ID")
    private String            id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    @Schema(description = "名称")
    private String            name;

    /**
     * 平台
     */
    @NotNull(message = "平台不能为空")
    @Schema(description = "平台")
    private String            type;

    /**
     * 配置
     */
    @NotNull(message = "配置JSON不能为空")
    @Schema(description = "配置JSON")
    private JSONObject        config;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String            remark;

    /**
     * 是否显示
     */
    @Schema(description = "是否显示")
    private Boolean           displayed;
}
