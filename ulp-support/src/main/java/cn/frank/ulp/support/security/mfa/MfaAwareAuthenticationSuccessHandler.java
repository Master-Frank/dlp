/*
 * ulp-support - ULP support library
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
package cn.frank.ulp.support.security.mfa;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import cn.frank.ulp.support.result.ApiRestResult;
import cn.frank.ulp.support.security.authentication.WebAuthenticationDetails;
import cn.frank.ulp.support.security.constant.SecurityConstants;
import cn.frank.ulp.support.util.HttpResponseUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Form-login success handler wrapper that gates the underlying delegate behind an MFA
 * decision. See {@link MfaDecision} for the three branches.
 *
 * <p>Behavior:
 * <ul>
 *   <li>{@link MfaDecision#DIRECT_LOGIN} — straight pass-through to the delegate; nothing
 *       MFA-specific is touched.
 *   <li>{@link MfaDecision#CHALLENGE_REQUIRED} — the {@link Authentication} is parked in
 *       {@link MfaPendingAuthenticationStore} under a UUID, the security context is
 *       <b>actively erased</b> from the session (because Spring Security's
 *       {@code UsernamePasswordAuthenticationFilter.successfulAuthentication} has already
 *       saved it to the {@link HttpSessionSecurityContextRepository} by the time we run
 *       — silently dropping the delegate is not enough), an {@code HttpOnly Secure
 *       SameSite=Strict} cookie carrying the UUID is set, and a JSON body announcing
 *       {@code mfa_required} + {@code challenge_id} is flushed.
 *   <li>{@link MfaDecision#SETUP_REQUIRED} — Phase 4 portal-only path. Console strategies
 *       MUST never emit this; if they do (programming error), we fall back to delegate +
 *       attach the {@code mfa_setup_required} status so the response is at least
 *       interpretable client-side.
 * </ul>
 *
 * <p>The handler is deployable-agnostic — both console and portal SecurityConfigurations
 * wrap their own success handler with this one, supplying a {@link MfaTriggerStrategy}
 * that knows how to look up the subject's {@code mfa_enabled} flag (and, for portal,
 * the org-level enforcement bit).
 */
public class MfaAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthenticationSuccessHandler  delegate;
    private final MfaTriggerStrategy            strategy;
    private final MfaPendingAuthenticationStore pendingStore;
    private final SecurityContextRepository     securityContextRepository;

    public MfaAwareAuthenticationSuccessHandler(AuthenticationSuccessHandler delegate,
                                                MfaTriggerStrategy strategy,
                                                MfaPendingAuthenticationStore pendingStore) {
        this(delegate, strategy, pendingStore, new HttpSessionSecurityContextRepository());
    }

    public MfaAwareAuthenticationSuccessHandler(AuthenticationSuccessHandler delegate,
                                                MfaTriggerStrategy strategy,
                                                MfaPendingAuthenticationStore pendingStore,
                                                SecurityContextRepository securityContextRepository) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
        this.strategy = Objects.requireNonNull(strategy, "strategy");
        this.pendingStore = Objects.requireNonNull(pendingStore, "pendingStore");
        this.securityContextRepository = Objects.requireNonNull(securityContextRepository,
            "securityContextRepository");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException,
                                                                       ServletException {
        MfaDecision decision = strategy.decide(authentication);
        switch (decision) {
            case DIRECT_LOGIN ->
                delegate.onAuthenticationSuccess(request, response, authentication);
            case CHALLENGE_REQUIRED -> requireChallenge(request, response, authentication);
            case SETUP_REQUIRED -> requireSetup(request, response, authentication);
        }
    }

    private void requireChallenge(HttpServletRequest request, HttpServletResponse response,
                                  Authentication authentication) {
        String sourceIp = resolveSourceIp(request, authentication);
        String challengeId = pendingStore.stash(authentication, sourceIp);

        // The primary-auth filter already wrote the SecurityContext to session — clear it
        // so a stolen cookie (without the second factor) doesn't yield an authenticated
        // session.
        SecurityContext empty = SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.setContext(empty);
        securityContextRepository.saveContext(empty, request, response);
        SecurityContextHolder.clearContext();

        response.addCookie(buildPendingCookie(request, challengeId));

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("mfa_required", true);
        payload.put("challenge_id", challengeId);
        HttpResponseUtils.flushResponseJson(response, HttpStatus.OK.value(),
            ApiRestResult.<Map<String, Object>> builder().result(payload)
                .status(SecurityConstants.MFA_REQUIRED).build());
    }

    private void requireSetup(HttpServletRequest request, HttpServletResponse response,
                              Authentication authentication) throws IOException, ServletException {
        // Portal-only branch (Phase 4). Falling through to delegate keeps the session
        // authenticated so the user can reach /mfa/setup; the org-enforcement filter
        // gates every other path. Console strategies MUST NOT return SETUP_REQUIRED — if
        // they do (programming error), this is still a correct best-effort.
        delegate.onAuthenticationSuccess(request, response, authentication);
    }

    private Cookie buildPendingCookie(HttpServletRequest request, String challengeId) {
        Cookie cookie = new Cookie(SecurityConstants.MFA_PENDING_COOKIE, challengeId);
        cookie.setHttpOnly(true);
        cookie.setSecure(request.isSecure());
        cookie.setPath("/");
        cookie.setMaxAge((int) java.time.Duration.ofMinutes(5).getSeconds());
        cookie.setAttribute("SameSite", "Strict");
        return cookie;
    }

    private String resolveSourceIp(HttpServletRequest request, Authentication authentication) {
        Object details = authentication.getDetails();
        if (details instanceof WebAuthenticationDetails wad && wad.getGeoLocation() != null) {
            String ip = wad.getGeoLocation().getIp();
            if (ip != null && !ip.isBlank()) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
}
