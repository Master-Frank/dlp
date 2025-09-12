/*
 * eiam-openapi - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.openapi.service;

import cn.topiam.employee.openapi.pojo.query.OapiV1AppAccountQuery;
import cn.topiam.employee.openapi.pojo.result.AppAccountListResult;
import cn.topiam.employee.openapi.pojo.save.AppAccountCreateParam;
import cn.topiam.employee.support.repository.page.domain.Page;
import cn.topiam.employee.support.repository.page.domain.PageModel;

/**
 * 应用账户
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/6/4 19:07
 */
public interface AppAccountService {

    /**
     * 查询应用账户
     *
     * @param pageModel {@link PageModel}
     * @param query     {@link  OapiV1AppAccountQuery}
     * @return {@link Page}
     */
    Page<AppAccountListResult> getAppAccountList(PageModel pageModel, OapiV1AppAccountQuery query);

    /**
     * 新增应用账户
     *
     * @param param {@link AppAccountCreateParam}
     * @return {@link Boolean}
     */
    Boolean createAppAccount(AppAccountCreateParam param);

    /**
     * 删除应用账户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean deleteAppAccount(String id);
}
