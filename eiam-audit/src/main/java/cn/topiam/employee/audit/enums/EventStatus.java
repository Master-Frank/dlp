/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.topiam.employee.support.web.converter.EnumConvert;

import lombok.Getter;

/**
 * 事件状态
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/11/6 21:57
 */
@Getter
public enum EventStatus {
                         /**
                          * 成功
                          */
                         SUCCESS("success", "成功"),
                         /**
                          * 失败
                          */
                         FAIL("fail", "失败");

    @JsonValue
    private final String code;
    private final String desc;

    EventStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获取类型
     *
     * @param code {@link String}
     * @return {@link EventStatus}
     */
    @EnumConvert
    public static EventStatus getType(String code) {
        EventStatus[] values = values();
        for (EventStatus status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        return null;
    }
}
