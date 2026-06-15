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
package cn.frank.ulp.portal.controller.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.common.security.mfa.OrgMfaPolicyService;
import cn.frank.ulp.portal.service.security.UserMfaService;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.security.mfa.MfaConfirmBindResult;
import cn.frank.ulp.support.security.mfa.MfaPrepareBindResult;
import cn.frank.ulp.support.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

/**
 * 终端用户自服务 MFA 端点。
 *
 * <p>{@code POST /unbind} 在进入 TOTP 验证前先调
 * {@link OrgMfaPolicyService#isUserEnforced(String)}；若组织强制覆盖该用户，立即返回 403
 * + {@code unbind_blocked_by_org_policy}，不消费失败计数器（lockout 在 Phase 5 接入）。
 */
@RestController
@RequestMapping("/api/v1/mfa")
@RequiredArgsConstructor
public class MfaController {

    private final UserMfaService      userMfaService;
    private final OrgMfaPolicyService orgMfaPolicyService;

    @PostMapping("/bind/prepare")
    public ApiRestResult<MfaPrepareBindResult> prepareBind() {
        String userId = SecurityUtils.getCurrentUserId();
        return ApiRestResult.ok(userMfaService.prepareBind(userId));
    }

    @PostMapping("/bind/confirm")
    public ApiRestResult<MfaConfirmBindResult> confirmBind(@RequestBody ConfirmBindRequest req) {
        String userId = SecurityUtils.getCurrentUserId();
        return ApiRestResult.ok(userMfaService.confirmBind(userId, req.otp()));
    }

    @PostMapping("/unbind")
    public ResponseEntity<ApiRestResult<Boolean>> unbind(@RequestBody UnbindRequest req) {
        String userId = SecurityUtils.getCurrentUserId();
        if (orgMfaPolicyService.isUserEnforced(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiRestResult.<Boolean> builder().status("unbind_blocked_by_org_policy")
                    .message("所在组织已启用强制 MFA，无法解绑").build());
        }
        userMfaService.unbind(userId, req.currentOtp());
        return ResponseEntity.ok(ApiRestResult.ok(Boolean.TRUE));
    }

    public record ConfirmBindRequest(String otp) {
    }

    public record UnbindRequest(String currentOtp) {
    }
}
