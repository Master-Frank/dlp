/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.controller.app;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.topiam.employee.console.pojo.query.app.AppCertQuery;
import cn.topiam.employee.console.pojo.result.app.AppCertListResult;
import cn.topiam.employee.console.service.app.AppCertService;
import cn.topiam.employee.support.result.ApiRestResult;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.topiam.employee.common.constant.AppConstants.APP_PATH;

/**
 * 应用证书
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/31 21:38
 */
@Validated
@Tag(name = "应用证书")
@RestController
@AllArgsConstructor
@RequestMapping(value = APP_PATH + "/cert", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppCertController {

    /**
     * 获取应用证书列表
     *
     * @param query {@link AppCertQuery}
     * @return {@link AppCertListResult}
     */
    @Operation(summary = "获取应用证书列表")
    @GetMapping(value = "/list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.topiam.employee.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<List<AppCertListResult>> getAppCertListResult(@Validated AppCertQuery query) {
        List<AppCertListResult> list = appCertService.getAppCertListResult(query);
        return ApiRestResult.<List<AppCertListResult>> builder().result(list).build();
    }

    private final AppCertService appCertService;
}
