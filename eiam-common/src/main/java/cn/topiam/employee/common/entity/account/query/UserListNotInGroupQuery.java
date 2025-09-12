/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.entity.account.query;

import java.io.Serial;
import java.io.Serializable;

import org.springdoc.core.annotations.ParameterObject;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

/**
 * 查询用户列表（不在指定用户组）入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 23:08
 */
@Data
@Schema(description = "查询用户列表（不在指定用户组）入参")
@ParameterObject
public class UserListNotInGroupQuery implements Serializable {
    @Serial
    private static final long serialVersionUID = -7110595216804896858L;
    /**
     * 组ID
     */
    @NotEmpty(message = "组ID不能为空")
    @Parameter(description = "组ID")
    private String            id;
    /**
     * 关键字
     */
    @Parameter(description = "关键字")
    private String            keyword;
}
