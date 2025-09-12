/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.enums.app;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.topiam.employee.support.enums.BaseEnum;
import cn.topiam.employee.support.web.converter.EnumConvert;

/**
 * 权限策略客体类型
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/4 21:05
 */
public enum AppPolicyObjectType implements BaseEnum {
                                                     /**
                                                      * 角色
                                                      */
                                                     ROLE("ROLE", "角色"),
                                                     /**
                                                      * 权限
                                                      */
                                                     PERMISSION("PERMISSION", "权限"),
                                                     /**
                                                      * 资源
                                                      */
                                                     RESOURCE("RESOURCE", "资源");

    /**
     * code
     */
    @JsonValue
    private final String code;
    /**
     * desc
     */
    private final String desc;

    AppPolicyObjectType(String code, String desc) {
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
     * @return {@link AppPolicyObjectType}
     */
    @EnumConvert
    public static AppPolicyObjectType getType(String code) {
        AppPolicyObjectType[] values = values();
        for (AppPolicyObjectType status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        return null;
    }
}
