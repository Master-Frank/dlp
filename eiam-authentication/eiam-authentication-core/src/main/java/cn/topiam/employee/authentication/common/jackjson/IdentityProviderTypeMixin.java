/*
 * eiam-authentication-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.common.jackjson;

import com.fasterxml.jackson.annotation.*;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2022/12/31 21:18
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class IdentityProviderTypeMixin {

    @JsonCreator
    IdentityProviderTypeMixin(@JsonProperty("value") String value,
                              @JsonProperty("name") String name,
                              @JsonProperty("desc") String desc) {
    }
}
