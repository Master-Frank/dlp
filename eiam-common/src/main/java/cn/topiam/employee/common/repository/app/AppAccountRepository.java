/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.app;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.topiam.employee.common.entity.app.AppAccountEntity;
import static cn.topiam.employee.common.constant.AppConstants.APP_ACCOUNT_CACHE_NAME;

/**
 * 应用账户
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/6/3 22:26
 */
@Repository
@CacheConfig(cacheNames = { APP_ACCOUNT_CACHE_NAME })
public interface AppAccountRepository extends JpaRepository<AppAccountEntity, String>,
                                      AppAccountRepositoryCustomized {
    /**
     * save
     *
     * @param entity must not be {@literal null}.
     * @param <S>    {@link S}
     * @return {@link AppAccountEntity}
     */
    @NotNull
    @Override
    @CacheEvict(allEntries = true)
    <S extends AppAccountEntity> S save(@NotNull S entity);

    /**
     * delete
     *
     * @param id must not be {@literal null}.
     */
    @CacheEvict(allEntries = true)
    @Override
    void deleteById(@NotNull String id);

    /**
     * 根据应用ID，用户ID查询应用账户list
     *
     * @param appId  {@link String}
     * @param userId {@link String}
     * @return {@link Optional}
     */
    @Cacheable(key = "#p0+':'+#p1", unless = "#result == null || #result.isEmpty()")
    List<AppAccountEntity> findByAppIdAndUserId(String appId, String userId);

    /**
     * 根据应用ID，用户ID查询默认应用账户
     *
     * @param appId  {@link String}
     * @param userId {@link String}
     * @return {@link Optional}
     */
    @Cacheable(key = "'default:'+#p0+':'+#p1", unless = "#result==null")
    Optional<AppAccountEntity> findByAppIdAndUserIdAndDefaultedIsTrue(String appId, String userId);

    /**
     * 根据应用ID，用户ID和Account查询应用账户
     *
     * @param appId  {@link String}
     * @param userId {@link String}
     * @param account {@link String}
     * @return {@link Optional}
     */
    @Cacheable(key = "#p0+':'+#p1+':'+#p2", unless = "#result==null")
    Optional<AppAccountEntity> findByAppIdAndUserIdAndAccount(String appId, String userId,
                                                              String account);

    /**
     * 根据appid删除所有的数据
     *
     * @param appId {@link String}
     */
    @CacheEvict(allEntries = true)
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteAllByAppId(@Param("appId") String appId);

    /**
     * 根据userId 删除用户数据
     *
     * @param userId {@link String}
     */
    @CacheEvict(allEntries = true)
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteByUserId(@Param("userId") String userId);
}
