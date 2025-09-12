/*
 * eiam-authentication-wechatwork - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.wechatwork.constant;

/**
 * 企业微信
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/9 22:19
 */
public final class WeChatWorkAuthenticationConstants {
    public final static String APP_ID        = "appid";
    public final static String AGENT_ID      = "agentid";
    public final static String HREF          = "href";
    public final static String LOGIN_TYPE    = "login_type";
    public final static String JSSDK         = "jssdk";
    public final static String URL_AUTHORIZE = "https://open.work.weixin.qq.com/wwopen/sso/v1/qrConnect";

    public final static String GET_USER_INFO = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo";

    public final static String GET_TOKEN     = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";
}
