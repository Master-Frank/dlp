/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.account;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import cn.topiam.employee.common.entity.account.UserGroupMemberEntity;

/**
 * 用户组成员
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/30 03:04
 */
@Repository
public interface UserGroupMemberRepository extends JpaRepository<UserGroupMemberEntity, String>,
                                           UserGroupMemberRepositoryCustomized {

    /**
     * 根据用户组ID和用户ID删除
     *
     * @param groupId {@link String}
     * @param userId  {@link String}
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteByGroupIdAndUserId(@Param("groupId") String groupId, @Param("userId") String userId);

    /**
     * 根据用户id所有用户组关联信息
     *
     * @param userId {@link String}
     *
     * @return {@link List}
     */
    List<UserGroupMemberEntity> findByUserId(@Param("userId") String userId);

    /**
     * 根据用户组id查询所有用户组关联信息
     *
     * @param groupId {@link String}
     *
     * @return {@link List}
     */
    List<UserGroupMemberEntity> findByGroupId(String groupId);

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
     * 根据用户组ID 删除关联关系
     *
     * @param groupId {@link String}
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteByGroupId(@Param("groupId") String groupId);
}
