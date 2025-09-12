/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.analysis;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 应用热点访问结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/11/22 23:16
 */
@Data
@AllArgsConstructor
@Schema(description = "应用访问频次")
public class AppVisitRankResult implements Serializable {

    /**
     * 应用名称
     */
    @Schema(description = "应用名称")
    private String name;

    /**
     * 访问次数
     */
    @Schema(description = "访问次数")
    private Long   count;
}
