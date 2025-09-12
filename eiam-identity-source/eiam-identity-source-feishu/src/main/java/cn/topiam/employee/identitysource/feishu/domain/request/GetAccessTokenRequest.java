/*
 * eiam-identity-source-feishu - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.feishu.domain.request;

import java.io.Serializable;

import com.alibaba.fastjson2.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * access token入参
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022-02-17 23:59
 */
@Data
@AllArgsConstructor
public class GetAccessTokenRequest implements Serializable {
    /**
     * appid
     */
    @JSONField(name = "app_id")
    private String appId;
    /**
     * secret
     */
    @JSONField(name = "app_secret")
    private String appSecret;
}
