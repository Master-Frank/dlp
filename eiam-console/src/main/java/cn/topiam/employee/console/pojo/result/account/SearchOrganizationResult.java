/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.account;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 查询组织架构结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 21:27
 */
@Data
@Schema(description = "查询组织架构结果")
public class SearchOrganizationResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 5599721546299678344L;
    /**
     * 主键ID
     */
    @Schema(description = "ID")
    private String            id;

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String            name;

    /**
     * 父级
     */
    @Schema(description = "父级")
    private String            parentId;

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
     * 外部ID
     */
    @Schema(description = "外部ID")
    private String            externalId;

    /**
     * 类型
     */
    @Schema(description = "类型")
    private String            type;

    /**
     * 来源
     */
    @Schema(description = "数据来源")
    private String            dataOrigin;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer           order;

    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    private Boolean           enabled;

    /**
     * 是否叶子节点
     */
    @JsonProperty(value = "isLeaf")
    @Schema(description = "是否叶子节点")
    private Boolean           leaf;
}
