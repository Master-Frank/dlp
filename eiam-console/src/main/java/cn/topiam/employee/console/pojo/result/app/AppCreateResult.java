/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.app;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 应用创建返回结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/26 23:25
 */
@Data
@AllArgsConstructor
@Schema(description = "应用创建返回响应")
public class AppCreateResult implements Serializable {

    /**
     * ID
     */
    @Parameter(description = "ID")
    private String id;
}
