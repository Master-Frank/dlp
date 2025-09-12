/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.save.app;

import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 分组保存入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/31 23:26
 */
@Data
@Schema(description = "应用分组保存入参")
public class AppGroupCreateParam implements Serializable {

    /**
     * 分组名称
     */
    @NotBlank(message = "分组名称不能为空")
    @Schema(description = "分组名称")
    private String name;

    /**
     * 分组编码
     */
    @NotBlank(message = "分组编码不能为空")
    @Schema(description = "分组编码")
    private String code;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
