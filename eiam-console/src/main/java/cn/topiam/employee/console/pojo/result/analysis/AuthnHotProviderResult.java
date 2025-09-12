/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.analysis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 热点认证方式结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/11/22 23:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "热点认证方式响应")
public class AuthnHotProviderResult {

    /**
     * 认证方式
     */
    @Schema(description = "认证方式")
    private String name;

    /**
     * 使用次数
     */
    @Schema(description = "使用次数")
    private Long   count;
}
