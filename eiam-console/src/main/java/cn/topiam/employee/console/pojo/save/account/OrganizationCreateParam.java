/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.save.account;

import java.io.Serial;
import java.io.Serializable;

import cn.topiam.employee.common.enums.account.OrganizationType;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * 创建组织架构入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 23:16
 */
@Data
@Schema(description = "创建组织架构入参")
public class OrganizationCreateParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 3118058164024117164L;

    /**
     * code
     */
    @Schema(description = "编码")
    private String            code;

    /**
     * 上级部门
     */
    @NotEmpty(message = "请选择上级组织")
    @Schema(description = "上级组织")
    private String            parentId;

    /**
     * 名称
     */
    @Schema(description = "架构名称")
    @NotBlank(message = "名称不能为空")
    private String            name;

    /**
     * 类型
     */
    @Schema(description = "架构类型")
    @NotNull(message = "类型不能为空")
    private OrganizationType  type;

    /**
     * 外部ID
     */
    @Schema(description = "外部ID")
    private String            externalId;

    /**
     * 区域
     */
    @Schema(description = "所在区域")
    private String            area;

    /**
     * 描述
     */
    @Schema(description = "架构描述")
    private String            desc;

    /**
     * 排序
     */
    @Schema(description = "架构排序")
    private String            order;

    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    private Boolean           enabled;
}
