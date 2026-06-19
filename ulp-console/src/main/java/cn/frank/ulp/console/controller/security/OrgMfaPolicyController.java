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
import java.util.Objects;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.audit.entity.Target;
import cn.frank.ulp.audit.enums.EventStatus;
import cn.frank.ulp.audit.enums.TargetType;
import cn.frank.ulp.audit.event.AuditEventPublish;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.common.entity.account.OrganizationEntity;
import cn.frank.ulp.common.repository.account.OrganizationRepository;
import cn.frank.ulp.support.exception.BadParamsException;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

/**
 * 组织级 MFA 强制位 ({@code ulp_organization.mfa_enforced}) 的切换端点。
 *
 * <p>仅 ADMIN 可调用。校验组织存在 + 未软删（{@code @SoftDelete} 自动过滤）。重复值视为
 * no-op 但仍返 200；只有真实变更值时写库 + 发 {@code ORG_MFA_POLICY_CHANGED} 审计事件
 * （Phase 6.5 接线），details 含 {@code org_id / org_name / old_value / new_value /
 * actor_admin_id}。重复值（changed=false）路径 MUST 不发审计，避免噪声。
 */
@RestController
@RequestMapping("/api/v1/admin/organizations")
@RequiredArgsConstructor
public class OrgMfaPolicyController {

    private final OrganizationRepository organizationRepository;
    private final AuditEventPublish      auditEventPublish;

    @PostMapping("/{id}/mfa-policy")
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.frank.ulp.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<MfaPolicyResult> setPolicy(@PathVariable("id") String orgId,
                                                    @RequestBody MfaPolicyRequest req) {
        Objects.requireNonNull(req, "request body");
        Boolean newValue = req.mfaEnforced();
        if (newValue == null) {
            throw new BadParamsException("mfaEnforced is required");
        }
        OrganizationEntity org = organizationRepository.findById(orgId)
            .orElseThrow(() -> new BadParamsException("organization not found: " + orgId));
        Boolean oldValue = Boolean.TRUE.equals(org.getMfaEnforced());
        boolean changed = !oldValue.equals(newValue);
        if (changed) {
            org.setMfaEnforced(newValue);
            organizationRepository.save(org);
            String actorAdminId = SecurityUtils.getCurrentUserId();
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("org_id", orgId);
            params.put("org_name", org.getName());
            params.put("old_value", oldValue);
            params.put("new_value", newValue);
            params.put("actor_admin_id", actorAdminId);
            Target target = Target.builder().id(orgId).name(org.getName())
                .type(TargetType.ORGANIZATION).build();
            auditEventPublish.publish(EventType.ORG_MFA_POLICY_CHANGED, params,
                "org_mfa_policy_changed", List.of(target), null, EventStatus.SUCCESS, null);
        }
        return ApiRestResult.ok(new MfaPolicyResult(orgId, newValue, changed));
    }

    public record MfaPolicyRequest(Boolean mfaEnforced) {
    }

    public record MfaPolicyResult(String orgId, Boolean mfaEnforced, boolean changed) {
    }
}
