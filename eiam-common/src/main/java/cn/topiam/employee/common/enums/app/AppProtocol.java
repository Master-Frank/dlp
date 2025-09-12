/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.enums.app;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.topiam.employee.support.enums.BaseEnum;
import cn.topiam.employee.support.web.converter.EnumConvert;

/**
 * 应用模板
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/11/29 22:27
 */
public enum AppProtocol implements BaseEnum {
                                             /**
                                              * OIDC
                                              */
                                             OIDC("OIDC", "OIDC"),

                                             /**
                                              * JWT
                                              */
                                             JWT("JWT", "JWT"),

                                             /**
                                              * FORM表单
                                              */
                                             FORM("FORM", "表单代填");

    @JsonValue
    private final String code;
    /**
     * name
     */
    private final String desc;

    AppProtocol(String code, String desc) {
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
    public static AppProtocol getType(String code) {
        AppProtocol[] values = values();
        for (AppProtocol source : values) {
            if (String.valueOf(source.getCode()).equals(code)) {
                return source;
            }
        }
        return null;
    }
}
