/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.topiam.employee.support.enums.BaseEnum;
import cn.topiam.employee.support.web.converter.EnumConvert;

/**
 * 语言
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/8/9 21:04
 */
public enum Language implements BaseEnum {
                                          /**
                                           * 英语
                                           */
                                          EN("en", "英语"),
                                          /**
                                           * 中文
                                           */
                                          ZH("zh", "中文");

    /**
     * code
     */
    @JsonValue
    private final String locale;
    /**
     * desc
     */
    private final String desc;

    Language(String locale, String desc) {
        this.locale = locale;
        this.desc = desc;
    }

    public String getLocale() {
        return locale;
    }

    @Override
    public String getCode() {
        return locale;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    /**
     * 获取类型
     *
     * @param code {@link String}
     * @return {@link Language}
     */
    @EnumConvert
    public static Language getType(String code) {
        Language[] values = values();
        for (Language status : values) {
            if (String.valueOf(status.getLocale()).equals(code)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.getLocale();
    }

}
