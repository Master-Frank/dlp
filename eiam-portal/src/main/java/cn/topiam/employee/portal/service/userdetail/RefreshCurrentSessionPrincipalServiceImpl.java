/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.service.userdetail;

import org.springframework.stereotype.Service;

import cn.topiam.employee.portal.service.UserService;
import cn.topiam.employee.support.security.session.RefreshCurrentSessionPrincipalService;
import cn.topiam.employee.support.security.userdetails.UserDetails;

/**
 * 刷新当前用户会话 Principal 实现
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2024/3/2 14:06
 */
@Service
public class RefreshCurrentSessionPrincipalServiceImpl implements
                                                       RefreshCurrentSessionPrincipalService {
    /**
     * 获取 Principal
     *
     * @param userId {@link String}
     * @return {@link UserDetails}
     */
    @Override
    public UserDetails getPrincipal(String userId) {
        return userService.getUserDetails(userId);
    }

    private final UserService userService;

    public RefreshCurrentSessionPrincipalServiceImpl(UserService userService) {
        this.userService = userService;
    }
}
