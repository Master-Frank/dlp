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

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.frank.ulp.common.entity.setting.AdministratorEntity;
import cn.frank.ulp.common.repository.setting.AdministratorRepository;
import cn.frank.ulp.support.exception.BadParamsException;
import cn.frank.ulp.support.security.mfa.MfaBackupCodeStore;

import lombok.RequiredArgsConstructor;

/**
 * {@link MfaBackupCodeStore} bound to {@code ulp_administrator} — paired with
 * {@link AdministratorMfaService} (both keyed by subject type {@code "admin"}).
 */
@Component
@RequiredArgsConstructor
public class AdministratorBackupCodeStore implements MfaBackupCodeStore {

    private final AdministratorRepository administratorRepository;

    @Override
    public String subjectType() {
        return "admin";
    }

    @Override
    public String loadBackupCodesJson(String userId) {
        return administratorRepository.findById(userId).map(AdministratorEntity::getBackupCodesJson)
            .orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBackupCodesJson(String userId, String backupCodesJson) {
        AdministratorEntity admin = administratorRepository.findById(userId)
            .orElseThrow(() -> new BadParamsException("administrator not found: " + userId));
        admin.setBackupCodesJson(backupCodesJson);
        administratorRepository.save(admin);
    }
}
