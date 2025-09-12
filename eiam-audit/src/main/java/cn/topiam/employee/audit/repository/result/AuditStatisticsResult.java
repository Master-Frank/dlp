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
 * 审计统计
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/10/04 23:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "审计统计")
public class AuditStatisticsResult implements Serializable {

    /**
     * key
     */
    private String key;

    /**
     * count
     */
    private Long   count;
}
