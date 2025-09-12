/*
 * eiam-openapi - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.openapi.pojo.result;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 组织用户关系
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 21:27
 */
@Data
@Schema(description = "组织用户关系")
public class OrganizationMember implements Serializable {

    @Serial
    private static final long serialVersionUID = 5599721546299698344L;

    /**
     * 主键ID
     */
    @Schema(description = "ID")
    private String            id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private String            userId;

    /**
     * 组织ID
     */
    @Schema(description = "组织ID")
    private String            orgId;
}
