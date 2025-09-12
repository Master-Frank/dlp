/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.setting;

import cn.topiam.employee.console.pojo.result.setting.SecurityDefensePolicyConfigResult;
import cn.topiam.employee.console.pojo.save.setting.SecurityDefensePolicyParam;

/**
 * 安全策略
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023-03-09
 */
public interface SecurityDefensePolicyService {

    /**
     * 保存安全防御策略
     *
     * @param param {@link SecurityDefensePolicyParam}
     * @return {@link Boolean}
     */
    Boolean saveSecurityDefensePolicyConfig(SecurityDefensePolicyParam param);

    /**
     * 获取安全防御策略
     * @return {@link SecurityDefensePolicyConfigResult}
     */
    SecurityDefensePolicyConfigResult getSecurityPolicyConfig();
}
