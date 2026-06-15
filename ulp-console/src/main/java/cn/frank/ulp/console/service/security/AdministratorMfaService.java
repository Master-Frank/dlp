/*
 * ulp-console - United Login Platform
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
package cn.frank.ulp.console.service.security;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.common.entity.setting.AdministratorEntity;
import cn.frank.ulp.common.repository.setting.AdministratorRepository;
import cn.frank.ulp.support.exception.BadParamsException;
import cn.frank.ulp.support.security.mfa.AbstractMfaService;
import cn.frank.ulp.support.security.mfa.MfaBackupCodeGenerator;
import cn.frank.ulp.support.security.mfa.MfaCodeVerifier;
import cn.frank.ulp.support.security.mfa.MfaOtpAuthUriBuilder;
import cn.frank.ulp.support.security.mfa.MfaSecretCipher;
import cn.frank.ulp.support.security.mfa.MfaSecretGenerator;

/**
 * Administrator-side MFA self-service — operates on {@code ulp_administrator} rows. Redis
 * staging key namespace is {@code ULP_BIND_MFA_SECRET:admin:{adminId}}.
 *
 * <p>Admin unbind is intentionally unguarded by organization policy: org-level
 * {@code mfa_enforced} only applies to end-users (see {@code OrgMfaPolicyService}).
 */
@Service
public class AdministratorMfaService extends AbstractMfaService {

    private final AdministratorRepository administratorRepository;

    public AdministratorMfaService(MfaSecretGenerator secretGenerator, MfaSecretCipher secretCipher,
                                   MfaCodeVerifier codeVerifier, MfaOtpAuthUriBuilder uriBuilder,
                                   MfaBackupCodeGenerator backupCodeGenerator,
                                   PasswordEncoder passwordEncoder,
                                   StringRedisTemplate redisTemplate,
                                   AdministratorRepository administratorRepository) {
        super(secretGenerator, secretCipher, codeVerifier, uriBuilder, backupCodeGenerator,
            passwordEncoder, redisTemplate);
        this.administratorRepository = administratorRepository;
    }

    @Override
    public String subjectType() {
        return "admin";
    }

    @Override
    protected String loadUsername(String userId) {
        return administratorRepository.findById(userId).map(AdministratorEntity::getUsername)
            .orElseThrow(() -> new BadParamsException("administrator not found: " + userId));
    }

    @Override
    protected boolean isMfaEnabled(String userId) {
        return administratorRepository.findById(userId).map(AdministratorEntity::getMfaEnabled)
            .orElse(Boolean.FALSE);
    }

    @Override
    protected String loadCipher(String userId) {
        return administratorRepository.findById(userId)
            .map(AdministratorEntity::getTotpSecretCipher).orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    protected void persistEnabled(String userId, String cipher, String backupCodesJson) {
        AdministratorEntity admin = administratorRepository.findById(userId)
            .orElseThrow(() -> new BadParamsException("administrator not found: " + userId));
        admin.setMfaEnabled(Boolean.TRUE);
        admin.setTotpSecretCipher(cipher);
        admin.setBackupCodesJson(backupCodesJson);
        administratorRepository.save(admin);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    protected void persistDisabled(String userId) {
        AdministratorEntity admin = administratorRepository.findById(userId)
            .orElseThrow(() -> new BadParamsException("administrator not found: " + userId));
        admin.setMfaEnabled(Boolean.FALSE);
        admin.setTotpSecretCipher(null);
        admin.setBackupCodesJson(null);
        administratorRepository.save(admin);
    }
}
