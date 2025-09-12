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
 * 密码策略
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/5/5 22:32
 */
@Data
@Schema(description = "登录策略列表查询")
@ParameterObject
public class PasswordPolicyListQuery {

    /**
     * 名称
     */
    @Parameter(description = "名称")
    private String  name;

    /**
     * 是否启用
     */
    @Parameter(description = "是否启用")
    private Boolean enabled;
}
