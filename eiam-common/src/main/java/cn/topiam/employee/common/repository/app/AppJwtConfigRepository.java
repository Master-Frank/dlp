/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.app;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.topiam.employee.common.entity.app.AppJwtConfigEntity;
import cn.topiam.employee.common.entity.app.po.AppFormConfigPO;
import cn.topiam.employee.common.entity.app.po.AppJwtConfigPO;
import static cn.topiam.employee.common.constant.AppConstants.JWT_CONFIG_CACHE_NAME;

/**
 * @author TopIAM
 */
@Repository
@CacheConfig(cacheNames = { JWT_CONFIG_CACHE_NAME })
public interface AppJwtConfigRepository extends JpaRepository<AppJwtConfigEntity, String> {
    /**
     * 按应用 ID 删除
     *
     * @param appId {@link String}
     */
    @CacheEvict(allEntries = true)
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteByAppId(@Param("appId") String appId);

    /**
     * delete
     *
     * @param id must not be {@literal null}.
     */
    @CacheEvict(allEntries = true)
    @Override
    void deleteById(@NotNull String id);

    /**
     * save
     *
     * @param entity must not be {@literal null}.
     * @param <S>    {@link S}
     * @return {@link AppJwtConfigEntity}
     */
    @NotNull
    @Override
    @CacheEvict(allEntries = true)
    <S extends AppJwtConfigEntity> S save(@NotNull S entity);

    /**
     * 根据应用ID获取配置
     *
     * @param appId {@link String}
     * @return {@link AppJwtConfigEntity}
     */
    Optional<AppJwtConfigEntity> findByAppId(String appId);

    /**
     * 根据应用ID获取
     *
     * @param appId {@link String}
     * @return {@link AppFormConfigPO}
     */
    @Query(value = "SELECT NEW cn.topiam.employee.common.entity.app.po.AppJwtConfigPO(jc,a.code,a.name,a.template,a.clientId,a.clientSecret,a.initLoginUrl,a.authorizationType,a.enabled,a.configured) FROM AppEntity a INNER JOIN AppJwtConfigEntity jc ON a.id = jc.appId WHERE a.id =:appId")
    AppJwtConfigPO getByAppId(String appId);

    /**
     * 根据应用编码查询应用配置
     *
     * @param appCode {@link String}
     * @return {@link AppFormConfigPO}
     */
    @Query(value = "SELECT NEW cn.topiam.employee.common.entity.app.po.AppJwtConfigPO(jc,a.code,a.name,a.template,a.clientId,a.clientSecret,a.initLoginUrl,a.authorizationType,a.enabled,a.configured) FROM AppEntity a INNER JOIN AppJwtConfigEntity jc ON a.id = jc.appId WHERE a.code =:appCode")
    AppJwtConfigPO findByAppCode(String appCode);
}
