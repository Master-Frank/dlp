/*
 * eiam-authentication-feishu - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.feishu.constant;

/**
 * 飞书认证常量
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/19 23:19
 */
public final class FeiShuAuthenticationConstants {

    public static final String AUTHORIZATION_REQUEST = "https://passport.feishu.cn/suite/passport/oauth/authorize";
    public static final String ACCESS_TOKEN          = "https://passport.feishu.cn/suite/passport/oauth/token";
    public static final String USER_INFO             = "https://passport.feishu.cn/suite/passport/oauth/userinfo";

    public static final String CLIENT_ID             = "client_id";
    public static final String CLIENT_SECRET         = "client_secret";
    public static final String OPEN_ID               = "open_id";

    public static final String CODE                  = "code";
    public static final String HREF                  = "href";

}