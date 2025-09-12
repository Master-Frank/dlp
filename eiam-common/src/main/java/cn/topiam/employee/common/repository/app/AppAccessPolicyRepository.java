/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.app;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.topiam.employee.common.entity.app.AppAccessPolicyEntity;
import cn.topiam.employee.common.enums.app.AppPolicySubjectType;

/**
 * 应用授权策略 Repository
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/6/4 21:54
 */
@Repository
public interface AppAccessPolicyRepository extends JpaRepository<AppAccessPolicyEntity, String>,
                                           AppAccessPolicyRepositoryCustomized {
    /**
     * 根据应用ID删除所有数据
     *
     * @param appId {@link String}
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteAllByAppId(@Param("appId") String appId);

    /**
     * 根据主体ID删除所有数据
     *
     * @param subjectId {@link String}
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteAllBySubjectId(@Param("subjectId") String subjectId);

    /**
     * 根据主体ID和应用列表删除所有数据
     *
     * @param subjectId {@link String}
     * @param appIds {@link List <String>}
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteAllBySubjectIdAndAppIdIn(@Param("subjectId") String subjectId,
                                        @Param("appIds") List<String> appIds);

    /**
     * 根据应用ID、主体ID，主体类型查询
     *
     * @param appId {@link String}
     * @param subjectId {@link String}
     * @param subjectType {@link AppPolicySubjectType}
     * @return {@link AppAccessPolicyEntity}
     */
    Optional<AppAccessPolicyEntity> findByAppIdAndSubjectIdAndSubjectType(String appId,
                                                                          String subjectId,
                                                                          AppPolicySubjectType subjectType);

    /**
     * 修改应用访问授权状态
     *
     * @param id     {@link String}
     * @param enabled {@link Boolean}
     * @return {@link  Integer}
     */
    @Transactional(rollbackFor = Exception.class)
    @Modifying
    @Query(value = "UPDATE AppAccessPolicyEntity SET enabled = :enabled WHERE id = :id")
    Integer updateStatus(@Param(value = "id") String id, @Param(value = "enabled") Boolean enabled);
}
