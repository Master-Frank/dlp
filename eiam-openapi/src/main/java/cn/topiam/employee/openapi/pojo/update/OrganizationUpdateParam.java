/*
 * eiam-openapi - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.openapi.pojo.update;

import java.io.Serial;
import java.io.Serializable;

import cn.topiam.employee.common.enums.account.OrganizationType;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 编辑组织架构入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 23:16
 */
@Data
@Schema(description = "修改组织机构入参")
public class OrganizationUpdateParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 4570955457331971748L;

    /**
     * id
     */
    @Schema(description = "组织ID")
    @NotBlank(message = "ID不能为空")
    private String            id;

    /**
     * 名称
     */
    @Schema(description = "组织名称")
    @NotBlank(message = "组织名称不能为空")
    private String            name;

    /**
     * 类型
     */
    @Schema(description = "组织类型")
    @NotNull(message = "组织类型不能为空")
    private OrganizationType  type;

    /**
     * 描述
     */
    @Schema(description = "组织描述")
    private String            desc;

    /**
     * 排序
     */
    @Schema(description = "组织排序")
    private String            order;

    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    private Boolean           enabled;

}
