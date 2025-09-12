/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.app;

import cn.topiam.employee.console.pojo.query.app.AppAccessPolicyQuery;
import cn.topiam.employee.console.pojo.result.app.AppAccessPolicyResult;
import cn.topiam.employee.console.pojo.save.app.AppAccessPolicyCreateParam;
import cn.topiam.employee.support.repository.page.domain.Page;
import cn.topiam.employee.support.repository.page.domain.PageModel;

/**
 * 应用访问权限策略 Service
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/6/4 21:55
 */
public interface AppAccessPolicyService {

    /**
     * 查询应用授权策略列表
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link AppAccessPolicyQuery}
     * @return {@link Page}
     */
    Page<AppAccessPolicyResult> getAppAccessPolicyList(PageModel pageModel,
                                                       AppAccessPolicyQuery query);

    /**
     * 创建应用授权策略
     *
     * @param param {@link AppAccessPolicyCreateParam}
     * @return @{link Boolean}
     */
    Boolean createAppAccessPolicy(AppAccessPolicyCreateParam param);

    /**
     * 删除应用授权策略
     *
     * @param id {@link  String}
     * @return {@link Boolean}
     */
    Boolean deleteAppAccessPolicy(String id);

    /**
     * 启用应用访问授权
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean enableAppAccessPolicy(String id);

    /**
     * 禁用应用访问授权
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean disableAppAccessPolicy(String id);
}
