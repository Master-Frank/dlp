/*
 * eiam-console - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.console.controller.identitysource;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.topiam.employee.console.pojo.query.identity.IdentitySourceEventRecordListQuery;
import cn.topiam.employee.console.pojo.result.identitysource.IdentitySourceEventRecordListResult;
import cn.topiam.employee.console.service.identitysource.IdentitySourceEventRecordService;
import cn.topiam.employee.support.repository.page.domain.Page;
import cn.topiam.employee.support.repository.page.domain.PageModel;
import cn.topiam.employee.support.result.ApiRestResult;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.topiam.employee.common.constant.AccountConstants.IDENTITY_SOURCE_PATH;

/**
 * 身份源事件记录
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/7/11 21:18
 */
@Validated
@Tag(name = "身份源事件")
@RestController
@AllArgsConstructor
@RequestMapping(value = IDENTITY_SOURCE_PATH
                        + "/event", produces = MediaType.APPLICATION_JSON_VALUE)
public class IdentitySourceEventController {

    /**
     * 身份源事件列表
     *
     * @return {@link IdentitySourceEventRecordListResult}
     */
    @Operation(summary = "身份源事件列表")
    @GetMapping(value = "/record_list")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.topiam.employee.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Page<IdentitySourceEventRecordListResult>> getIdentitySourceSynchronizeRecordList(PageModel pageModel,
                                                                                                           IdentitySourceEventRecordListQuery query) {
        Page<IdentitySourceEventRecordListResult> results = identitySourceEventRecordService
            .getIdentitySourceEventRecordList(query, pageModel);
        return ApiRestResult.<Page<IdentitySourceEventRecordListResult>> builder().result(results)
            .build();
    }

    /**
     * 身份源同步Service
     */
    private final IdentitySourceEventRecordService identitySourceEventRecordService;
}
