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

import java.util.Objects;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.common.entity.account.OrganizationEntity;
import cn.frank.ulp.common.repository.account.OrganizationRepository;
import cn.frank.ulp.support.exception.BadParamsException;
import cn.frank.ulp.support.result.ApiRestResult;

import lombok.RequiredArgsConstructor;

/**
 * 组织级 MFA 强制位 ({@code ulp_organization.mfa_enforced}) 的切换端点。
 *
 * <p>仅 ADMIN 可调用。校验组织存在 + 未软删（{@code @SoftDelete} 自动过滤）。重复值视为
 * no-op 但仍返 200；只有真实变更值时写库 + （Phase 6.4 完成）发 {@code ORG_MFA_POLICY_CHANGED}
 * 审计事件，details 含 {@code org_id / org_name / old_value / new_value / actor_admin_id}。
 */
@RestController
@RequestMapping("/api/v1/admin/organizations")
@RequiredArgsConstructor
public class OrgMfaPolicyController {

    private final OrganizationRepository organizationRepository;

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
        }
        return ApiRestResult.ok(new MfaPolicyResult(orgId, newValue, changed));
    }

    public record MfaPolicyRequest(Boolean mfaEnforced) {
    }

    public record MfaPolicyResult(String orgId, Boolean mfaEnforced, boolean changed) {
    }
}
