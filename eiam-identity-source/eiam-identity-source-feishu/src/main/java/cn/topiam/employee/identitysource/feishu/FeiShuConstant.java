/*
 * eiam-identity-source-feishu - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.feishu;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import static cn.topiam.employee.support.constant.EiamConstants.COLON;

/**
 * 常量
 *
 * @author TopIAM
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeiShuConstant {

    public static final String LOGGER_NAME          = "feishu-identity";

    /**
     * 分页大小， 最大50
     */
    public static final int    PAGE_SIZE            = 50;
    /**
     * 钉钉开放接口域名
     */
    public static final String DOMAIN               = "https://open.feishu.cn";

    /**
     * 获取 app_access_token（企业自建应用）
     * https://open.feishu.cn/document/ukTMukTMukTM/ukDNz4SO0MjL5QzM/auth-v3/auth/app_access_token_internal
     */
    public static final String APP_ACCESS_TOKEN_URL = DOMAIN
                                                      + "/open-apis/auth/v3/app_access_token/internal";

    /**
     * 获取 tenant_access_token（企业自建应用）
     * https://open.feishu.cn/document/ukTMukTMukTM/ukDNz4SO0MjL5QzM/auth-v3/auth/tenant_access_token_internal
     */
    public static final String ACCESS_TOKEN_URL     = DOMAIN
                                                      + "/open-apis/auth/v3/tenant_access_token/internal";
    /**
     * 递归获取子部门列表
     * https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/contact-v3/department/children
     */
    public static final String DEPARTMENT_LIST      = DOMAIN
                                                      + "/open-apis/contact/v3/departments/%s/children";
    /**
     * 获取单个部门信息
     * https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/contact-v3/department/get
     */
    public static final String DEPARTMENT_GET       = DOMAIN + "/open-apis/contact/v3/departments/";
    /**
     * 获取部门直属用户列表
     * https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/contact-v3/user/find_by_department
     */
    public static final String USER_LIST            = DOMAIN
                                                      + "/open-apis/contact/v3/users/find_by_department";
    /**
     * 获取用户信息
     * https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/contact-v3/user/get
     */
    public static final String GET_USER             = DOMAIN + "/open-apis/contact/v3/users/";
    /**
     * accessToken缓存名
     */
    public static final String FEI_SHU_ACCESS_TOKEN = "fei_shu_access_token" + COLON;
}
