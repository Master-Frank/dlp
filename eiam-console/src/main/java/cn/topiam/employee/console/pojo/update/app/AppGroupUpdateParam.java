/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.update.app;

import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 分组修改入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/7/18 23:26
 */
@Data
@Schema(description = "分组修改入参")
public class AppGroupUpdateParam implements Serializable {

    /**
     * id
     */
    @Schema(description = "分组id")
    @NotNull(message = "ID不能为空")
    private String id;

    /**
     * 分组名称
     */
    @Schema(description = "分组名称")
    private String name;

    /**
     * 分组排序
     */
    @Schema(description = "分组编码")
    private String code;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
