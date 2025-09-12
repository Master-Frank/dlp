/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.account;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.topiam.employee.common.entity.account.UserEntity;
import cn.topiam.employee.common.entity.account.po.UserPO;
import cn.topiam.employee.common.entity.account.query.UserListNotInGroupQuery;
import cn.topiam.employee.common.entity.account.query.UserListQuery;

/**
 * User Repository Customized
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/12/29 21:27
 */
public interface UserRepositoryCustomized {
    /**
     * 获取用户列表
     *
     * @param pageable {@link  Pageable}
     * @param query    {@link  UserListQuery}
     * @return {@link Page}
     */
    Page<UserPO> getUserList(UserListQuery query, Pageable pageable);

    /**
     * 获取用户组不存在成员列表
     *
     * @param query    {@link UserListNotInGroupQuery}
     * @param pageable {@link Pageable}
     * @return {@link Page}
     */
    Page<UserPO> getUserListNotInGroupId(UserListNotInGroupQuery query, Pageable pageable);

    /**
     * 不在组织下和数据来源查找用户列表
     *
     * @param identitySourceId {@link String}
     * @return {@link List}
     */
    List<UserEntity> findAllByOrgIdNotExistAndIdentitySourceId(String identitySourceId);

    /**
     * 批量新增
     *
     * @param list {@link List}
     */
    void batchSave(List<UserEntity> list);

    /**
     * 批量更新
     *
     * @param list {@link List}
     */
    void batchUpdate(List<UserEntity> list);
}
