/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.constant;

import static cn.topiam.employee.support.constant.EiamConstants.COLON;
import static cn.topiam.employee.support.constant.EiamConstants.V1_API_PATH;

/**
 * 应用管理常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/7/26 21:07
 */
public final class AppConstants {

    /**
     * 应用管理API路径
     */
    public final static String APP_PATH                    = V1_API_PATH + "/app";

    /**
     * APP 配置缓存前缀
     */
    public static final String APP_CACHE_NAME_PREFIX       = "app" + COLON;

    /**
     * 分组配置缓存前缀
     */
    public static final String APP_GROUP_CACHE_NAME_PREFIX = "app_group" + COLON;

    /**
     * APP 应用基本信息
     */
    public static final String APP_CACHE_NAME              = APP_CACHE_NAME_PREFIX + "basic";

    /**
     * 应用账户缓存名称
     */
    public static final String APP_ACCOUNT_CACHE_NAME      = APP_CACHE_NAME_PREFIX + "account";

    /**
     * OIDC 配置缓存名称
     */
    public static final String OIDC_CONFIG_CACHE_NAME      = APP_CACHE_NAME_PREFIX + "oidc";

    /**
     * APP Cert
     */
    public static final String APP_CERT_CACHE_NAME         = APP_CACHE_NAME_PREFIX + "cert";

    /**
     * FORM 配置缓存名称
     */
    public static final String FORM_CONFIG_CACHE_NAME      = APP_CACHE_NAME_PREFIX + "form";

    /**
     * JWT 配置缓存名称
     */
    public static final String JWT_CONFIG_CACHE_NAME       = APP_CACHE_NAME_PREFIX + "jwt";

}
