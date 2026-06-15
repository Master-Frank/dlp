/*
 * ulp-common - United Login Platform
 * Copyright (c) 2022-Present Frank Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.frank.ulp.common.repository.account;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.common.entity.account.OrganizationMemberEntity;
import cn.frank.ulp.common.entity.account.po.OrganizationMemberPO;

/**
 * 组织机构成员
 *
 * @author Frank Zhang
 */
@Repository
public interface OrganizationMemberRepository extends
                                              JpaRepository<OrganizationMemberEntity, String>,
                                              OrganizationMemberCustomizedRepository {

    /**
     * 根据组织机构ID和用户ID删除
     *
     * @param orgId  {@link String}
     * @param userId {@link String}
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteByOrgIdAndUserId(String orgId, String userId);

    /**
     * 根据根据组织id查询关联的用户
     *
     * @param orgId {@link String}
     * @return {@link List}
     */
    List<OrganizationMemberEntity> findAllByOrgId(String orgId);

    /**
     * 根据根据用户id查询关联的用户
     *
     * @param userId {@link String}
     * @return {@link List}
     */
    List<OrganizationMemberEntity> findAllByUserId(String userId);

    /**
     * 根据用户ID 批量删除关联关系
     *
     * @param userIds {@link String}
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteAllByUserIdIn(Collection<String> userIds);

    /**
     * 根据用户ID 删除关联关系
     *
     * @param id {@link String}
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteByUserId(@Param("id") String id);

    /**
     * 根据根据用户id查询关联的组织
     *
     * @param userId {@link String}
     * @return {@link List}
     */
    @Query(value = "SELECT NEW cn.frank.ulp.common.entity.account.po.OrganizationMemberPO(om.id,om.userId,om.orgId,o.name,o.displayPath) FROM OrganizationMemberEntity om LEFT JOIN OrganizationEntity o ON o.id= om.orgId WHERE om.userId =:userId")
    List<OrganizationMemberPO> findAllPoByUserId(String userId);

    /**
     * 查询用户所属的全部组织 ID（用于 MFA 强制策略判定）
     *
     * @param userId {@link String}
     * @return 组织 ID 列表
     */
    @Query("SELECT om.orgId FROM OrganizationMemberEntity om WHERE om.userId = :userId")
    List<String> findOrgIdsByUserId(@Param("userId") String userId);
}
