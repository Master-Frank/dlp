/*
 * eiam-application-jwt - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.application.jwt.pojo;

import java.io.Serializable;

import cn.topiam.employee.common.enums.app.JwtBindingType;
import cn.topiam.employee.common.enums.app.JwtIdTokenSubjectType;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * JWT 配置返回
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/02/12 22:46
 */
@Data
@Schema(description = "JWT 配置返回响应")
public class AppJwtConfigGetResult implements Serializable {
    /**
     * 应用id
     */
    @Schema(description = "应用id")
    private String                 appId;

    /**
     * SSO 登录链接
     */
    @Schema(description = "SSO 登录链接")
    private String                 initLoginUrl;

    /**
     * 业务系统中（或PC程序）的JWT SSO地址，在单点登录时本系统将向该地址用[GET]方式发送id_token信息，参数名为id_token，
     * 业务系统通过id_token与Public Key可获取业务系统中的用户信息，如果在业务系统（SP）发起登录，请求SP登录地址时如果携带service参数本系统会检验合法性，
     * 成功后会将浏览器重定向到该地址，并携带id_token身份令牌。
     */
    @Schema(description = "Redirect Url")
    private String                 redirectUrl;

    /**
     * 业务系统中在JWT SSO成功后重定向的URL，一般用于跳转到二级菜单等，若设置了该URL，在JWT SSO时会以参数target_link_url优先传递该值，若未设置该值，此时若SSO中有请求参数target_link_url，则会按照请求参数传递该值。此项可选。
     */
    @Schema(description = "target link url")
    private String                 targetLinkUrl;

    @Schema(description = "SSO 绑定类型")
    private JwtBindingType         bindingType;

    /**
     * id_token sub 类型
     */
    private JwtIdTokenSubjectType  idTokenSubjectType;

    /**
     * ID_token 有效期
     */
    @Schema(description = "Token 过期时间（秒）")
    private String                 idTokenTimeToLive;

    /**
     * 协议端点
     */
    @Schema(description = "协议端点")
    private AppJwtProtocolEndpoint protocolEndpoint;
}
