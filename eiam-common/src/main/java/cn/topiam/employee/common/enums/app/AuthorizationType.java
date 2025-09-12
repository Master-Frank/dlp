/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.enums.app;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.topiam.employee.support.enums.BaseEnum;
import cn.topiam.employee.support.web.converter.EnumConvert;

/**
 * SSO 授权类型
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/27 23:25
 */
public enum AuthorizationType implements BaseEnum {
                                                   /**
                                                    * 手动授权
                                                    */
                                                   AUTHORIZATION("authorization", "手动授权"),
                                                   /**
                                                    * 全员可访问
                                                    */
                                                   ALL_ACCESS("all_access", "全员可访问");

    /**
     * code
     */
    @JsonValue
    private final String code;
    /**
     * desc
     */
    private final String desc;

    AuthorizationType(String code, String desc) {
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
     * @return {@link AuthorizationType}
     */
    @EnumConvert
    public static AuthorizationType getType(String code) {
        AuthorizationType[] values = values();
        for (AuthorizationType status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
