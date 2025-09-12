/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.pojo.result;

import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 账号绑定
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/3/31 21:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "账号绑定")
public class BoundIdpListResult extends IdentityProviderResult {

    /**
     * ID
     */
    @Schema(description = "ID")
    private String  id;

    /**
     * IDP id
     */
    @Schema(description = "IDP ID")
    private String  idpId;

    /**
     * 是否已绑定
     */
    @Schema(description = "是否已绑定")
    private Boolean bound;
}
