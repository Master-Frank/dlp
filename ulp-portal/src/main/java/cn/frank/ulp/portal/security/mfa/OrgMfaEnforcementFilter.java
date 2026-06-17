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
package cn.frank.ulp.portal.security.mfa;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import cn.frank.ulp.common.security.mfa.OrgMfaPolicyService;
import cn.frank.ulp.portal.service.security.UserMfaService;
import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.userdetails.UserType;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Phase 4.1 — 组织级 MFA 强制门。
 *
 * <p>装配位置：{@code SecurityContextHolderFilter}（Spring Security 7 把
 * {@code SecurityContextPersistenceFilter} 改了名）之后、{@code AuthorizationFilter}
 * 之前。需要登录已恢复（拿到 {@link Authentication}）但又要在授权决策前拦截，给
 * 已认证但"该绑没绑"的 portal 用户一个统一的 403 出口，让前端把请求重定向到
 * {@code /mfa/setup}。
 *
 * <p>拦截条件（必须 <b>全部</b> 满足）：
 * <ol>
 *   <li>{@link Authentication#isAuthenticated()} == true 且非匿名</li>
 *   <li>principal 是 {@link UserDetails}，{@code userType == "user"}（admin 走 console，
 *       不参与组织强制；admin 是否启用 MFA 完全自愿）</li>
 *   <li>{@link UserMfaService#loadActiveCipher(String)} == null
 *       （等价于 {@code mfa_enabled = false}，强制门只针对未绑定用户）</li>
 *   <li>{@link OrgMfaPolicyService#isUserEnforced(String)} == true
 *       （用户所在任一组织开了 {@code mfa_enforced} 位）</li>
 *   <li>请求路径不在 {@link #whitelist} 白名单内（白名单包含绑定流程、challenge、
 *       静态资源、错误页等"必须放过的"路径，否则 portal 直接陷死循环）</li>
 * </ol>
 *
 * <p>命中拦截：写 403 + Content-Type {@code application/json} + body
 * {@code {"error":"mfa_setup_required","reason":"org_policy"}}；不调
 * {@link FilterChain#doFilter}。前端拦截器看到 {@code error=mfa_setup_required}
 * 即跳 {@code /mfa/setup} 完成绑定（Phase 7 前端任务）。
 *
 * <p>不命中：直接 {@link FilterChain#doFilter} 透传，不改 Authentication、不写
 * response headers / cookies —— 本 filter 是只读决策器，正常通过的请求看不出它存在过。
 *
 * <p>白名单设计说明：
 * <ul>
 *   <li>{@code /api/v1/mfa/bind/**} —— 绑定流程必须放过，否则用户被强制后无法完成绑定</li>
 *   <li>{@code /api/v1/mfa/challenge} —— 已主认证未二次的回路（虽然 challenge 一般针对
 *       已绑用户走，但放过更宽容）</li>
 *   <li>{@code /mfa/setup}、{@code /mfa/challenge} —— SPA 路由白名单（实际是 portal index.html
 *       托管，但路径放过避免任何前端框架的预取行为被 403）</li>
 *   <li>{@code /logout} —— 已认证用户必须能登出，否则陷死循环</li>
 *   <li>{@code /error} —— Spring Boot 错误转发，拦它会让 4xx/5xx 全部 404</li>
 *   <li>静态资源前缀（{@code /static/**}、{@code /assets/**}、{@code /favicon.ico}、{@code /*.js}、
 *       {@code /*.css}、{@code /*.png} 等） —— 防止 SPA 资源被 403</li>
 * </ul>
 *
 * <p>实现笔记：用 {@link AntPathMatcher} 而不是 {@code requestMatchers}，因为本 filter
 * 不在 Security DSL 里；用纯 Servlet API 写出避免对 Spring MVC 的二次依赖。
 */
public class OrgMfaEnforcementFilter extends OncePerRequestFilter {

    private static final Logger       log               = LoggerFactory
        .getLogger(OrgMfaEnforcementFilter.class);

    private static final String       BLOCKED_BODY      = "{\"error\":\"mfa_setup_required\",\"reason\":\"org_policy\"}";

    private static final List<String> DEFAULT_WHITELIST = List.of(
        // bind / challenge / setup —— 强制流程闭环必经
        "/api/v1/mfa/bind/**", "/api/v1/mfa/challenge", "/mfa/setup", "/mfa/challenge",
        // 登出 / 错误页
        "/logout", "/error",
        // 静态资源 —— SPA 不能被 403 卡死
        "/favicon.ico", "/static/**", "/assets/**", "/public/**", "/*.js", "/*.css", "/*.map",
        "/*.png", "/*.jpg", "/*.svg", "/*.ico", "/*.woff", "/*.woff2", "/*.ttf");

    private final UserMfaService      userMfaService;
    private final OrgMfaPolicyService orgMfaPolicyService;
    private final List<String>        whitelist;
    private final PathMatcher         pathMatcher       = new AntPathMatcher();

    public OrgMfaEnforcementFilter(UserMfaService userMfaService,
                                   OrgMfaPolicyService orgMfaPolicyService) {
        this(userMfaService, orgMfaPolicyService, DEFAULT_WHITELIST);
    }

    public OrgMfaEnforcementFilter(UserMfaService userMfaService,
                                   OrgMfaPolicyService orgMfaPolicyService,
                                   List<String> whitelist) {
        this.userMfaService = Objects.requireNonNull(userMfaService, "userMfaService");
        this.orgMfaPolicyService = Objects.requireNonNull(orgMfaPolicyService,
            "orgMfaPolicyService");
        this.whitelist = List.copyOf(Objects.requireNonNull(whitelist, "whitelist"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (isWhitelisted(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails userDetails)) {
            filterChain.doFilter(request, response);
            return;
        }
        UserType userType = userDetails.getUserType();
        if (userType == null || !UserType.USER.getType().equalsIgnoreCase(userType.getType())) {
            // admin / 非 portal 用户不进强制门
            filterChain.doFilter(request, response);
            return;
        }
        String userId = userDetails.getId();
        if (userId == null || userId.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 已绑定的用户直接放行 —— 强制门只盯"该绑没绑"的子集
        if (userMfaService.loadActiveCipher(userId) != null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 未绑 + 组织强制 → 403 阻断
        if (orgMfaPolicyService.isUserEnforced(userId)) {
            log.info("Portal user {} blocked by org MFA policy on path {}", userId,
                request.getRequestURI());
            writeBlocked(response);
            return;
        }

        // 未绑 + 组织不强制 → 自愿用户，放过
        filterChain.doFilter(request, response);
    }

    private boolean isWhitelisted(HttpServletRequest request) {
        String path = request.getRequestURI();
        String ctx = request.getContextPath();
        if (ctx != null && !ctx.isEmpty() && path.startsWith(ctx)) {
            path = path.substring(ctx.length());
        }
        for (String pattern : whitelist) {
            if (pathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    private void writeBlocked(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (PrintWriter writer = response.getWriter()) {
            writer.write(BLOCKED_BODY);
            writer.flush();
        }
    }
}
