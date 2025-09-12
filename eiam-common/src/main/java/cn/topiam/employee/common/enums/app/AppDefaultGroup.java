/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.enums.app;

import cn.topiam.employee.support.enums.BaseEnum;
import cn.topiam.employee.support.web.converter.EnumConvert;

/**
 * 应用默认分组
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/9/9 16:22
 */
public enum AppDefaultGroup implements BaseEnum {
                                                 /**
                                                  * 开发类
                                                  */
                                                 DEVELOPMENT("development", "开发类"),

                                                 /**
                                                  * 运维类
                                                  */
                                                 OPS("ops", "运维类"),

                                                 /**
                                                  * 办公类
                                                  */
                                                 OFFICE("office", "办公类");

    private final String code;
    private final String desc;

    AppDefaultGroup(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获取类型
     *
     * @param code {@link String}
     * @return {@link AppPolicyEffect}
     */
    @EnumConvert
    public static AppDefaultGroup getType(String code) {
        AppDefaultGroup[] values = values();
        for (AppDefaultGroup status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
