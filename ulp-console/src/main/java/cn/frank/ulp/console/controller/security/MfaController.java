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

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.console.service.security.AdministratorMfaService;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.security.mfa.MfaConfirmBindResult;
import cn.frank.ulp.support.security.mfa.MfaPrepareBindResult;
import cn.frank.ulp.support.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

/**
 * 管理员自服务 MFA 端点。
 *
 * <p>路径前缀 {@code /api/v1/mfa}：
 * <ul>
 *   <li>{@code POST /bind/prepare} — 生成 TOTP secret + otpauth URI</li>
 *   <li>{@code POST /bind/confirm} — 用首次 OTP 验证并提交，返回 10 个明文备份码</li>
 *   <li>{@code POST /unbind} — 提供当前 OTP 后解除绑定（备份码不接受，admin 不受组织强制位约束）</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/mfa")
@RequiredArgsConstructor
public class MfaController {

    private final AdministratorMfaService administratorMfaService;

    @PostMapping("/bind/prepare")
    public ApiRestResult<MfaPrepareBindResult> prepareBind() {
        String adminId = SecurityUtils.getCurrentUserId();
        return ApiRestResult.ok(administratorMfaService.prepareBind(adminId));
    }

    @PostMapping("/bind/confirm")
    public ApiRestResult<MfaConfirmBindResult> confirmBind(@org.springframework.web.bind.annotation.RequestBody ConfirmBindRequest req) {
        String adminId = SecurityUtils.getCurrentUserId();
        return ApiRestResult.ok(administratorMfaService.confirmBind(adminId, req.otp()));
    }

    @PostMapping("/unbind")
    public ApiRestResult<Boolean> unbind(@org.springframework.web.bind.annotation.RequestBody UnbindRequest req) {
        String adminId = SecurityUtils.getCurrentUserId();
        administratorMfaService.unbind(adminId, req.currentOtp());
        return ApiRestResult.ok(Boolean.TRUE);
    }

    public record ConfirmBindRequest(String otp) {
    }

    public record UnbindRequest(String currentOtp) {
    }
}
