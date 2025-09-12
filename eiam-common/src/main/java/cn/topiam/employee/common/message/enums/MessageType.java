/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.message.enums;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.topiam.employee.support.web.converter.EnumConvert;

/**
 * 消息类型
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/25 21:19
 */
public enum MessageType implements Serializable {
                                                 /**
                                                  * 邮件
                                                  */
                                                 MAIL("mail", "邮件"),
                                                 /**
                                                  * 短信
                                                  */
                                                 SMS("sms", "短信");

    /**
     * code
     */
    @JsonValue
    private final String code;
    /**
     * desc
     */
    private final String desc;

    MessageType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 获取类型
     *
     * @param code {@link String}
     * @return {@link String}
     */
    @EnumConvert
    public static MessageType getType(String code) {
        MessageType[] values = values();
        for (MessageType status : values) {
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
