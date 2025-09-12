/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.app;

import java.io.Serializable;
import java.time.LocalDateTime;

import cn.topiam.employee.common.enums.app.AppGroupType;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 分组列表返回
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/31 10:29
 */
@Data
@Schema(description = "分组列表返回")
public class AppGroupListResult implements Serializable {

    /**
     * ID
     */
    @Parameter(description = "ID")
    private String        id;

    /**
     * 分组名称
     */
    @Parameter(description = "分组名称")
    private String        name;

    /**
     * 分组编码
     */
    @Parameter(description = "分组编码")
    private String        code;

    /**
     * 应用数量
     */
    @Parameter(description = "应用数量")
    private Integer       appCount;

    /**
     * 分组类型
     */
    @Parameter(description = "分组类型")
    private AppGroupType  type;

    /**
     * 创建时间
     */
    @Parameter(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 备注
     */
    @Parameter(description = "备注")
    private String        remark;

}
