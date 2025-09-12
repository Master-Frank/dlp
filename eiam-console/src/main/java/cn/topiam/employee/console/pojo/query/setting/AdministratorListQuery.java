/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.query.setting;

import org.springdoc.core.annotations.ParameterObject;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/14 21:36
 */
@Data
@Schema(description = "管理员列表查询")
@ParameterObject
public class AdministratorListQuery {
    /**
     * username
     */
    @Parameter(description = "用户名")
    private String username;

    /**
     * phone
     */
    @Parameter(description = "手机号码")
    private String phone;

    /**
     * email
     */
    @Parameter(description = "邮箱地址")
    private String email;

}
