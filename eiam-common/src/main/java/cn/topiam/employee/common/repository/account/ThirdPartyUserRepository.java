/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.topiam.employee.common.entity.account.ThirdPartyUserEntity;

/**
 * 三方用户表
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/11/30 22:18
 */
@Repository
public interface ThirdPartyUserRepository extends JpaRepository<ThirdPartyUserEntity, String> {

    Optional<ThirdPartyUserEntity> findByOpenIdAndIdpId(String openId, String idpId);
}
