/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.service.userdetail;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;

import cn.topiam.employee.common.repository.account.UserRepository;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/10/19 14:10
 */
@Service
public class UserDetailsPasswordServiceImpl implements UserDetailsPasswordService {

    private final Logger logger = LoggerFactory.getLogger(UserDetailsPasswordService.class);

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
        String userId = ((cn.topiam.employee.support.security.userdetails.UserDetails) user)
            .getId();
        userRepository.updatePassword(userId, newPassword, LocalDateTime.now());
        return user;
    }

    /**
     * UserRepository
     */
    private final UserRepository userRepository;

    public UserDetailsPasswordServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
