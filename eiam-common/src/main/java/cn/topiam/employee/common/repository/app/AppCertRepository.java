/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.app;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import cn.topiam.employee.common.entity.app.AppCertEntity;
import cn.topiam.employee.common.entity.app.AppOidcConfigEntity;
import cn.topiam.employee.common.enums.app.AppCertUsingType;
import static cn.topiam.employee.common.constant.AppConstants.APP_CERT_CACHE_NAME;

/**
 * AppCertificateRepository
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/31 21:52
 */
@CacheConfig(cacheNames = { APP_CERT_CACHE_NAME })
public interface AppCertRepository extends JpaRepository<AppCertEntity, String> {
    /**
     * 根据应用ID查询证书
     *
     * @param appId     {@link String}
     * @param usingType {@link AppCertUsingType}
     * @return {@link AppCertEntity}
     */
    @Cacheable(key = "#p1.code+':'+#p0", unless = "#result==null")
    Optional<AppCertEntity> findByAppIdAndUsingType(String appId, AppCertUsingType usingType);

    /**
     * save
     *
     * @param entity must not be {@literal null}.
     * @param <S>    {@link S}
     * @return {@link AppCertEntity}
     */
    @NotNull
    @Override
    @CacheEvict(allEntries = true)
    <S extends AppCertEntity> S save(@NotNull S entity);

    /**
     * delete
     *
     * @param id must not be {@literal null}.
     */
    @CacheEvict(allEntries = true)
    @Override
    void deleteById(@NotNull String id);

    /**
     * 根据应用ID删除应用
     *
     * @param appId {@link String}
     */
    @CacheEvict(allEntries = true)
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteByAppId(@Param("appId") String appId);

    /**
     * find by id
     *
     * @param id must not be {@literal null}.
     * @return {@link AppOidcConfigEntity}
     */
    @NotNull
    @Override
    @Cacheable
    Optional<AppCertEntity> findById(@NotNull String id);
}
