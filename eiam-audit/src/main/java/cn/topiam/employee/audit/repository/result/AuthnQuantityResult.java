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
 * 认证量统计结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/11/22 23:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "认证量统计响应")
public class AuthnQuantityResult implements Serializable {

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String name;

    /**
     * 数量
     */
    @Schema(description = "数量")
    private Long   count;

    /**
     * 状态
     */
    @Schema(description = "状态")
    private String status;
}
