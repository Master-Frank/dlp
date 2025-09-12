/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.topiam.employee.support.enums.BaseEnum;
import cn.topiam.employee.support.web.converter.EnumConvert;

/**
 * 同步状态
 *
 * @author TopIAM
 */
public enum SyncStatus implements BaseEnum {
                                            /**
                                             * 成功
                                             */
                                            SUCCESS("success", "成功"),
                                            /**
                                             * 失败
                                             */
                                            FAIL("fail", "失败"),
                                            /**
                                             * 跳过
                                             */
                                            SKIP("skip", "跳过"),
                                            /**
                                             * 同步中
                                             */
                                            PENDING("pending", "同步中");

    /**
     * code
     */
    @JsonValue
    private final String code;
    /**
     * desc
     */
    private final String desc;

    SyncStatus(String code, String desc) {
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
    public static SyncStatus getStatus(String code) {
        SyncStatus[] values = values();
        for (SyncStatus status : values) {
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
