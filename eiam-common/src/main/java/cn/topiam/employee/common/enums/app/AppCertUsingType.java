/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.enums.app;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.topiam.employee.support.enums.BaseEnum;
import cn.topiam.employee.support.web.converter.EnumConvert;

/**
 * 证书使用类型
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/31 22:28
 */
public enum AppCertUsingType implements BaseEnum {
                                                  /**
                                                   * OIDC JWK
                                                   */
                                                  OIDC_JWK("oidc_jwk", "OIDC JWK"),

                                                  /**
                                                   * JWT 加密
                                                   */
                                                  JWT_ENCRYPT("jwt_encrypt", "JWT 加密");

    @JsonValue
    private final String code;
    /**
     * name
     */
    private final String desc;

    AppCertUsingType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @EnumConvert
    public static AppCertUsingType getType(String code) {
        AppCertUsingType[] values = values();
        for (AppCertUsingType type : values) {
            if (String.valueOf(type.getCode()).equals(code)) {
                return type;
            }
        }
        return null;
    }
}
