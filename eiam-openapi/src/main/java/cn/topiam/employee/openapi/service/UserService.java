/*
 * eiam-openapi - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.openapi.service;

import java.io.Serializable;

import cn.topiam.employee.common.entity.account.query.UserListQuery;
import cn.topiam.employee.common.enums.UserStatus;
import cn.topiam.employee.openapi.pojo.result.UserListResult;
import cn.topiam.employee.openapi.pojo.result.UserResult;
import cn.topiam.employee.openapi.pojo.save.UserCreateParam;
import cn.topiam.employee.openapi.pojo.update.UserUpdateParam;
import cn.topiam.employee.support.repository.page.domain.Page;
import cn.topiam.employee.support.repository.page.domain.PageModel;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-07-31
 */
public interface UserService {

    /**
     * 获取用户（分页）
     *
     * @param page  {@link PageModel}
     * @param query {@link UserListQuery}
     * @return {@link UserListQuery}
     */
    Page<UserListResult> getUserList(PageModel page, UserListQuery query);

    /**
     * 更改用户状态
     *
     * @param id     {@link Long}
     * @param status {@link UserStatus}
     */
    void changeUserStatus(String id, UserStatus status);

    /**
     * 创建用户
     *
     * @param param {@link UserCreateParam}
     */
    void createUser(UserCreateParam param);

    /**
     * 根据ID查询用户
     *
     * @param id {@link String}
     * @return {@link Boolean}
     */
    UserResult getUser(String id);

    /**
     * 更新用户
     *
     * @param param {@link UserUpdateParam}
     */
    void updateUser(UserUpdateParam param);

    /**
     * 删除用户
     *
     * @param id {@link Serializable}
     */
    void deleteUser(String id);

    /**
     * 获取用户id
     *
     * @param externalId {@link String}
     * @param phoneNumber {@link String}
     * @param email {@link String}
     * @param username {@link String}
     * @return {@link String}
     */
    String getUserIdByParams(String externalId, String phoneNumber, String email, String username);
}
