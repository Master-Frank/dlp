/*
 * eiam-application-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.application;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/7/10 21:07
 */
@Data
public class AppAccount implements Serializable {

    @Serial
    private static final long serialVersionUID = 3496932644393351967L;

    /**
     * 应用ID
     */
    private String            appId;

    /**
     * 用户ID
     */
    private String            userId;

    /**
     * 账户名称
     */
    private String            account;

    /**
     * 账户密码
     */
    private String            password;

    /**
     * 是否默认
     */
    private Boolean           defaulted;
}
