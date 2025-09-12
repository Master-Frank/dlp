/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.account;

import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 查询用户详情结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 23:22
 */
@Data
@Schema(description = "查询用户详情响应")
public class UserGroupMemberListResult implements Serializable {
    /**
     * ID
     */
    @Schema(description = "ID")
    private String id;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 姓名
     */
    @Schema(description = "姓名")
    private String fullName;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 头像URL
     */
    @Schema(description = "头像URL")
    private String avatar;

    /**
     * 组织显示目录
     */
    @Schema(description = "组织显示目录")
    private String orgDisplayPath;
}
