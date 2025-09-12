/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.service.account.userdetail;

import java.time.LocalDateTime;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;

import cn.topiam.employee.common.repository.setting.AdministratorRepository;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/10/19 14:10
 */
@Service
public class UserDetailsPasswordServiceImpl implements UserDetailsPasswordService {

    /**
     * Modify the specified user's password. This should change the user's password in the
     * persistent user repository (database, LDAP etc).
     *
     * @param user        the user to modify the password for
     * @param newPassword the password to change to, encoded by the configured
     *                    {@code PasswordEncoder}
     * @return the updated UserDetails with the new password
     */
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        administratorRepository.updatePassword(
            ((cn.topiam.employee.support.security.userdetails.UserDetails) user).getId(),
            newPassword, LocalDateTime.now());
        return user;
    }

    /**
     * AdministratorRepository
     */
    private final AdministratorRepository administratorRepository;

    public UserDetailsPasswordServiceImpl(AdministratorRepository administratorRepository) {
        this.administratorRepository = administratorRepository;
    }
}
