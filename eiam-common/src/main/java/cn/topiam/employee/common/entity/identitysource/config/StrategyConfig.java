/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.entity.identitysource.config;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * 策略配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/9/24 23:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "策略配置")
public class StrategyConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = -1L;

    /**
     * 组织策略
     */
    @Valid
    @NotNull(message = "组织策略不能为空")
    @Parameter(description = "组织策略")
    private Organization      organization;

    /**
     * 用户策略
     */
    @Valid
    @NotNull(message = "用户策略不能为空")
    @Parameter(description = "用户策略")
    private User              user;

    @Data
    public static class Organization implements Serializable {

        @Serial
        private static final long serialVersionUID = -1L;

        /**
         * 目标ID
         */
        @NotEmpty(message = "目标ID不能为空")
        @Parameter(description = "目标ID")
        private String            targetId;
    }

    @Data
    public static class User implements Serializable {

        @Serial
        private static final long serialVersionUID = -1L;

        /**
         * 默认密码
         */
        @Parameter(description = "默认密码")
        private String            defaultPassword;

        /**
         * 是否启用
         */
        @Parameter(description = "是否启用")
        private Boolean           enabled;

        /**
         * 邮件通知
         */
        @Parameter(description = "邮件通知")
        private Boolean           emailNotify;
    }
}
