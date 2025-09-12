/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.entity.app.po;

import cn.topiam.employee.common.entity.app.AppJwtConfigEntity;
import cn.topiam.employee.common.enums.app.AuthorizationType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/02/12 23:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AppJwtConfigPO extends AppJwtConfigEntity {

    /**
     * 应用编码
     */
    private String            appCode;
    /**
     * 应用名称
     */
    private String            appName;

    /**
     * 模版
     */
    private String            appTemplate;

    /**
     * 客户端ID
     */
    private String            clientId;

    /**
     * 客户端秘钥
     */
    private String            clientSecret;

    /**
     * SSO 登录链接
     */
    private String            initLoginUrl;

    /**
     * 授权范围
     */
    private AuthorizationType authorizationType;

    /**
     * 应用是否启用
     */
    private Boolean           enabled;

    /**
     * 是否配置
     */
    private Boolean           configured;

    public AppJwtConfigPO(AppJwtConfigEntity config, String appCode, String appName,
                          String appTemplate, String clientId, String clientSecret,
                          String initLoginUrl, AuthorizationType authorizationType, Boolean enabled,
                          Boolean configured) {
        //DTO 字段
        this.appCode = appCode;
        this.appName = appName;
        this.appTemplate = appTemplate;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.initLoginUrl = initLoginUrl;
        this.authorizationType = authorizationType;
        this.enabled = enabled;
        this.configured = configured;
        // JWT 配置字段
        super.setAppId(config.getAppId());
        super.setRedirectUrl(config.getRedirectUrl());
        super.setTargetLinkUrl(config.getTargetLinkUrl());
        super.setBindingType(config.getBindingType());
        super.setIdTokenSubjectType(config.getIdTokenSubjectType());
        super.setIdTokenTimeToLive(config.getIdTokenTimeToLive());

        super.setCreateBy(config.getCreateBy());
        super.setCreateTime(config.getCreateTime());
        super.setUpdateBy(config.getUpdateBy());
        super.setUpdateTime(config.getUpdateTime());
        super.setRemark(config.getRemark());
    }
}
