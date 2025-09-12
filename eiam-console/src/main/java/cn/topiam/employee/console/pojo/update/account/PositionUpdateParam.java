/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.update.account;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 编辑岗位入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/4/13 21:32
 */
@Data
@Schema(description = "修改岗位入参")
public class PositionUpdateParam implements Serializable {
    @Serial
    private static final long serialVersionUID = -6616249172773611157L;
    /**
     * ID
     */
    @Schema(description = "岗位ID")
    @NotBlank(message = "岗位ID不能为空")
    private String            id;
    /**
     * 岗位名称
     */
    @Schema(description = "岗位名称")
    @NotBlank(message = "岗位名称不能为空")
    private String            name;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String            remark;
}
