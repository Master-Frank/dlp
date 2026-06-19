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
package cn.frank.ulp.console.controller.security;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.audit.entity.Target;
import cn.frank.ulp.audit.enums.EventStatus;
import cn.frank.ulp.audit.enums.TargetType;
import cn.frank.ulp.audit.event.AuditEventPublish;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.common.entity.account.UserEntity;
import cn.frank.ulp.common.entity.setting.AdministratorEntity;
import cn.frank.ulp.common.repository.account.UserRepository;
import cn.frank.ulp.common.repository.setting.AdministratorRepository;
import cn.frank.ulp.support.exception.BadParamsException;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

/**
 * 管理员重置他人 MFA 绑定的端点（强制清除目标用户/管理员的 TOTP secret + 备份码，
 * 不验证目标的 OTP — 管理员身份本身即为授权证明）。
 *
 * <p>两路径分别处理用户和管理员，便于审计区分（{@code target_user_type} 字段）。
 * 仅 ADMIN 角色可调用，前置 {@code @PreAuthorize} 拦截。
 *
 * <p>成功后发 {@link AccountEventType#ADMIN_RESET_USER_MFA} 审计，params 含
 * {@code target_user_id / target_user_type / actor_admin_id}；target 列表挂上对应
 * {@link TargetType#USER}/{@link TargetType#ADMINISTRATOR} 节点便于按目标维度筛选。
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminMfaResetController {

    private final UserRepository          userRepository;
    private final AdministratorRepository administratorRepository;
    private final AuditEventPublish       auditEventPublish;

    @PostMapping("/users/{id}/reset-mfa")
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> resetUserMfa(@PathVariable("id") String userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new BadParamsException("user not found: " + userId));
        user.setMfaEnabled(Boolean.FALSE);
        user.setTotpSecretCipher(null);
        user.setBackupCodesJson(null);
        userRepository.save(user);
        publishReset(userId, user.getUsername(), "user", TargetType.USER);
        return ApiRestResult.ok(Boolean.TRUE);
    }

    @PostMapping("/administrators/{id}/reset-mfa")
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> resetAdministratorMfa(@PathVariable("id") String adminId) {
        AdministratorEntity admin = administratorRepository.findById(adminId)
            .orElseThrow(() -> new BadParamsException("administrator not found: " + adminId));
        admin.setMfaEnabled(Boolean.FALSE);
        admin.setTotpSecretCipher(null);
        admin.setBackupCodesJson(null);
        administratorRepository.save(admin);
        publishReset(adminId, admin.getUsername(), "admin", TargetType.ADMINISTRATOR);
        return ApiRestResult.ok(Boolean.TRUE);
    }

    private void publishReset(String targetId, String targetName, String targetUserType,
                              TargetType targetType) {
        String actorAdminId = SecurityUtils.getCurrentUserId();
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("target_user_id", targetId);
        params.put("target_user_type", targetUserType);
        params.put("actor_admin_id", actorAdminId);
        Target target = Target.builder().id(targetId)
            .name(targetName == null ? targetId : targetName).type(targetType).build();
        auditEventPublish.publish(EventType.ADMIN_RESET_USER_MFA, params, "admin_reset_mfa",
            List.of(target), null, EventStatus.SUCCESS, null);
    }
}
