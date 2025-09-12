/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.query.setting;

import org.springdoc.core.annotations.ParameterObject;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/4/27 22:32
 */
@Data
@Schema(description = "行政区列表查询")
@ParameterObject
public class AreaListQuery {

    @Parameter(description = "名称")
    private String name;

}
