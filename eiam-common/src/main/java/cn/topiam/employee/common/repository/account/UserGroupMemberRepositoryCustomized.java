/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.topiam.employee.common.entity.account.po.UserPO;
import cn.topiam.employee.common.entity.account.query.UserGroupMemberListQuery;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/13 21:27
 */
public interface UserGroupMemberRepositoryCustomized {
    /**
     * 获取用户组成员列表
     *
     * @param query    {@link UserGroupMemberListQuery}
     * @param pageable {@link Pageable}
     * @return {@link Page}
     */
    Page<UserPO> getUserGroupMemberList(UserGroupMemberListQuery query, Pageable pageable);
}
