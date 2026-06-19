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

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.console.service.security.AdministratorMfaService;
import cn.frank.ulp.support.exception.BadParamsException;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.security.mfa.MfaConfirmBindResult;
import cn.frank.ulp.support.security.mfa.MfaLockoutService;
import cn.frank.ulp.support.security.mfa.MfaMetrics;
import cn.frank.ulp.support.security.mfa.MfaPrepareBindResult;
import cn.frank.ulp.support.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

/**
 * 管理员自服务 MFA 端点。
 *
 * <p>路径前缀 {@code /api/v1/mfa}：
 * <ul>
 *   <li>{@code POST /bind/prepare} — 生成 TOTP secret + otpauth URI（无 OTP，不接 lockout）</li>
 *   <li>{@code POST /bind/confirm} — 用首次 OTP 验证并提交，返回 10 个明文备份码</li>
 *   <li>{@code POST /unbind} — 提供当前 OTP 后解除绑定（备份码不接受，admin 不受组织强制位约束）</li>
 * </ul>
 *
 * <p>{@code bind/confirm} 与 {@code unbind} 共用同一个 {@link MfaLockoutService} 计数器
 * （key = {@code ULP_MFA_FAIL:admin:{adminId}}，与登录后挑战阶段共享）：
 * <ul>
 *   <li>请求进入时先 {@code isLockedOut} 短路 → 423 + {@code Retry-After}，不消耗 OTP 校验</li>
 *   <li>{@link BadParamsException}（invalid OTP）→ {@code recordFailure}；若刚好打到阈值
 *       则吞掉异常返 423，否则继续抛给全局处理器渲染原 400 语义</li>
 *   <li>成功 → {@code clear}，让用户回到干净起点</li>
 * </ul>
 * 共享计数器意味着：登录挑战阶段失败 4 次后再调 bind/confirm 失败 1 次也会被锁，符合"针对 subject
 * 的全口径节流"威胁模型。
 */
@RestController
@RequestMapping("/api/v1/mfa")
@RequiredArgsConstructor
public class MfaController {

    private static final String           STATUS_LOCKED_OUT = "locked_out";

    private final AdministratorMfaService administratorMfaService;
    private final MfaLockoutService       mfaLockoutService;
    private final MfaMetrics              mfaMetrics;

    @PostMapping("/bind/prepare")
    public ApiRestResult<MfaPrepareBindResult> prepareBind() {
        String adminId = SecurityUtils.getCurrentUserId();
        MfaPrepareBindResult result = administratorMfaService.prepareBind(adminId);
        mfaMetrics.bind(administratorMfaService.subjectType(), "prepare", "success");
        return ApiRestResult.ok(result);
    }

    @PostMapping("/bind/confirm")
    public ResponseEntity<ApiRestResult<MfaConfirmBindResult>> confirmBind(@RequestBody ConfirmBindRequest req) {
        String adminId = SecurityUtils.getCurrentUserId();
        String subjectType = administratorMfaService.subjectType();
        if (mfaLockoutService.isLockedOut(subjectType, adminId)) {
            mfaMetrics.bind(subjectType, "confirm", "locked_out");
            return lockedResponse(subjectType, adminId);
        }
        try {
            MfaConfirmBindResult result = administratorMfaService.confirmBind(adminId, req.otp());
            mfaLockoutService.clear(subjectType, adminId);
            mfaMetrics.bind(subjectType, "confirm", "success");
            return ResponseEntity.ok(ApiRestResult.ok(result));
        } catch (BadParamsException ex) {
            long count = mfaLockoutService.recordFailure(subjectType, adminId);
            if (count >= mfaLockoutService.threshold()) {
                mfaMetrics.lockout(subjectType, "bind_confirm");
                mfaMetrics.bind(subjectType, "confirm", "locked_out");
                return lockedResponse(subjectType, adminId);
            }
            mfaMetrics.bind(subjectType, "confirm", "invalid_otp");
            throw ex;
        }
    }

    @PostMapping("/unbind")
    public ResponseEntity<ApiRestResult<Boolean>> unbind(@RequestBody UnbindRequest req) {
        String adminId = SecurityUtils.getCurrentUserId();
        String subjectType = administratorMfaService.subjectType();
        if (mfaLockoutService.isLockedOut(subjectType, adminId)) {
            mfaMetrics.bind(subjectType, "unbind", "locked_out");
            return lockedResponse(subjectType, adminId);
        }
        try {
            administratorMfaService.unbind(adminId, req.currentOtp());
            mfaLockoutService.clear(subjectType, adminId);
            mfaMetrics.bind(subjectType, "unbind", "success");
            return ResponseEntity.ok(ApiRestResult.ok(Boolean.TRUE));
        } catch (BadParamsException ex) {
            long count = mfaLockoutService.recordFailure(subjectType, adminId);
            if (count >= mfaLockoutService.threshold()) {
                mfaMetrics.lockout(subjectType, "unbind");
                mfaMetrics.bind(subjectType, "unbind", "locked_out");
                return lockedResponse(subjectType, adminId);
            }
            mfaMetrics.bind(subjectType, "unbind", "invalid_otp");
            throw ex;
        }
    }

    private <T> ResponseEntity<ApiRestResult<T>> lockedResponse(String subjectType, String userId) {
        long retryAfter = mfaLockoutService.remainingSeconds(subjectType, userId);
        if (retryAfter <= 0L) {
            retryAfter = mfaLockoutService.fallbackWindowSeconds();
        }
        return ResponseEntity.status(HttpStatus.LOCKED)
            .header(HttpHeaders.RETRY_AFTER, Long.toString(Math.max(retryAfter, 1L)))
            .body(ApiRestResult.<T> builder().status(STATUS_LOCKED_OUT).message("locked out")
                .build());
    }

    public record ConfirmBindRequest(String otp) {
    }

    public record UnbindRequest(String currentOtp) {
    }
}
