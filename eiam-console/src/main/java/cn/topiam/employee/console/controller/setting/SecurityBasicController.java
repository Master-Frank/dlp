/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.controller.setting;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.topiam.employee.audit.annotation.Audit;
import cn.topiam.employee.audit.event.type.EventType;
import cn.topiam.employee.console.pojo.result.setting.SecurityBasicConfigResult;
import cn.topiam.employee.console.pojo.save.setting.SecurityBasicSaveParam;
import cn.topiam.employee.console.service.setting.SecuritySettingService;
import cn.topiam.employee.support.demo.Preview;
import cn.topiam.employee.support.lock.Lock;
import cn.topiam.employee.support.result.ApiRestResult;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.topiam.employee.common.constant.SettingConstants.SECURITY_PATH;

/**
 * 安全设置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/10/4 21:37
 */
@Validated
@Tag(name = "安全基础设置")
@RestController
@RequestMapping(value = SECURITY_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class SecurityBasicController {

    /**
     * 获取高级配置
     *
     * @return {@link SecurityBasicConfigResult}
     */
    @Operation(summary = "获取基础配置")
    @GetMapping(value = "/basic/config")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.topiam.employee.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<SecurityBasicConfigResult> getBasicConfig() {
        SecurityBasicConfigResult result = securitySettingService.getBasicConfig();
        return ApiRestResult.<SecurityBasicConfigResult> builder().result(result).build();
    }

    /**
     * 保存高级配置
     *
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "保存基础配置")
    @Audit(type = EventType.SAVE_LOGIN_SECURITY_BASIC_SETTINGS)
    @PostMapping(value = "/basic/save")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.topiam.employee.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> save(@Validated @RequestBody SecurityBasicSaveParam param) {
        return ApiRestResult.<Boolean> builder()
            .result(securitySettingService.saveBasicConfig(param)).build();
    }

    /**
     * SecuritySettingService
     */
    private final SecuritySettingService securitySettingService;
}
