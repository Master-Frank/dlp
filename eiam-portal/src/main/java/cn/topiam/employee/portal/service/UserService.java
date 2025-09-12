/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.service;

import java.util.Optional;

import cn.topiam.employee.common.entity.account.UserEntity;
import cn.topiam.employee.support.security.userdetails.UserDetails;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2024/3/2 22:21
 */
public interface UserService {
    /**
     * 获取用户详情
     *
     * @return {@link UserDetails}
     */
    UserDetails getUserDetails(String userId);

    /**
     * 获取用户详情
     *
     * @return {@link UserEntity}
     */
    UserDetails getUserDetails(UserEntity user);

    /**
     * 根据用户名、手机号、邮箱查询用户
     *
     * @return {@link UserEntity}
     */
    Optional<UserEntity> findByUsernameOrPhoneOrEmail(String keyword);
}
