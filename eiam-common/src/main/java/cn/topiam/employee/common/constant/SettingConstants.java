/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.constant;

import cn.topiam.employee.support.constant.EiamConstants;

/**
 * 设置常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/7/26 21:07
 */
public final class SettingConstants {
    /**
     * 系统设置API路径
     */
    public final static String SETTING_PATH       = EiamConstants.V1_API_PATH + "/setting";

    /**
     * 安全设置API路径
     */
    public final static String SECURITY_PATH      = SETTING_PATH + "/security";

    /**
     * 系统设置缓存 cacheName
     */
    public static final String SETTING_CACHE_NAME = "setting";

    /**
     * admin 缓存 cacheName
     */
    public static final String ADMIN_CACHE_NAME   = "admin";

    /**
     * 应用AES秘钥
     */
    public static final String AES_SECRET         = "security.aes_secret";

}
