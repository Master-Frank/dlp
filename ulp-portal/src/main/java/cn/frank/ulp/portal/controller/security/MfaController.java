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

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.audit.enums.EventStatus;
import cn.frank.ulp.audit.event.AuditEventPublish;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.common.security.mfa.OrgMfaPolicyService;
import cn.frank.ulp.portal.service.security.UserMfaService;
import cn.frank.ulp.support.exception.BadParamsException;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.security.mfa.MfaConfirmBindResult;
import cn.frank.ulp.support.security.mfa.MfaLockoutService;
import cn.frank.ulp.support.security.mfa.MfaMetrics;
import cn.frank.ulp.support.security.mfa.MfaPrepareBindResult;
import cn.frank.ulp.support.security.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

/**
 * 终端用户自服务 MFA 端点。
 *
 * <p>{@code POST /unbind} 在进入 TOTP 验证前先调
 * {@link OrgMfaPolicyService#isUserEnforced(String)}；若组织强制覆盖该用户，立即返回 403
 * + {@code unbind_blocked_by_org_policy}，不消费失败计数器。
 *
 * <p>{@code bind/confirm} 与（通过 org-policy 校验后的）{@code unbind} 共用同一个
 * {@link MfaLockoutService} 计数器（key = {@code ULP_MFA_FAIL:user:{userId}}，与登录后挑战
 * 阶段共享）：
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

    private static final String       STATUS_LOCKED_OUT = "locked_out";

    private final UserMfaService      userMfaService;
    private final OrgMfaPolicyService orgMfaPolicyService;
    private final MfaLockoutService   mfaLockoutService;
    private final AuditEventPublish   auditEventPublish;
    private final MfaMetrics          mfaMetrics;

    @PostMapping("/bind/prepare")
    public ApiRestResult<MfaPrepareBindResult> prepareBind() {
        String userId = SecurityUtils.getCurrentUserId();
        MfaPrepareBindResult result = userMfaService.prepareBind(userId);
        auditEventPublish.publish(EventType.PREPARE_BIND_MFA, "subject=user;userId=" + userId,
            EventStatus.SUCCESS);
        mfaMetrics.bind(userMfaService.subjectType(), "prepare", "success");
        return ApiRestResult.ok(result);
    }

    @PostMapping("/bind/confirm")
    public ResponseEntity<ApiRestResult<MfaConfirmBindResult>> confirmBind(@RequestBody ConfirmBindRequest req) {
        String userId = SecurityUtils.getCurrentUserId();
        String subjectType = userMfaService.subjectType();
        if (mfaLockoutService.isLockedOut(subjectType, userId)) {
            auditEventPublish.publish(EventType.MFA_LOCKED_OUT,
                "phase=bind_confirm;subject=" + subjectType + ";userId=" + userId,
                EventStatus.FAIL);
            mfaMetrics.bind(subjectType, "confirm", "locked_out");
            return lockedResponse(subjectType, userId);
        }
        try {
            MfaConfirmBindResult result = userMfaService.confirmBind(userId, req.otp());
            mfaLockoutService.clear(subjectType, userId);
            auditEventPublish.publish(EventType.BIND_MFA,
                "subject=" + subjectType + ";userId=" + userId, EventStatus.SUCCESS);
            mfaMetrics.bind(subjectType, "confirm", "success");
            return ResponseEntity.ok(ApiRestResult.ok(result));
        } catch (BadParamsException ex) {
            long count = mfaLockoutService.recordFailure(subjectType, userId);
            if (count >= mfaLockoutService.threshold()) {
                auditEventPublish.publish(EventType.MFA_LOCKED_OUT,
                    "phase=bind_confirm;subject=" + subjectType + ";userId=" + userId,
                    EventStatus.FAIL);
                mfaMetrics.lockout(subjectType, "bind_confirm");
                mfaMetrics.bind(subjectType, "confirm", "locked_out");
                return lockedResponse(subjectType, userId);
            }
            auditEventPublish.publish(EventType.BIND_MFA,
                "subject=" + subjectType + ";userId=" + userId + ";failure_reason=invalid_otp",
                EventStatus.FAIL);
            mfaMetrics.bind(subjectType, "confirm", "invalid_otp");
            throw ex;
        }
    }

    @PostMapping("/unbind")
    public ResponseEntity<ApiRestResult<Boolean>> unbind(@RequestBody UnbindRequest req) {
        String userId = SecurityUtils.getCurrentUserId();
        String subjectType = userMfaService.subjectType();
        if (orgMfaPolicyService.isUserEnforced(userId)) {
            auditEventPublish
                .publish(EventType.UNBIND_MFA,
                    "subject=" + subjectType + ";userId=" + userId
                                               + ";failure_reason=blocked_by_org_policy",
                    EventStatus.FAIL);
            mfaMetrics.bind(subjectType, "unbind", "blocked_by_org_policy");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiRestResult.<Boolean> builder().status("unbind_blocked_by_org_policy")
                    .message("所在组织已启用强制 MFA，无法解绑").build());
        }
        if (mfaLockoutService.isLockedOut(subjectType, userId)) {
            auditEventPublish.publish(EventType.MFA_LOCKED_OUT,
                "phase=unbind;subject=" + subjectType + ";userId=" + userId, EventStatus.FAIL);
            mfaMetrics.bind(subjectType, "unbind", "locked_out");
            return lockedResponse(subjectType, userId);
        }
        try {
            userMfaService.unbind(userId, req.currentOtp());
            mfaLockoutService.clear(subjectType, userId);
            auditEventPublish.publish(EventType.UNBIND_MFA,
                "subject=" + subjectType + ";userId=" + userId, EventStatus.SUCCESS);
            mfaMetrics.bind(subjectType, "unbind", "success");
            return ResponseEntity.ok(ApiRestResult.ok(Boolean.TRUE));
        } catch (BadParamsException ex) {
            long count = mfaLockoutService.recordFailure(subjectType, userId);
            if (count >= mfaLockoutService.threshold()) {
                auditEventPublish.publish(EventType.MFA_LOCKED_OUT,
                    "phase=unbind;subject=" + subjectType + ";userId=" + userId, EventStatus.FAIL);
                mfaMetrics.lockout(subjectType, "unbind");
                mfaMetrics.bind(subjectType, "unbind", "locked_out");
                return lockedResponse(subjectType, userId);
            }
            auditEventPublish.publish(EventType.UNBIND_MFA,
                "subject=" + subjectType + ";userId=" + userId + ";failure_reason=invalid_otp",
                EventStatus.FAIL);
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

    @SuppressWarnings("unused")
    private static Map<String, Object> details(String key, Object value) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put(key, value);
        return m;
    }

    public record ConfirmBindRequest(String otp) {
    }

    public record UnbindRequest(String currentOtp) {
    }
}
