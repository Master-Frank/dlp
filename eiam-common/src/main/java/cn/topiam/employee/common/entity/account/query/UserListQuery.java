/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.entity.account.query;

import java.io.Serial;
import java.io.Serializable;

import org.springdoc.core.annotations.ParameterObject;

import cn.topiam.employee.common.enums.UserStatus;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 查询用户列表入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 23:08
 */
@Data
@Schema(description = "查询用户列表入参")
@ParameterObject
public class UserListQuery implements Serializable {
    @Serial
    private static final long serialVersionUID = -7110595216804896858L;

    /**
     * 用户名
     */
    @Parameter(description = "用户名")
    private String            username;

    /**
     * 姓名
     */
    @Parameter(description = "姓名")
    private String            fullName;

    /**
     * 邮箱
     */
    @Parameter(description = "邮箱")
    private String            email;

    /**
     * 手机号
     */
    @Parameter(description = "手机号")
    private String            phone;

    /**
     * 组织ID
     */
    @Parameter(description = "组织ID")
    private String            organizationId;

    /**
     * 包含子组织
     */
    @Parameter(description = "包含子组织")
    private Boolean           inclSubOrganization;

    /**
     * 状态
     */
    @Parameter(description = "状态")
    private UserStatus        status;

    /**
     * 数据来源
     */
    @Parameter(description = "数据来源")
    private String            dataOrigin;
}
