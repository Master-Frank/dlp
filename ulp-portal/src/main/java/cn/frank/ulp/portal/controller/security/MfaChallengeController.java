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

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.security.constant.SecurityConstants;
import cn.frank.ulp.support.security.mfa.MfaChallengeOutcome;
import cn.frank.ulp.support.security.mfa.MfaChallengeService;
import cn.frank.ulp.support.security.mfa.MfaLockoutService;

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
 *   { "backupCode": "ABCD-EFGH" }   // Phase 5 占位，目前直接返回 invalid_backup_code
 * }</pre>
 *
 * <p>challenge_id 来源优先级：cookie {@code ulp-mfa-pending} → body 字段 {@code challengeId}。
 *
 * <p>响应 / HTTP 语义见 spec.md Phase 6 audit failure_reason 对齐：
 * <ul>
 *   <li>200 {@code status=ok} — 验证通过，Authentication 已写入 session</li>
 *   <li>401 {@code status=invalid_otp} — code 不匹配，cookie 保留</li>
 *   <li>401 {@code status=invalid_backup_code} — Phase 5 占位</li>
 *   <li>401 {@code status=challenge_expired} — pending 已过期 / 不存在，cookie 清除</li>
 *   <li>401 {@code status=challenge_session_invalid} — IP 不在 /24 同段、subject 已失绑等，cookie 清除</li>
 *   <li>423 {@code status=locked_out} + {@code Retry-After} 头 — 失败计数达阈值，cookie 保留</li>
 * </ul>
 *
 * <p>端点本身放行（{@code permitAll}）—— 处于"已主认证未二次"中间态，由
 * {@link cn.frank.ulp.portal.configuration.security.PortalSecurityConfiguration} 在 chain
 * 中显式开放，安全性靠 cookie 一次性 UUID + 5 分钟 TTL + Redis pending /24 IP 绑定共同兜底。
 */
@RestController
@RequestMapping("/api/v1/mfa")
@RequiredArgsConstructor
public class MfaChallengeController {

    private static final String       STATUS_OK = "ok";

    private final MfaChallengeService challengeService;
    private final MfaLockoutService   lockoutService;

    @PostMapping("/challenge")
    public ResponseEntity<ApiRestResult<Void>> challenge(@RequestBody(required = false) ChallengeRequest body,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response) {
        ChallengeRequest payload = body == null ? new ChallengeRequest(null, null, null) : body;
        String challengeId = resolveChallengeId(payload, request);

        // Phase 5 placeholder — backup-code verification not wired yet. We surface a
        // dedicated status so the frontend can distinguish the two failure modes today and
        // not need to re-roll its error UX when Phase 5 lands.
        if (StringUtils.isNotBlank(payload.backupCode())) {
            return errorResponse(HttpStatus.UNAUTHORIZED, MfaChallengeOutcome.INVALID_BACKUP_CODE,
                false, request, response);
        }

        if (StringUtils.isBlank(challengeId) || StringUtils.isBlank(payload.code())) {
            return errorResponse(HttpStatus.UNAUTHORIZED, MfaChallengeOutcome.CHALLENGE_EXPIRED,
                true, request, response);
        }

        MfaChallengeOutcome outcome = challengeService.verifyAndCommit(challengeId, payload.code(),
            request, response);
        return switch (outcome) {
            case SUCCESS -> {
                clearPendingCookie(request, response);
                yield ResponseEntity.ok(ApiRestResult.<Void> builder().status(STATUS_OK).build());
            }
            case LOCKED_OUT -> lockedResponse(outcome, challengeId, request, response);
            case INVALID_OTP, INVALID_BACKUP_CODE -> errorResponse(HttpStatus.UNAUTHORIZED, outcome,
                false, request, response);
            case CHALLENGE_EXPIRED, CHALLENGE_SESSION_INVALID, SUBJECT_NOT_BOUND ->
                errorResponse(HttpStatus.UNAUTHORIZED, outcome, true, request, response);
        };
    }

    private ResponseEntity<ApiRestResult<Void>> lockedResponse(MfaChallengeOutcome outcome,
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
            .body(ApiRestResult.<Void> builder().status(outcome.reason()).message("locked out")
                .build());
    }

    private ResponseEntity<ApiRestResult<Void>> errorResponse(HttpStatus status,
                                                              MfaChallengeOutcome outcome,
                                                              boolean clearCookie,
                                                              HttpServletRequest request,
                                                              HttpServletResponse response) {
        if (clearCookie) {
            clearPendingCookie(request, response);
        }
        return ResponseEntity.status(status).body(ApiRestResult.<Void> builder()
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

    public record ChallengeRequest(String challengeId, String code, String backupCode) {
    }
}
