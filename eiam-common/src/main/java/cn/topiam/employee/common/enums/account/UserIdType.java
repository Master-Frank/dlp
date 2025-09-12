/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.enums.account;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonValue;

import cn.topiam.employee.support.enums.BaseEnum;
import cn.topiam.employee.support.web.converter.EnumConvert;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/10/6 22:11
 */
public enum UserIdType implements BaseEnum {
                                            /**
                                             * 身份证号
                                             */
                                            IDENTITY_CARD("identity_card", "身份证号");

    /**
     * code
     */
    @JsonValue
    @JSONField
    private final String code;
    /**
     * desc
     */
    private final String desc;

    UserIdType(String code, String desc) {
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

    /**
     * 获取来源
     *
     * @param code {@link String}
     * @return {@link UserIdType}
     */
    @EnumConvert
    public static UserIdType getType(String code) {
        UserIdType[] values = values();
        for (UserIdType source : values) {
            if (String.valueOf(source.getCode()).equals(code)) {
                return source;
            }
        }
        return null;
    }

    public static UserIdType getName(String name) {
        UserIdType[] values = values();
        for (UserIdType source : values) {
            if (source.name().equals(name)) {
                return source;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
