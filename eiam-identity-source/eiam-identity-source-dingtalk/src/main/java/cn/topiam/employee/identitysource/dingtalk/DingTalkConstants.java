/*
 * eiam-identity-source-dingtalk - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.dingtalk;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import static cn.topiam.employee.support.constant.EiamConstants.COLON;

/**
 * 常量
 *
 * @author TopIAM
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DingTalkConstants {
    /**
     * logger name
     */
    public static final String LOGGER_NAME            = "dingtalk-identity";
    /**
     * 中文
     */
    public static final String ZH_CN                  = "zh_CN";
    /**
     * 分页
     */
    public static final long   CURSOR                 = 0L;
    /**
     * 分页大小， 最大100
     */
    public static final long   SIZE                   = 100L;
    /**
     * 钉钉开放接口域名
     */
    public static final String DOMAIN                 = "https://oapi.dingtalk.com";
    /**
     * 本接口只支持获取当前部门下的一级部门基础信息
     */
    public static final String DEPARTMENT_LIST        = DOMAIN + "/topapi/v2/department/listsub";
    /**
     * 调用本接口根据部门ID获取指定部门详情。
     */
    public static final String DEPARTMENT_GET         = DOMAIN + "/topapi/v2/department/get";
    /**
     * 本接口只支持获取指定部门下的员工详情信息，子部门员工信息获取不到。
     */
    public static final String USER_LIST              = DOMAIN + "/topapi/v2/user/list";
    /**
     * 获取用户信息
     */
    public static final String GET_USER               = DOMAIN + "/topapi/v2/user/get";

    /**
     * accessToken缓存名
     */
    public static final String DING_TALK_ACCESS_TOKEN = "ding_talk_access_token" + COLON;
}
