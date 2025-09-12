/*
 * eiam-authentication-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.authentication.common;

import java.util.List;

import com.google.common.collect.Lists;

import cn.topiam.employee.support.enums.BaseEnum;
import cn.topiam.employee.support.web.converter.EnumConvert;

/**
 * 身份源类型
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/21 23:07
 */
public enum IdentityProviderCategory implements BaseEnum {
                                                          /**
                                                           * 社交
                                                           */
                                                          social("social", "社交", Lists.newArrayList(
                                                              IdentityProviderType.QQ_OAUTH,
                                                              IdentityProviderType.WECHAT,
                                                              IdentityProviderType.GITEE_OAUTH,
                                                              IdentityProviderType.GITHUB_OAUTH,
                                                              IdentityProviderType.ALIPAY_OAUTH)),
                                                          /**
                                                           * 企业
                                                           */
                                                          enterprise("enterprise", "企业", Lists
                                                              .newArrayList(
                                                                  IdentityProviderType.WECHAT_WORK_OAUTH,
                                                                  IdentityProviderType.DINGTALK_OAUTH,
                                                                  IdentityProviderType.FEISHU_OAUTH));

    private final String                     code;

    private final String                     desc;

    private final List<IdentityProviderType> providers;

    IdentityProviderCategory(String code, String desc, List<IdentityProviderType> providers) {
        this.code = code;
        this.desc = desc;
        this.providers = providers;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    public List<IdentityProviderType> getProviders() {
        return providers;
    }

    /**
     * 获取类型
     *
     * @param code {@link String}
     * @return {@link IdentityProviderCategory}
     */
    @EnumConvert
    public static IdentityProviderCategory getType(String code) {
        IdentityProviderCategory[] values = values();
        for (IdentityProviderCategory status : values) {
            if (String.valueOf(status.getCode()).equals(code)) {
                return status;
            }
        }
        throw new NullPointerException("未获取到对应平台");
    }
}
