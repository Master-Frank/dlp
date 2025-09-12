/*
 * eiam-authentication-qq - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.qq.constant;

/**
 * 企业微信
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/9 22:19
 */
public final class QqAuthenticationConstants {
    /**
     * 获取授权码地址
     */
    public static final String URL_AUTHORIZE        = "https://graph.qq.com/oauth2.0/authorize";
    /**
     * 获取令牌地址
     */
    public static final String URL_GET_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";
    /**
     * 获取 openId 的地址
     */
    public static final String URL_GET_OPEN_ID      = "https://graph.qq.com/oauth2.0/me";
    /**
     * 获取用户信息的地址
     */
    public static final String URL_GET_USER_INFO    = "https://graph.qq.com/user/get_user_info";

}
