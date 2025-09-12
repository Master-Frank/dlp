/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.constant;

import cn.topiam.employee.support.constant.EiamConstants;
import static cn.topiam.employee.support.security.constant.SecurityConstants.LOGIN_PATH;

/**
 * 认证常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/7/26 21:07
 */
public final class AuthnConstants {
    public final static String AUTHN_PATH   = EiamConstants.V1_API_PATH + "/authn";

    /**
     * 登录配置
     */
    public static final String LOGIN_CONFIG = LOGIN_PATH + "/config";

    /**
     * 前端登录路由
     */
    public static final String FE_LOGIN     = "/login";

}
