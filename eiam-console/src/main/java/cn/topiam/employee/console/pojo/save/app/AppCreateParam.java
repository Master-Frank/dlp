/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.save.app;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 应用保存入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/7/18 23:26
 */
@Data
@Schema(description = "应用保存入参")
public class AppCreateParam implements Serializable {

    /**
     * 应用名称
     */
    @NotBlank(message = "应用名称不能为空")
    @Schema(description = "应用名称")
    private String       name;

    /**
     * 应用模版
     */
    @NotNull(message = "应用模版不能为空")
    @Schema(description = "应用模版")
    private String       template;

    /**
     * 应用图标
     */
    @Schema(description = "应用图标")
    private String       icon;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String       remark;

    /**
     * 应用分组id
     */
    @Schema(description = "应用分组")
    private List<String> groupIds;
}
