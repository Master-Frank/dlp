/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.setting;

import java.util.List;

import cn.topiam.employee.console.pojo.result.setting.PasswordPolicyConfigResult;
import cn.topiam.employee.console.pojo.result.setting.WeakPasswordLibListResult;
import cn.topiam.employee.console.pojo.save.setting.PasswordPolicySaveParam;

/**
 * <p>
 * 密码策略 服务类
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-08-17
 */
public interface PasswordPolicyService extends SettingService {

    /**
     * 获取配置
     *
     * @return {@link PasswordPolicyConfigResult}
     */
    PasswordPolicyConfigResult getPasswordPolicyConfig();

    /**
     * 保存配置
     *
     * @param param {@link PasswordPolicySaveParam}
     * @return {@link Boolean}
     */
    Boolean savePasswordPolicyConfig(PasswordPolicySaveParam param);

    /**
     * 获取弱密码库
     *
     * @return {@link List}
     */
    List<WeakPasswordLibListResult> getWeakPasswordLibList();

}
