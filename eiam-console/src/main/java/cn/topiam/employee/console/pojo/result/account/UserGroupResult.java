/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.account;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import static cn.topiam.employee.support.constant.EiamConstants.DEFAULT_DATE_TIME_FORMATTER_PATTERN;

/**
 * 查询用户分组详情
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/11 23:22
 */
@Data
@Schema(description = "根据ID查询用户分组响应")
public class UserGroupResult implements Serializable {
    @Serial
    private static final long serialVersionUID = -5144879825451360221L;
    /**
     * 用户ID
     */
    @Parameter(description = "用户组ID")
    private String            id;

    /**
     * 用户组名称
     */
    @Parameter(description = "用户组名称")
    private String            name;

    /**
     * 用户组编码
     */
    @Parameter(description = "用户组编码")
    private String            code;

    /**
     * 备注
     */
    @Parameter(description = "备注")
    private String            remark;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = DEFAULT_DATE_TIME_FORMATTER_PATTERN)
    @Parameter(description = "创建时间")
    private LocalDateTime     createTime;
}
