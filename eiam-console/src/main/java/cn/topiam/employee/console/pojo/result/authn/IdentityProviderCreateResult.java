/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.authn;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 身份源创建返回
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/29 21:43
 */
@Data
@Builder
@Schema(description = "身份源创建返回")
public class IdentityProviderCreateResult implements Serializable {

    /**
     * ID
     */
    @Parameter(description = "ID")
    private String id;

    /**
     * 提供商类型
     */
    @Parameter(description = "提供商类型")
    private String type;
}
