/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.authn;

import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 社交认证源平台列表，带有元素字段，避免前端重复画页面，基本都是input
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/26 21:57
 */
@Data
@Schema(description = "社交认证源平台列表")
public class IdentityProviderListResult implements Serializable {

    /**
     * ID
     */
    @Parameter(description = "ID")
    private String  id;

    /**
     * name
     */
    @Parameter(description = "名称")
    private String  name;

    /**
     * 提供商
     */
    @Parameter(description = "提供商")
    private String  type;

    /**
     * 是否启用
     */
    @Parameter(description = "是否启用")
    private Boolean enabled;

    /**
     * 描述
     */
    @Parameter(description = "描述")
    private String  desc;

    /**
     * 备注
     */
    @Parameter(description = "备注")
    private String  remark;
}
