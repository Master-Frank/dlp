/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.setting;

import java.util.List;

import cn.topiam.employee.common.entity.setting.MailTemplateEntity;
import cn.topiam.employee.common.enums.MailType;
import cn.topiam.employee.console.pojo.result.setting.EmailTemplateListResult;
import cn.topiam.employee.console.pojo.result.setting.EmailTemplateResult;
import cn.topiam.employee.console.pojo.save.setting.EmailCustomTemplateSaveParam;

/**
 * <p>
 * 邮件模板 服务类
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-08-13
 */
public interface MailTemplateService extends SettingService {
    /**
     * 根据邮件模板类型获取配置
     *
     * @param type {@link MailType}
     * @return SettingMailTemplateEntity
     */
    MailTemplateEntity getEmailTemplateByType(MailType type);

    /**
     * 添加邮件模板
     *
     * @param type  {@link MailType}
     * @param param {@link EmailCustomTemplateSaveParam}
     * @return SettingMailTemplateEntity
     */
    MailTemplateEntity saveCustomEmailTemplate(MailType type, EmailCustomTemplateSaveParam param);

    /**
     * 邮件模板详情
     *
     * @param templateType {@link MailType}
     * @return {@link EmailTemplateResult}
     */
    EmailTemplateResult getEmailTemplate(MailType templateType);

    /**
     * 获取邮件模板列表
     *
     * @return {@link List}
     */
    List<EmailTemplateListResult> getEmailTemplateList();

    /**
     * 禁用自定义模块
     *
     * @param type {@link MailType}
     */
    void disableCustomEmailTemplate(MailType type);
}
