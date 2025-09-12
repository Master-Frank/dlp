/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.app;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 获取分组返回
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/31 11:25
 */
@Data
@Schema(description = "获取分组返回响应")
public class AppGroupGetResult implements Serializable {
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
