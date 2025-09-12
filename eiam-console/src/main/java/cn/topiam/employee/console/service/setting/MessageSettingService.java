/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.setting;

import cn.topiam.employee.console.pojo.result.setting.EmailProviderConfigResult;
import cn.topiam.employee.console.pojo.result.setting.SmsProviderConfigResult;
import cn.topiam.employee.console.pojo.save.setting.MailProviderSaveParam;
import cn.topiam.employee.console.pojo.save.setting.SmsProviderSaveParam;

/**
 * 消息设置接口
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/10/1 21:19
 */
public interface MessageSettingService extends SettingService {
    /**
     * 保存配置
     *
     * @param param {@link MailProviderSaveParam}
     * @return {@link Boolean}
     */
    Boolean saveMailProviderConfig(MailProviderSaveParam param);

    /**
     * 保存邮件验证配置
     *
     * @param param {@link SmsProviderSaveParam}
     * @return {@link Boolean}
     */
    Boolean saveSmsProviderConfig(SmsProviderSaveParam param);

    /**
     * 禁用短信验证服务
     *
     * @return {@link Boolean}
     */
    Boolean disableSmsProvider();

    /**
     * 禁用邮件提供商
     *
     * @return {@link Boolean}
     */
    Boolean disableMailProvider();

    /**
     * 获取邮件提供商配置
     *
     * @return {@link EmailProviderConfigResult}
     */
    EmailProviderConfigResult getMailProviderConfig();

    /**
     * 获取短信验证服务配置
     *
     * @return {@link SmsProviderConfigResult}
     */
    SmsProviderConfigResult getSmsProviderConfig();
}
