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
public class AlipaySystemUserInfoShareResponse extends TeaModel {
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

    @NameInMap("user_id")
    @Validation(required = true)
    public String userId;

    @NameInMap("avatar")
    @Validation(required = true)
    public String avatar;

    @NameInMap("city")
    @Validation(required = true)
    public Long   city;

    @NameInMap("nick_name")
    @Validation(required = true)
    public String nickName;

    @NameInMap("province")
    @Validation(required = true)
    public Long   province;

    @NameInMap("gender")
    @Validation(required = true)
    public Long   gender;

}
