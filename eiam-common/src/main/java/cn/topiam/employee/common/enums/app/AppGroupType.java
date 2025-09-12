/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.enums.app;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.topiam.employee.support.enums.BaseEnum;
import cn.topiam.employee.support.web.converter.EnumConvert;

/**
 * 应用分组类型
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/9/9 16:22
 */
public enum AppGroupType implements BaseEnum {
                                              /**
                                               * 默认分组
                                               */
                                              DEFAULT("default", "默认分组"),
                                              /**
                                               * 自定义分组
                                               */
                                              CUSTOM("custom", "自定义分组");

    @JsonValue
    private final String code;
    private final String desc;

    AppGroupType(String code, String desc) {
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
     * 获取类型
     *
     * @param code {@link String}
     * @return {@link AppGroupType}
     */
    @EnumConvert
    public static AppGroupType getType(String code) {
        AppGroupType[] values = values();
        for (AppGroupType status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        return null;
    }
}
