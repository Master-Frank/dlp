/*
 * eiam-openapi - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.openapi.constant;

import static cn.topiam.employee.support.constant.EiamConstants.V1_API_PATH;

/**
 * Open API 常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/9/5 21:58
 */
public class OpenApiV1Constants {
    /**
     * OpenAPI 路径
     */
    public final static String  OPEN_API_V1_PATH        = V1_API_PATH;

    public final static Integer ACCESS_TOKEN_EXPIRES_IN = 7200;

    /**
     * 组名称
     */
    public static final String  OPEN_API_NAME           = "开放接口";

    /**
     * 访问凭证
     */
    public final static String  AUTH_PATH               = OPEN_API_V1_PATH + "/auth";

    /**
     * 账户
     */
    public final static String  ACCOUNT_PATH            = OPEN_API_V1_PATH + "/account";

    /**
     * 用户
     */
    public final static String  USER_PATH               = ACCOUNT_PATH + "/user";

    /**
     * 组织
     */
    public final static String  ORGANIZATION_PATH       = ACCOUNT_PATH + "/organization";

    /**
     * 组织
     */
    public final static String  APP_ACCOUNT_PATH        = ACCOUNT_PATH + "/app/account";
}
