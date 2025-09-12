/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.pojo.request;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

/**
 * 更改手机号入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/8/8 21:15
 */
@Data
@Schema(description = "更改手机号入参")
public class ChangePhoneRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 5681761697876754485L;

    /**
     * OTP
     */
    @NotEmpty(message = "OTP验证码不能为空")
    @Parameter(description = "OTP")
    private String            otp;

    /**
     * 手机号
     */
    @NotEmpty(message = "手机号不能为空")
    @Parameter(description = "手机号")
    private String            phone;

}
