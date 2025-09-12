/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.topiam.employee.support.enums.BaseEnum;
import cn.topiam.employee.support.web.converter.EnumConvert;

/**
 * 用户状态
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/7/31 22:50
 */
public enum UserStatus implements BaseEnum {
                                            /**
                                             * 已启用
                                             */
                                            ENABLED("enabled", "启用"),

                                            /**
                                            * 已禁用
                                            */
                                            DISABLED("disabled", "禁用"),

                                            /**
                                             * 登录失败锁定
                                             */
                                            LOCKED("locked", "锁定"),

                                            /**
                                             * 过期锁定
                                             */
                                            EXPIRED_LOCKED("expired_locked", "过期锁定"),

                                            /**
                                            * 密码过期锁定
                                            */
                                            PASSWORD_EXPIRED_LOCKED("password_expired_locked",
                                                                    "密码过期锁定");

    /**
     * code
     */
    @JsonValue
    private final String code;
    /**
     * desc
     */
    private final String desc;

    UserStatus(String code, String desc) {
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
    public static UserStatus getStatus(String code) {
        UserStatus[] values = values();
        for (UserStatus status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return code;
    }

}
