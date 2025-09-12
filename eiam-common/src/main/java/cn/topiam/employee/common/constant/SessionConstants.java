/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.constant;

import cn.topiam.employee.support.constant.EiamConstants;

/**
 * SessionConstants
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/6/8 21:12
 */
public final class SessionConstants {
    public final static String SESSION_PATH   = EiamConstants.V1_API_PATH + "/session";

    /**
     * CURRENT_USER
     */
    public static final String CURRENT_USER   = SESSION_PATH + "/current_user";

    /**
     * session 当前状态
     */
    public static final String CURRENT_STATUS = SESSION_PATH + "/current_status";

}
