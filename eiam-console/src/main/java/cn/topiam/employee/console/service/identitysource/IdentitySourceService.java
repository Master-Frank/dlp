/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.identitysource;

import java.util.List;

import cn.topiam.employee.common.entity.identitysource.IdentitySourceEntity;
import cn.topiam.employee.console.pojo.other.IdentitySourceConfigValidatorParam;
import cn.topiam.employee.console.pojo.query.identity.IdentitySourceListQuery;
import cn.topiam.employee.console.pojo.result.identitysource.IdentitySourceListResult;
import cn.topiam.employee.console.pojo.save.identitysource.IdentitySourceConfigSaveParam;
import cn.topiam.employee.console.pojo.save.identitysource.IdentitySourceCreateParam;
import cn.topiam.employee.console.pojo.save.identitysource.IdentitySourceCreateResult;
import cn.topiam.employee.console.pojo.update.identity.IdentitySourceUpdateParam;
import cn.topiam.employee.support.repository.page.domain.Page;
import cn.topiam.employee.support.repository.page.domain.PageModel;

/**
 * <p>
 * 身份源配置 服务类
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-08-16
 */
public interface IdentitySourceService {

    /**
     * 身份源列表
     *
     * @param query     {@link  IdentitySourceListQuery}
     * @param pageModel {@link  PageModel}
     * @return {@link List}
     */
    Page<IdentitySourceListResult> getIdentitySourceList(IdentitySourceListQuery query,
                                                         PageModel pageModel);

    /**
     * 身份源详情
     *
     * @param id {@link String}
     * @return {@link IdentitySourceEntity}
     */
    IdentitySourceEntity getIdentitySource(String id);

    /**
     * 创建身份源
     *
     * @param param {@link IdentitySourceCreateParam}
     * @return {@link IdentitySourceCreateResult}
     */
    IdentitySourceCreateResult createIdentitySource(IdentitySourceCreateParam param);

    /**
     * 修改身份源
     *
     * @param param {@link IdentitySourceUpdateParam}
     * @return {@link Boolean}
     */
    Boolean updateIdentitySource(IdentitySourceUpdateParam param);

    /**
     * 禁用身份源
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean disableIdentitySource(String id);

    /**
     * 启用身份源
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    Boolean enableIdentitySource(String id);

    /**
     * 删除身份源
     *
     * @param id {@link  String}
     * @return {@link  Boolean}
     */
    Boolean deleteIdentitySource(String id);

    /**
     * 保存身份源配置
     *
     * @param param {@link  IdentitySourceConfigSaveParam}
     * @return {@link  Boolean}
     */
    Boolean saveIdentitySourceConfig(IdentitySourceConfigSaveParam param);

    /**
     * 更新身份源策略
     *
     * @param id             {@link Long} 主键
     * @param strategyConfig {@link String} 策略
     */
    void updateStrategyConfig(String id, String strategyConfig);

    /**
     * 身份源配置验证
     *
     * @param param {@link  IdentitySourceConfigValidatorParam}
     * @return {@link  Boolean}
     */
    Boolean identitySourceConfigValidator(IdentitySourceConfigValidatorParam param);
}
