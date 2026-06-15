/*
 * ulp-portal - United Login Platform
 * Copyright (c) 2022-Present Frank Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.frank.ulp.portal.service.security;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.support.exception.BadParamsException;
import cn.frank.ulp.support.security.mfa.AbstractMfaService;
import cn.frank.ulp.support.security.mfa.MfaBackupCodeGenerator;
import cn.frank.ulp.support.security.mfa.MfaCodeVerifier;
import cn.frank.ulp.support.security.mfa.MfaOtpAuthUriBuilder;
import cn.frank.ulp.support.security.mfa.MfaSecretCipher;
import cn.frank.ulp.support.security.mfa.MfaSecretGenerator;

/**
 * End-user MFA self-service — operates on {@code ulp_user} rows. Redis staging key namespace
 * is {@code ULP_BIND_MFA_SECRET:user:{userId}}.
 *
 * <p>Callers of {@link #unbind(String, String)} MUST consult
 * {@code OrgMfaPolicyService.isUserEnforced(userId)} BEFORE invoking this service; if true,
 * return 403 {@code unbind_blocked_by_org_policy} and skip the OTP check entirely (no
 * failure-counter consumption). The enforcement guard is intentionally NOT embedded here so
 * that the policy decision stays at the controller/edge layer and the service contract
 * matches the admin variant.
 */
@Service
public class UserMfaService extends AbstractMfaService {

    private final UserRepository userRepository;

    public UserMfaService(MfaSecretGenerator secretGenerator, MfaSecretCipher secretCipher,
                          MfaCodeVerifier codeVerifier, MfaOtpAuthUriBuilder uriBuilder,
                          MfaBackupCodeGenerator backupCodeGenerator,
                          PasswordEncoder passwordEncoder, StringRedisTemplate redisTemplate,
                          UserRepository userRepository) {
        super(secretGenerator, secretCipher, codeVerifier, uriBuilder, backupCodeGenerator,
            passwordEncoder, redisTemplate);
        this.userRepository = userRepository;
    }

    @Override
    public String subjectType() {
        return "user";
    }

    @Override
    protected String loadUsername(String userId) {
        return userRepository.findById(userId).map(UserEntity::getUsername)
            .orElseThrow(() -> new BadParamsException("user not found: " + userId));
    }

    @Override
    protected boolean isMfaEnabled(String userId) {
        return userRepository.findById(userId).map(UserEntity::getMfaEnabled).orElse(Boolean.FALSE);
    }

    @Override
    protected String loadCipher(String userId) {
        return userRepository.findById(userId).map(UserEntity::getTotpSecretCipher).orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    protected void persistEnabled(String userId, String cipher, String backupCodesJson) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new BadParamsException("user not found: " + userId));
        user.setMfaEnabled(Boolean.TRUE);
        user.setTotpSecretCipher(cipher);
        user.setBackupCodesJson(backupCodesJson);
        userRepository.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    protected void persistDisabled(String userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new BadParamsException("user not found: " + userId));
        user.setMfaEnabled(Boolean.FALSE);
        user.setTotpSecretCipher(null);
        user.setBackupCodesJson(null);
        userRepository.save(user);
    }
}
