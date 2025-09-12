/*
 * eiam-protocol-oidc - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.eiam.protocol.oidc.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * TokenTypeMixin
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/6/30 21:10
 */
@SuppressWarnings("AlibabaAbstractClassShouldStartWithAbstractNaming")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
abstract class TokenTypeMixin {

    @JsonCreator
    TokenTypeMixin(@JsonProperty("value") String value) {
    }

}
