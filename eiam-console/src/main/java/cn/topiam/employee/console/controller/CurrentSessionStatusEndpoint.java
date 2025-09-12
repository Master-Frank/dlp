/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.controller;

import java.io.Serializable;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.topiam.employee.console.authentication.AuthenticationTrustResolverImpl;
import cn.topiam.employee.support.result.ApiRestResult;
import cn.topiam.employee.support.security.password.authentication.NeedChangePasswordAuthenticationToken;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.media.Schema;
import static cn.topiam.employee.common.constant.SessionConstants.CURRENT_STATUS;
import static cn.topiam.employee.support.security.constant.SecurityConstants.REQUIRE_RESET_PASSWORD;

/**
 * 会话状态
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/7/30 21:09
 */
@Slf4j
@RestController
@RequestMapping
public class CurrentSessionStatusEndpoint {
    private final AuthenticationTrustResolver delegate = new AuthenticationTrustResolverImpl();

    /**
     * 获取当前登录状态
     *
     * @return {@link CurrentStatusResult}
     */
    @GetMapping(value = CURRENT_STATUS)
    public ApiRestResult<CurrentStatusResult> getCurrentStatus() {
        CurrentStatusResult.CurrentStatusResultBuilder builder = CurrentStatusResult.builder();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        builder.authenticated(authentication.isAuthenticated());
        if (delegate.isAnonymous(authentication)) {
            builder.authenticated(false);
        }
        if (authentication instanceof NeedChangePasswordAuthenticationToken) {
            builder.status(REQUIRE_RESET_PASSWORD);
        }
        return ApiRestResult.ok(builder.build());
    }

    /**
     * 当前状态返回结果
     *
     * @author TopIAM
     * Created by support@topiam.cn on 2020/10/26 21:16
     */
    @Data
    @Builder
    @Schema(description = "当前状态响应")
    public static class CurrentStatusResult implements Serializable {
        /**
         * 是否认证
         */
        private Boolean authenticated;

        /**
         * status
         */
        private String  status;
    }
}
