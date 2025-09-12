/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.query.app;

import java.io.Serial;
import java.io.Serializable;

import org.springdoc.core.annotations.ParameterObject;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 查询应用列表
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/7/6 22:38
 */
@Data
@Schema(description = "查询应用列表")
@ParameterObject
public class GetAppListQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = -4981513177967939516L;

    /**
     * 应用名称
     */
    @Parameter(description = "应用名称")
    private String            name;

    /**
     * 应用分组ID
     */
    @Parameter(description = "应用分组ID")
    private String            groupId;

}
