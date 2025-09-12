/*
 * eiam-authentication-alipay - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.alipay.client;

import com.aliyun.tea.NameInMap;
import com.aliyun.tea.TeaModel;
import com.aliyun.tea.Validation;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/25 22:26
 */
@Getter
@Setter
public class AlipaySystemOauthTokenResponse extends TeaModel {
    @NameInMap("http_body")
    @Validation(required = true)
    public String httpBody;

    @NameInMap("code")
    @Validation(required = true)
    public String code;

    @NameInMap("msg")
    @Validation(required = true)
    public String msg;

    @NameInMap("sub_code")
    @Validation(required = true)
    public String subCode;

    @NameInMap("sub_msg")
    @Validation(required = true)
    public String subMsg;

    @NameInMap("open_id")
    @Validation(required = true)
    public String openId;

    @NameInMap("access_token")
    @Validation(required = true)
    public String accessToken;

    @NameInMap("expires_in")
    @Validation(required = true)
    public Long   expiresIn;

    @NameInMap("refresh_token")
    @Validation(required = true)
    public String refreshToken;

    @NameInMap("re_expires_in")
    @Validation(required = true)
    public Long   reExpiresIn;

}
