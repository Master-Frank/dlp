/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.account;

import java.io.Serial;
import java.io.Serializable;

import cn.topiam.employee.common.enums.account.OrganizationType;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 获取组织
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 21:27
 */
@Data
@Schema(description = "获取组织")
public class OrganizationResult implements Serializable {
    @Serial
    private static final long serialVersionUID = -150631305460653395L;
    /**
     * 主键ID
     */
    @Schema(description = "ID")
    private String            id;
    /**
     * key
     */
    @Schema(description = "名称")
    private String            name;

    /**
     * 显示路径
     */
    @Schema(description = "显示路径")
    private String            displayPath;
    /**
     * 编码
     */
    @Schema(description = "编码")
    private String            code;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private String            order;

    /**
     * 组织机构类型
     */
    @Schema(description = "机构类型")
    private OrganizationType  type;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String            remark;

}
