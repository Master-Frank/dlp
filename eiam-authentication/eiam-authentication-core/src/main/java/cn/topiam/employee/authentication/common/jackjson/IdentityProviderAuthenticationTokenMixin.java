/*
 * eiam-authentication-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.common.jackjson;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.*;

import cn.topiam.employee.authentication.common.IdentityProviderType;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/10 22:25
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, isGetterVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class IdentityProviderAuthenticationTokenMixin {
    @JsonCreator
    IdentityProviderAuthenticationTokenMixin(@JsonProperty("principal") Object principal,
                                             @JsonProperty("providerType") IdentityProviderType providerType,
                                             @JsonProperty("providerId") String providerId,
                                             @JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities) {
    }
}
