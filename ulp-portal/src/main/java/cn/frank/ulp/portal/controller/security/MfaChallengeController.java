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

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.audit.entity.Actor;
import cn.frank.ulp.audit.enums.EventStatus;
import cn.frank.ulp.audit.event.AuditEventPublish;
import cn.frank.ulp.audit.event.type.EventType;
import cn.frank.ulp.audit.event.type.PortalEventType;
import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.security.constant.SecurityConstants;
import cn.frank.ulp.support.security.mfa.MfaChallengeOutcome;
import cn.frank.ulp.support.security.mfa.MfaChallengeService;
import cn.frank.ulp.support.security.mfa.MfaChallengeService.BackupCodeChallengeResult;
import cn.frank.ulp.support.security.mfa.MfaLockoutService;
import cn.frank.ulp.support.security.mfa.MfaMetrics;
import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.userdetails.UserType;

import lombok.RequiredArgsConstructor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Portal 第二因子挑战提交端点。结构与 console 版本完全对齐，只在 portal SecurityFilterChain
 * 下生效；两侧共用 {@link MfaChallengeService}，subject-type 路由由 service 自身按
 * pending entry 里的 userType 选择对应 {@link cn.frank.ulp.support.security.mfa.MfaService} 实现。
 *
 * <p>{@code POST /api/v1/mfa/challenge} 接受 JSON body：
 * <pre>{@code
 *   { "code": "123456" }            // Phase 3 — TOTP 6 位
 *   { "backupCode": "ABCD-EFGH" }   // Phase 5 — 备份码消费
 * }</pre>
 *
 * <p>challenge_id 来源优先级：cookie {@code ulp-mfa-pending} → body 字段 {@code challengeId}。
 *
 * <p>响应 / HTTP 语义见 spec.md Phase 6 audit failure_reason 对齐：
 * <ul>
 *   <li>200 {@code status=ok} — 验证通过，Authentication 已写入 session；备份码路径会附带
 *       {@code backup_codes_remaining}、{@code regenerate_backup_codes_warning}（剩余 ≤2）、
 *       {@code regenerate_backup_codes_required}（剩余 =0）三个标志位</li>
 *   <li>401 {@code status=invalid_otp} — code 不匹配，cookie 保留</li>
 *   <li>401 {@code status=invalid_backup_code} — 备份码不匹配，cookie 保留</li>
 *   <li>401 {@code status=challenge_expired} — pending 已过期 / 不存在，cookie 清除</li>
 *   <li>401 {@code status=challenge_session_invalid} — IP 不在 /24 同段、subject 已失绑等，cookie 清除</li>
 *   <li>423 {@code status=locked_out} + {@code Retry-After} 头 — 失败计数达阈值，cookie 保留</li>
 * </ul>
 *
 * <p>Phase 6.5 审计接线：所有 outcome 分支发对应 {@link PortalEventType} 事件；失败路径
 * (SecurityContext 仍空) 显式构造 {@link Actor}，actor 来自 {@link
 * MfaChallengeService#peekPendingAuthentication(String)} 的 principal，避免
 * {@link AuditEventPublish#getActor()} 因无 auth 上下文 NPE。
 * {@code MFA_VERIFY_FAILURE} params 含 {@code failure_reason} ∈
 * {@code {invalid_otp, invalid_backup_code, challenge_expired, challenge_session_invalid}}；
 * {@code BACKUP_CODE_USED} 在备份码成功路径同步发，便于审计追踪 10 码消费节奏。
 *
 * <p>端点本身放行（{@code permitAll}）—— 处于"已主认证未二次"中间态，由
 * {@link cn.frank.ulp.portal.configuration.security.PortalSecurityConfiguration} 在 chain
 * 中显式开放，安全性靠 cookie 一次性 UUID + 5 分钟 TTL + Redis pending /24 IP 绑定共同兜底。
 */
@RestController
@RequestMapping("/api/v1/mfa")
@RequiredArgsConstructor
public class MfaChallengeController {

    private static final String       STATUS_OK    = "ok";
    private static final String       SUBJECT_USER = "user";

    private final MfaChallengeService challengeService;
    private final MfaLockoutService   lockoutService;
    private final AuditEventPublish   auditEventPublish;
    private final MfaMetrics          mfaMetrics;

    @PostMapping("/challenge")
    public ResponseEntity<ApiRestResult<Map<String, Object>>> challenge(@RequestBody(required = false) ChallengeRequest body,
                                                                        HttpServletRequest request,
                                                                        HttpServletResponse response) {
        ChallengeRequest payload = body == null ? new ChallengeRequest(null, null, null) : body;
        String challengeId = resolveChallengeId(payload, request);

        if (StringUtils.isNotBlank(payload.backupCode())) {
            return handleBackupCode(challengeId, payload.backupCode(), request, response);
        }

        if (StringUtils.isBlank(challengeId) || StringUtils.isBlank(payload.code())) {
            publishVerifyFailure(challengeId, MfaChallengeOutcome.CHALLENGE_EXPIRED);
            mfaMetrics.verifyOutcome(SUBJECT_USER, "totp", MfaChallengeOutcome.CHALLENGE_EXPIRED);
            return errorResponse(HttpStatus.UNAUTHORIZED, MfaChallengeOutcome.CHALLENGE_EXPIRED,
                true, request, response);
        }

        MfaChallengeOutcome outcome = challengeService.verifyAndCommit(challengeId, payload.code(),
            request, response);
        mfaMetrics.verifyOutcome(SUBJECT_USER, "totp", outcome);
        return switch (outcome) {
            case SUCCESS -> {
                publishVerifySuccess(false, -1);
                clearPendingCookie(request, response);
                yield ResponseEntity.ok(
                    ApiRestResult.<Map<String, Object>> builder().status(STATUS_OK).build());
            }
            case LOCKED_OUT -> {
                publishLockedOut(challengeId);
                mfaMetrics.lockout(SUBJECT_USER, "challenge");
                yield lockedResponse(outcome, challengeId, request, response);
            }
            case INVALID_OTP, INVALID_BACKUP_CODE -> {
                publishVerifyFailure(challengeId, outcome);
                yield errorResponse(HttpStatus.UNAUTHORIZED, outcome, false, request, response);
            }
            case CHALLENGE_EXPIRED, CHALLENGE_SESSION_INVALID, SUBJECT_NOT_BOUND -> {
                publishVerifyFailure(challengeId, outcome);
                yield errorResponse(HttpStatus.UNAUTHORIZED, outcome, true, request, response);
            }
        };
    }

    /**
     * Backup-code branch — delegates to {@link MfaChallengeService#verifyBackupCodeAndCommit}
     * and on success surfaces remaining-count flags so the frontend can prompt the user to
     * regenerate codes. Failure paths reuse the shared error response builder; lockout the
     * shared 423 + Retry-After path.
     */
    private ResponseEntity<ApiRestResult<Map<String, Object>>> handleBackupCode(String challengeId,
                                                                                String backupCode,
                                                                                HttpServletRequest request,
                                                                                HttpServletResponse response) {
        if (StringUtils.isBlank(challengeId)) {
            publishVerifyFailure(challengeId, MfaChallengeOutcome.CHALLENGE_EXPIRED);
            mfaMetrics.verifyOutcome(SUBJECT_USER, "backup", MfaChallengeOutcome.CHALLENGE_EXPIRED);
            return errorResponse(HttpStatus.UNAUTHORIZED, MfaChallengeOutcome.CHALLENGE_EXPIRED,
                true, request, response);
        }
        BackupCodeChallengeResult result = challengeService.verifyBackupCodeAndCommit(challengeId,
            backupCode, request, response);
        mfaMetrics.verifyOutcome(SUBJECT_USER, "backup", result.outcome());
        return switch (result.outcome()) {
            case SUCCESS -> {
                publishVerifySuccess(true, result.remaining());
                clearPendingCookie(request, response);
                Map<String, Object> payload = new LinkedHashMap<>();
                payload.put("backup_codes_remaining", result.remaining());
                payload.put("regenerate_backup_codes_warning", result.remaining() <= 2);
                payload.put("regenerate_backup_codes_required", result.remaining() == 0);
                yield ResponseEntity.ok(ApiRestResult.<Map<String, Object>> builder()
                    .status(STATUS_OK).result(payload).build());
            }
            case LOCKED_OUT -> {
                publishLockedOut(challengeId);
                mfaMetrics.lockout(SUBJECT_USER, "challenge");
                yield lockedResponse(result.outcome(), challengeId, request, response);
            }
            case INVALID_BACKUP_CODE, INVALID_OTP -> {
                publishVerifyFailure(challengeId, result.outcome());
                yield errorResponse(HttpStatus.UNAUTHORIZED, result.outcome(), false, request,
                    response);
            }
            case CHALLENGE_EXPIRED, CHALLENGE_SESSION_INVALID, SUBJECT_NOT_BOUND -> {
                publishVerifyFailure(challengeId, result.outcome());
                yield errorResponse(HttpStatus.UNAUTHORIZED, result.outcome(), true, request,
                    response);
            }
        };
    }

    private ResponseEntity<ApiRestResult<Map<String, Object>>> lockedResponse(MfaChallengeOutcome outcome,
                                                                              String challengeId,
                                                                              HttpServletRequest request,
                                                                              HttpServletResponse response) {
        // We need (userType,userId) to read remaining seconds, and at lockout time the
        // pending entry is still in Redis (the service deliberately did not consume it).
        // Falling back to lockoutService.remainingSeconds with placeholder values yields 0;
        // instead, derive Retry-After from the configured window if the lookup fails.
        long retryAfter = challengeService.computeRetryAfterSeconds(challengeId)
            .orElse(lockoutService.fallbackWindowSeconds());
        return ResponseEntity.status(HttpStatus.LOCKED)
            .header(HttpHeaders.RETRY_AFTER, Long.toString(Math.max(retryAfter, 1L)))
            .body(ApiRestResult.<Map<String, Object>> builder().status(outcome.reason())
                .message("locked out").build());
    }

    private ResponseEntity<ApiRestResult<Map<String, Object>>> errorResponse(HttpStatus status,
                                                                             MfaChallengeOutcome outcome,
                                                                             boolean clearCookie,
                                                                             HttpServletRequest request,
                                                                             HttpServletResponse response) {
        if (clearCookie) {
            clearPendingCookie(request, response);
        }
        return ResponseEntity.status(status).body(ApiRestResult.<Map<String, Object>> builder()
            .status(outcome.reason()).message(outcome.reason()).build());
    }

    private void clearPendingCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(SecurityConstants.MFA_PENDING_COOKIE, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(request.isSecure());
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);
    }

    private String resolveChallengeId(ChallengeRequest payload, HttpServletRequest request) {
        if (StringUtils.isNotBlank(payload.challengeId())) {
            return payload.challengeId();
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (SecurityConstants.MFA_PENDING_COOKIE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private void publishVerifySuccess(boolean usedBackupCode, int remaining) {
        Authentication auth = currentAuthentication();
        Actor actor = buildActor(auth);
        String content = "phase=challenge;subject=" + actor.getType().getType() + ";userId="
                         + actor.getId() + (usedBackupCode ? ";via=backup_code" : ";via=totp");
        auditEventPublish.publish(EventType.MFA_VERIFY_SUCCESS, content, actor,
            EventStatus.SUCCESS);
        if (usedBackupCode) {
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("remaining", remaining);
            params.put("regenerate_warning", remaining <= 2);
            params.put("regenerate_required", remaining == 0);
            auditEventPublish.publish(EventType.BACKUP_CODE_USED, params, "backup_code_used", null,
                null, EventStatus.SUCCESS, actor);
        }
    }

    private void publishVerifyFailure(String challengeId, MfaChallengeOutcome outcome) {
        Actor actor = buildActor(peekAuthentication(challengeId));
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("failure_reason", failureReason(outcome));
        auditEventPublish.publish(EventType.MFA_VERIFY_FAILURE, params, "mfa_verify_failure", null,
            null, EventStatus.FAIL, actor);
    }

    private void publishLockedOut(String challengeId) {
        Actor actor = buildActor(peekAuthentication(challengeId));
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("phase", "challenge");
        params.put("subject", actor.getType().getType());
        params.put("userId", actor.getId());
        auditEventPublish.publish(EventType.MFA_LOCKED_OUT, params, "mfa_locked_out", null, null,
            EventStatus.FAIL, actor);
    }

    private Authentication peekAuthentication(String challengeId) {
        if (StringUtils.isBlank(challengeId)) {
            return null;
        }
        return challengeService.peekPendingAuthentication(challengeId).orElse(null);
    }

    private Authentication currentAuthentication() {
        return org.springframework.security.core.context.SecurityContextHolder.getContext()
            .getAuthentication();
    }

    private Actor buildActor(Authentication auth) {
        if (auth != null && auth.getPrincipal() instanceof UserDetails ud && ud.getId() != null
            && ud.getUserType() != null) {
            return Actor.builder().id(ud.getId()).type(ud.getUserType()).build();
        }
        return Actor.builder().id("anonymous").type(UserType.USER).build();
    }

    private static String failureReason(MfaChallengeOutcome outcome) {
        return switch (outcome) {
            case INVALID_OTP -> "invalid_otp";
            case INVALID_BACKUP_CODE -> "invalid_backup_code";
            case CHALLENGE_EXPIRED -> "challenge_expired";
            case CHALLENGE_SESSION_INVALID, SUBJECT_NOT_BOUND -> "challenge_session_invalid";
            default -> outcome.reason();
        };
    }

    public record ChallengeRequest(String challengeId, String code, String backupCode) {
    }
}
