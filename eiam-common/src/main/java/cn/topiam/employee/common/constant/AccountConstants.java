/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.constant;

import static cn.topiam.employee.support.constant.EiamConstants.ROOT_NODE;
import static cn.topiam.employee.support.constant.EiamConstants.V1_API_PATH;

/**
 * 账户常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/7/26 21:07
 */
public final class AccountConstants {

    /**
     * 用户API
     */
    public final static String USER_PATH            = V1_API_PATH + "/user";
    /**
     * 组织机构API
     */
    public final static String ORGANIZATION_PATH    = V1_API_PATH + "/organization";
    /**
     * 用户组API
     */
    public final static String USER_GROUP_PATH      = V1_API_PATH + "/user_group";

    /**
     * 身份源API
     */
    public final static String IDENTITY_SOURCE_PATH = V1_API_PATH + "/identity_source";

    /**
     * 身份源缓存 cacheName
     */
    public static final String IDS_CACHE_NAME       = "ids";

    /**
     * user 缓存 cacheName
     */
    public static final String USER_CACHE_NAME      = "user";
    /**
     * org 缓存 cacheName
     */
    public static final String ORG_CACHE_NAME       = "organization";

    /**
     * 根部门 ID
     */
    public static final String ROOT_DEPT_ID         = ROOT_NODE;

}
