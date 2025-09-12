/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.enums.app;

import com.fasterxml.jackson.annotation.JsonValue;

import cn.topiam.employee.support.enums.BaseEnum;
import cn.topiam.employee.support.web.converter.EnumConvert;

/**
 * 权限策略主体类型
 * 用户、角色、组织机构
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/4 21:05
 */
public enum AppPolicySubjectType implements BaseEnum {
                                                      /**
                                                       * 角色
                                                       */
                                                      ROLE("ROLE", "角色"),
                                                      /**
                                                       * 用户
                                                       */
                                                      USER("USER", "用户"),
                                                      /**
                                                       * 组织机构
                                                       */
                                                      ORGANIZATION("ORGANIZATION", "组织机构"),
                                                      /**
                                                       * 分组
                                                       */
                                                      USER_GROUP("USER_GROUP", "分组"),
                                                      /**
                                                       * TODO 客户端
                                                       */
                                                      CLIENT("CLIENT", "客户端");

    /**
     * code
     */
    @JsonValue
    private final String code;
    /**
     * desc
     */
    private final String desc;

    AppPolicySubjectType(String code, String desc) {
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
     * @return {@link AppPolicySubjectType}
     */
    @EnumConvert
    public static AppPolicySubjectType getType(String code) {
        AppPolicySubjectType[] values = values();
        for (AppPolicySubjectType status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        return null;
    }
}
