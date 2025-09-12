/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.pojo.result.authn;

import java.io.Serial;
import java.io.Serializable;

import cn.topiam.employee.authentication.common.client.IdentityProviderConfig;

import lombok.Data;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 认证源详情
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/21 21:21
 */
@Data
@Schema(description = "获取社交认证源")
public class IdentityProviderResult implements Serializable {
    @Serial
    private static final long      serialVersionUID = -1440230086940289961L;

    /**
     * ID
     */
    @Parameter(description = "ID")
    private String                 id;

    /**
     * 名称
     */
    @Parameter(description = "名称")
    private String                 name;

    /**
     * 提供商类型
     */
    @Parameter(description = "提供商类型")
    private String                 type;

    /**
     * 配置
     */
    @Parameter(description = "配置JSON")
    private IdentityProviderConfig config;

    /**
     * 是否展示
     */
    @Parameter(description = "是否展示")
    private Boolean                displayed;

    /**
     * 备注
     */
    @Parameter(description = "备注")
    private String                 remark;

    /**
     * 回调地址
     */
    @Parameter(description = "回调地址")
    private String                 redirectUri;
}
