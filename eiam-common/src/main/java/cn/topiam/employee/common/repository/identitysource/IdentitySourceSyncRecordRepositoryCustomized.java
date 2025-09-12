/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.identitysource;

import java.util.List;

import cn.topiam.employee.common.entity.identitysource.IdentitySourceSyncRecordEntity;

/**
 * 身份源同步记录
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/15 21:35
 */
public interface IdentitySourceSyncRecordRepositoryCustomized {
    /**
     * 批量保存
     *
     * @param list {@link List}
     */
    void batchSave(List<IdentitySourceSyncRecordEntity> list);
}
