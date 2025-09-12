/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.enums.app;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.topiam.employee.support.enums.BaseEnum;
import cn.topiam.employee.support.web.converter.EnumConvert;

/**
 * Form用户名，密码加密类型
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/02/15
 */
public enum FormEncryptType implements BaseEnum {
                                                 /**
                                                  * aes
                                                  */
                                                 AES("aes", "AES加密"),
                                                 /**
                                                  * MD5编码
                                                  */
                                                 MD5("md5", "MD5编码"),
                                                 /**
                                                  * bas64
                                                  */
                                                 BASE64("base64", "BASE64编码");

    /**
     * code
     */
    @JsonValue
    private final String code;
    /**
     * desc
     */
    private final String desc;

    FormEncryptType(String code, String desc) {
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
     * @return {@link InitLoginType}
     */
    @EnumConvert
    public static FormEncryptType getType(String code) {
        if (code == null) {
            return null;
        }
        FormEncryptType[] values = values();
        for (FormEncryptType status : values) {
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
