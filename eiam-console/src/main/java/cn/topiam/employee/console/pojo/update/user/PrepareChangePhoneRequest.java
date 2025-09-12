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
 *准备更改手机号入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/8/8 21:15
 */
@Data
@Schema(description = "准备更改手机号入参")
public class PrepareChangePhoneRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 5681761697876754485L;

    /**
     * 手机号
     */
    @NotEmpty(message = "手机号不能为空")
    @Parameter(description = "手机号")
    private String            phone;

    /**
     * 手机号区域
     */
    @NotEmpty(message = "手机号区域不能为空")
    @Parameter(description = "手机号区域")
    private String            phoneRegion;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    @Parameter(description = "密码")
    private String            password;

}
