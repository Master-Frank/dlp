/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import cn.topiam.employee.common.entity.account.UserGroupEntity;

/**
 * <p>
 * 用户组表 Repository 接口
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-07-31
 */
@Repository
public interface UserGroupRepository extends JpaRepository<UserGroupEntity, String>,
                                     JpaSpecificationExecutor<UserGroupEntity> {

}
