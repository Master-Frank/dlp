/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.account.impl;

import java.util.Optional;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Repository;

import cn.topiam.employee.common.entity.account.po.UserIdpBindPO;
import cn.topiam.employee.common.repository.account.UserIdpRepositoryCustomized;
import cn.topiam.employee.support.repository.aspect.query.QuerySingleResult;
import cn.topiam.employee.support.repository.page.domain.Page;

import lombok.AllArgsConstructor;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import static cn.topiam.employee.common.constant.AccountConstants.USER_CACHE_NAME;

/**
 * UserIdp Repository Customized
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/12/29 21:27
 */
@Repository
@CacheConfig(cacheNames = { USER_CACHE_NAME })
@AllArgsConstructor
public class UserIdpRepositoryCustomizedImpl implements UserIdpRepositoryCustomized {

    /**
     * 根据ID查询
     *
     * @param id {@link  String}
     * @return {@link Optional}
     */
    @Override
    @QuerySingleResult
    public Optional<UserIdpBindPO> selectById(String id) {
        String hql = """
                SELECT
                    NEW cn.topiam.employee.common.entity.account.po.UserIdpBindPO(uib.id,user.id,user.username,user.nickName,user.email,user.phone,user.phoneAreaCode,user.avatar,tpu.openId,tpu.unionId,idp.id,idp.name,idp.type,uib.bindTime)
                FROM
                    UserIdpBindEntity uib
                    LEFT JOIN UserEntity user ON uib.userId = user.id
                    LEFT JOIN ThirdPartyUserEntity tpu ON uib.thirdPartyUser.id = tpu.id
                    LEFT JOIN IdentityProviderEntity idp ON tpu.idpId = idp.id
                WHERE
                    uib.id =:id
                """;
        TypedQuery<UserIdpBindPO> query = entityManager.createQuery(hql, UserIdpBindPO.class);
        query.setParameter("id", id);
        return Optional.of(query.getSingleResult());
    }

    /**
     * 根据身份源ID和openId查询
     *
     * @param idpId  {@link  String}
     * @param openId {@link  String}
     * @return {@link Optional}
     */
    @Override
    @QuerySingleResult
    public Optional<UserIdpBindPO> findByIdpIdAndOpenId(String idpId, String openId) {
        String hql = """
                SELECT
                    NEW cn.topiam.employee.common.entity.account.po.UserIdpBindPO(uib.id,user.id,user.username,user.nickName,user.email,user.phone,user.phoneAreaCode,user.avatar,tpu.openId,tpu.unionId,idp.id,idp.name,idp.type,uib.bindTime)
                FROM
                    UserIdpBindEntity uib
                    LEFT JOIN UserEntity user ON uib.userId = user.id
                    LEFT JOIN ThirdPartyUserEntity tpu ON uib.thirdPartyUser.id = tpu.id
                    LEFT JOIN IdentityProviderEntity idp ON tpu.idpId = idp.id
                WHERE
                    tpu.idpId =:idpId
                    AND tpu.openId =:openId
                """;
        TypedQuery<UserIdpBindPO> query = entityManager.createQuery(hql, UserIdpBindPO.class);
        query.setParameter("idpId", idpId);
        query.setParameter("openId", openId);
        return Optional.of(query.getSingleResult());
    }

    /**
     * 根据身份源ID和userId查询
     *
     * @param idpId  {@link  String}
     * @param userId {@link  String}
     * @return {@link Optional}
     */
    @Override
    @QuerySingleResult
    public Optional<UserIdpBindPO> findByIdpIdAndUserId(String idpId, String userId) {
        String hql = """
                SELECT
                    NEW cn.topiam.employee.common.entity.account.po.UserIdpBindPO(uib.id,user.id,user.username,user.nickName,user.email,user.phone,user.phoneAreaCode,user.avatar,tpu.openId,tpu.unionId,idp.id,idp.name,idp.type,uib.bindTime)
                FROM
                    UserIdpBindEntity uib
                    LEFT JOIN UserEntity user ON uib.userId = user.id
                    LEFT JOIN ThirdPartyUserEntity tpu ON uib.thirdPartyUser.id = tpu.id
                    LEFT JOIN IdentityProviderEntity idp ON tpu.idpId = idp.id
                WHERE
                     uib.userId =:userId
                     AND tpu.idpId =:idpId
                """;
        TypedQuery<UserIdpBindPO> query = entityManager.createQuery(hql, UserIdpBindPO.class);
        query.setParameter("userId", userId);
        query.setParameter("idpId", idpId);
        return Optional.of(query.getSingleResult());
    }

    /**
     * 查询用户身份提供商绑定
     *
     * @param userId {@link  String}
     * @return {@link Page}
     */
    @Override
    public Iterable<UserIdpBindPO> getUserIdpBindList(String userId) {
        String hql = """
                SELECT
                    NEW cn.topiam.employee.common.entity.account.po.UserIdpBindPO(uib.id,user.id,user.username,user.nickName,user.email,user.phone,user.phoneAreaCode,user.avatar,uib.thirdPartyUser.openId,uib.thirdPartyUser.unionId,idp.id,idp.name,idp.type,uib.bindTime)
                FROM
                    UserIdpBindEntity uib
                    LEFT JOIN UserEntity user ON uib.userId = user.id
                    LEFT JOIN IdentityProviderEntity idp ON uib.thirdPartyUser.idpId = idp.id
                WHERE
                    uib.userId =:userId
                """;
        TypedQuery<UserIdpBindPO> query = entityManager.createQuery(hql, UserIdpBindPO.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    /**
     * EntityManager
     */
    private final EntityManager entityManager;
}
