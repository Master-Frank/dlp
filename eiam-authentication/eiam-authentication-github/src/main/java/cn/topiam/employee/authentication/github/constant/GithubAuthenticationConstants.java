/*
 * eiam-authentication-github - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.github.constant;

/**
 * GITHUB
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/16 22:19
 */
public final class GithubAuthenticationConstants {
    /**
     * 获取授权码地址
     */
    public static final String URL_AUTHORIZE        = "https://github.com/login/oauth/authorize";
    /**
     * 获取令牌地址
     */
    public static final String URL_GET_ACCESS_TOKEN = "https://github.com/login/oauth/access_token";
    /**
     * 获取用户信息的地址
     */
    public static final String URL_GET_USER_INFO    = "https://api.github.com/user";

}
