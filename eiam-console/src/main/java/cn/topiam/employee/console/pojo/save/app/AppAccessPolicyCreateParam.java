/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.save.app;

import java.util.List;

import cn.topiam.employee.common.enums.app.AppPolicySubjectType;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

/**
 * 应用访问授权策略添加参数
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/9/27 21:29
 */
@Data
@Schema(description = "应用访问授权策略添加参数")
public class AppAccessPolicyCreateParam {

    /**
     * 应用ID
     */
    @Schema(description = "应用ID")
    @NotNull(message = "应用ID不能为空")
    private String               appId;

    /**
     * 主体ID（用户、分组、组织机构）
     */
    @Schema(description = "主体")
    @NotNull(message = "主体不能为空")
    private List<String>         subjectIds;

    /**
     * 主体类型（用户、分组、组织机构）
     */
    @Schema(description = "主体类型")
    @NotNull(message = "主体类型不能为空")
    private AppPolicySubjectType subjectType;
}
