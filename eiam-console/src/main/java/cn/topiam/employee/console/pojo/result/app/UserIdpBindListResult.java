/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.app;

import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户身份提供商绑定列表查询结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/11/3 22:13
 */
@Data
@Schema(description = "用户身份提供商绑定列表查询响应")
public class UserIdpBindListResult {

    /**
     * id
     */
    @Schema(description = "id")
    private String        id;

    /**
     * open id
     */
    @Schema(description = "open id")
    private String        openId;

    /**
     * 提供商名称
     */
    @Schema(description = "提供商名称")
    private String        idpName;

    /**
     * 提供商类型
     */
    @Schema(description = "提供商类型")
    private String        idpType;

    /**
     * 绑定时间
     */
    @Schema(description = "绑定时间")
    private LocalDateTime bindTime;
}
