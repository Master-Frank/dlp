/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.save.account;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 用户创建请求入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 23:16
 */
@Data
@Schema(description = "创建用户分组入参")
public class UserGroupCreateParam implements Serializable {
    @Serial
    private static final long serialVersionUID = -6044649488381303849L;
    /**
     * 用户组名称
     */
    @Schema(description = "用户组名称")
    @NotBlank(message = "用户组名称不能为空")
    private String            name;

    /**
     * 用户组编码
     */
    @Schema(description = "用户组编码")
    @NotBlank(message = "用户组编码不能为空")
    private String            code;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String            remark;
}
