/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.save.identitysource;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 身份源创建返回结果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/21 21:34
 */
@Data
@AllArgsConstructor
@Schema(description = "身份源创建返回响应")
public class IdentitySourceCreateResult implements Serializable {
    /**
     * ID
     */
    @Parameter(description = "ID")
    private String id;
}
