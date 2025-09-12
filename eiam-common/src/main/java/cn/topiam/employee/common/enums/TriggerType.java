/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.topiam.employee.support.enums.BaseEnum;
import cn.topiam.employee.support.web.converter.EnumConvert;

/**
 * 触发类型
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/9 23:47
 */
public enum TriggerType implements BaseEnum {
                                             /**
                                              * 手动触发
                                              */
                                             MANUAL("manual", "手动触发"),
                                             /**
                                              * 任务触发
                                              */
                                             JOB("job", "任务触发");

    @JsonValue
    private final String code;
    private final String desc;

    TriggerType(String code, String desc) {
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
     * @return {@link TriggerType}
     */
    @EnumConvert
    public static TriggerType getType(String code) {
        TriggerType[] values = values();
        for (TriggerType status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        return null;
    }
}
