/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.account;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.topiam.employee.common.entity.account.ThirdPartyUserEntity;
import cn.topiam.employee.common.entity.account.UserIdpBindEntity;

/**
 * 用户身份绑定表
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/4/3 22:18
 */
@Repository
public interface UserIdpRepository extends JpaRepository<UserIdpBindEntity, String>,
                                   UserIdpRepositoryCustomized {

    long countByThirdPartyUser(ThirdPartyUserEntity thirdPartyUserEntity);

    /**
     * 根据用户id删除
     *
     * @param userIds {@link String}
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteAllByUserIdIn(Collection<String> userIds);
}
