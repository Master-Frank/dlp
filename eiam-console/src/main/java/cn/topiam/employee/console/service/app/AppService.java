/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.app;

import java.util.Map;

import cn.topiam.employee.console.pojo.query.app.AppQuery;
import cn.topiam.employee.console.pojo.result.app.AppCreateResult;
import cn.topiam.employee.console.pojo.result.app.AppGetResult;
import cn.topiam.employee.console.pojo.result.app.AppListResult;
import cn.topiam.employee.console.pojo.save.app.AppCreateParam;
import cn.topiam.employee.console.pojo.update.app.AppSaveConfigParam;
import cn.topiam.employee.console.pojo.update.app.AppUpdateParam;
import cn.topiam.employee.support.repository.page.domain.Page;
import cn.topiam.employee.support.repository.page.domain.PageModel;

/**
 * <p>
 * 应用管理 服务类
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-07-31
 */
public interface AppService {

    /**
     * 获取应用（分页）
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link AppQuery}
     * @return {@link AppListResult}
     */
    Page<AppListResult> getAppList(PageModel pageModel,
                                   cn.topiam.employee.console.pojo.query.app.AppQuery query);

    /**
     * 创建应用
     *
     * @param param {@link AppCreateParam}
     * @return {@link AppCreateResult}
     */
    AppCreateResult createApp(AppCreateParam param);

    /**
     * 修改应用
     *
     * @param param {@link AppUpdateParam}
     * @return {@link Boolean}
     */
    boolean updateApp(AppUpdateParam param);

    /**
     * 删除应用
     *
     * @param id {@link  Long}
     * @return {@link Boolean}
     */
    boolean deleteApp(String id);

    /**
     * 获取单个应用详情
     *
     * @param id {@link Long}
     * @return {@link AppGetResult}
     */
    AppGetResult getApp(String id);

    /**
     * 启用应用
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean enableApp(String id);

    /**
     * 禁用应用
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean disableApp(String id);

    /**
     *  更新应用配置
     *
     * @param param {@link AppSaveConfigParam}
     * @return {@link Boolean}
     */
    Boolean saveAppConfig(AppSaveConfigParam param);

    /**
     * 获取应用配置
     *
     * @param appId {@link String}
     * @return {@link Map}
     */
    Object getAppConfig(String appId);
}
