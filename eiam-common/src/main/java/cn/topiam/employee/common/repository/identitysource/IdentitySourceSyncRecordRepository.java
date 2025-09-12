/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.identitysource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import cn.topiam.employee.common.entity.identitysource.IdentitySourceSyncRecordEntity;

/**
 * 身份源同步详情
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/15 21:35
 */
@Repository
public interface IdentitySourceSyncRecordRepository extends
                                                    JpaRepository<IdentitySourceSyncRecordEntity, String>,
                                                    JpaSpecificationExecutor<IdentitySourceSyncRecordEntity>,
                                                    IdentitySourceSyncRecordRepositoryCustomized {
}
