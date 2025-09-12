/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.query.identity;

import java.io.Serial;
import java.io.Serializable;

import org.springdoc.core.annotations.ParameterObject;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 查询身份源列表入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/14 01:29
 */
@Data
@Schema(description = "查询身份源列表入参")
@ParameterObject
public class IdentitySourceListQuery implements Serializable {
    @Serial
    private static final long serialVersionUID = -7110595216804896858L;

    /**
     * 身份源名称
     */
    @Parameter(description = "身份源名称")
    private String            name;
}
