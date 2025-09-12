/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.update.user;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

/**
 * 更改密码入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/8/8 21:15
 */
@Data
@Schema(description = "更改密码入参")
public class ChangePasswordRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 5681761697876754485L;

    /**
     * 新密码
     */
    @NotEmpty(message = "新密码不能为空")
    @Parameter(description = "新密码")
    private String            newPassword;

    /**
     * 验证码
     */
    @NotEmpty(message = "旧密码不能为空")
    @Parameter(description = "旧密码")
    private String            oldPassword;
}
