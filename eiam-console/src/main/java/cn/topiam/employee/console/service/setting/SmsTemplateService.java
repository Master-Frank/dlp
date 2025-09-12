/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.setting;

import java.util.List;

import cn.topiam.employee.common.enums.Language;
import cn.topiam.employee.console.pojo.result.setting.SmsTemplateListResult;

/**
 * <p>
 * 短信模版 服务类
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-08-13
 */
public interface SmsTemplateService extends SettingService {

    /**
     * 获取短信模版列表
     *
     * @param language {@link Language}
     * @return {@link SmsTemplateListResult}
     */
    List<SmsTemplateListResult> getSmsTemplateList(Language language);
}
