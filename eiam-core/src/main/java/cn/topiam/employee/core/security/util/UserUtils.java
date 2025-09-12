/*
 * eiam-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.core.security.util;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import cn.topiam.employee.common.entity.account.UserDetailEntity;
import cn.topiam.employee.common.entity.account.UserEntity;
import cn.topiam.employee.common.exception.UserNotFoundException;
import cn.topiam.employee.common.repository.account.UserDetailRepository;
import cn.topiam.employee.common.repository.account.UserRepository;
import cn.topiam.employee.support.context.ApplicationContextService;

/**
 * 用户工具
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/8/13 21:50
 */
public final class UserUtils {
    private static final Logger logger = LoggerFactory.getLogger(UserUtils.class);

    public static UserEntity getUser() {
        return getUser(cn.topiam.employee.support.security.util.SecurityUtils.getCurrentUserId());
    }

    public static UserEntity getUser(String userId) {
        Optional<UserEntity> optional = ApplicationContextService.getBean(UserRepository.class)
            .findById(userId);
        if (optional.isPresent()) {
            return optional.get();
        }
        SecurityContextHolder.clearContext();
        logger.error("根据用户ID: [{}] 未查询到用户信息", userId);
        throw new UserNotFoundException();
    }

    public static UserDetailEntity getUserDetails() {
        return getUserDetails(
            cn.topiam.employee.support.security.util.SecurityUtils.getCurrentUserId());
    }

    public static UserDetailEntity getUserDetails(String userId) {
        Optional<UserDetailEntity> optional = ApplicationContextService
            .getBean(UserDetailRepository.class).findByUserId(userId);
        if (optional.isPresent()) {
            return optional.get();
        }
        SecurityContextHolder.clearContext();
        logger.error("根据用户ID: [{}] 未查询到用户信息", userId);
        throw new UserNotFoundException();
    }
}
