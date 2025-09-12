/*
 * eiam-application-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.application;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/13 21:32
 */
@Data
@SuperBuilder
public class AbstractProtocolConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = 7006674647924828758L;

    /**
     * 应用ID
     */
    @NonNull
    private String            appId;

    /**
     * 应用名称
     */
    @NonNull
    private String            appName;

    /**
     * 客户端ID
     */
    @NonNull
    private String            clientId;

    /**
     * 客户端秘钥
     */
    @NonNull
    private String            clientSecret;

    /**
     * 应用编码
     */
    @NonNull
    private String            appCode;

    /**
     * 应用模版
     */
    @NonNull
    private String            appTemplate;
}
