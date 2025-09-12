/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.pojo.result;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 三方登录提供商
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/3/31 21:58
 */
@Data
@Schema(description = "三方登录提供商")
public class IdentityProviderResult implements Serializable {
    @Serial
    private static final long serialVersionUID = -6482651783349719888L;

    /**
     * CODE
     */
    @Schema(description = "CODE")
    private String            code;

    /**
     * name
     */
    @Schema(description = "名称")
    private String            name;

    /**
     * 提供商
     */
    @Schema(description = "提供商")
    private String            type;

    /**
     * 提供商类型
     */
    @Schema(description = "提供商类型")
    private String            category;

    /**
     * 认证地址
     */
    @Schema(description = "认证地址")
    private String            authorizationUri;
}
