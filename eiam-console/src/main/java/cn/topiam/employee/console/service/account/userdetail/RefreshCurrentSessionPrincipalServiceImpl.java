/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.account.userdetail;

import org.springframework.stereotype.Service;

import cn.topiam.employee.console.service.setting.AdministratorService;
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
        return administratorService.getUserDetails(userId);
    }

    private final AdministratorService administratorService;

    public RefreshCurrentSessionPrincipalServiceImpl(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }
}
