/*
 * eiam-portal - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.portal.controller.login;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.topiam.employee.portal.pojo.result.LoginConfigResult;
import cn.topiam.employee.portal.service.LoginConfigService;
import cn.topiam.employee.support.result.ApiRestResult;

import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.topiam.employee.common.constant.AuthnConstants.LOGIN_CONFIG;

/**
 * 登录配置 Endpoint
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/8/3 23:15
 */
@Tag(name = "登录配置")
@Slf4j
@RestController
@RequestMapping(value = LOGIN_CONFIG, method = RequestMethod.GET)
public class LoginConfigController {

    @GetMapping
    public ApiRestResult<LoginConfigResult> getLoginConfig() {
        return ApiRestResult.ok(loginConfigService.getLoginConfig());
    }

    /**
     * IdpService
     */
    private final LoginConfigService loginConfigService;

    public LoginConfigController(LoginConfigService loginConfigService) {
        this.loginConfigService = loginConfigService;
    }

}
