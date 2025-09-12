/*
 * eiam-application-form - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.application.form.pojo;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
* 协议端点域
*
* @author TopIAM
* Created by support@topiam.cn on 2022/6/4 23:37
*/
@Data
@Schema(description = "协议端点")
public class AppFormProtocolEndpoint implements Serializable {

    @Serial
    private static final long serialVersionUID = -2261602995152894964L;

    /**
     * IDP SSO 端点
     */
    @Schema(description = "IDP SSO 端点")
    private String            idpSsoEndpoint;
}
