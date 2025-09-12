/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.setting;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 安全策略
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023-03-09
 */
@Schema(description = "获取安全策略")
@Data
public class SecurityDefensePolicyConfigResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 2578080414501381617L;

    /**
     * 连续登录失败持续时间
     */
    @Parameter(description = "连续登录失败持续时间")
    private Integer           loginFailureDuration;

    /**
     * 连续登录失败次数
     */
    @Parameter(description = "连续登录失败次数")
    private Integer           loginFailureCount;

    /**
     * 自动解锁时间（分）
     */
    @Parameter(description = "自动解锁时间（分）")
    private Integer           autoUnlockTime;

    /**
     * 内容安全策略
     */
    @Parameter(description = "内容安全策略")
    private String            contentSecurityPolicy;
}
