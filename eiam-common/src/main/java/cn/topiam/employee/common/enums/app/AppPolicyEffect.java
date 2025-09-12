/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.enums.app;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.topiam.employee.support.enums.BaseEnum;
import cn.topiam.employee.support.web.converter.EnumConvert;

/**
 * 规则效果
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/4 21:07
 */
public enum AppPolicyEffect implements BaseEnum {
                                                 /**
                                                  * 允许
                                                  */
                                                 Allow("ALLOW", "允许"),
                                                 /**
                                                  * 拒绝
                                                  */
                                                 Deny("DENY", "拒绝");

    /**
     * code
     */
    @JsonValue
    private final String code;
    /**
     * desc
     */
    private final String desc;

    AppPolicyEffect(String code, String desc) {
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
     * @return {@link AppPolicyEffect}
     */
    @EnumConvert
    public static AppPolicyEffect getType(String code) {
        AppPolicyEffect[] values = values();
        for (AppPolicyEffect status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        return null;
    }
}
