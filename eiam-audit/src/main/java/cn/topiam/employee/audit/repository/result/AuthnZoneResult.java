/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.repository.result;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 登录区域结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/01/24 23:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录区域响应")
public class AuthnZoneResult implements Serializable {

    /**
     * 省份code
     */
    @Schema(description = "省份code")
    private String name;

    /**
     * 登录次数
     */
    @Schema(description = "登录次数")
    private Long   count;
}
