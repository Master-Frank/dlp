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
import cn.topiam.employee.console.pojo.result.setting.GeoIpProviderResult;
import cn.topiam.employee.console.pojo.save.setting.GeoIpProviderSaveParam;
import cn.topiam.employee.console.service.setting.GeoLocationSettingService;
import cn.topiam.employee.support.context.ApplicationContextService;
import cn.topiam.employee.support.demo.Preview;
import cn.topiam.employee.support.geo.GeoLocation;
import cn.topiam.employee.support.lock.Lock;
import cn.topiam.employee.support.result.ApiRestResult;

import lombok.AllArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import static cn.topiam.employee.common.constant.ConfigBeanNameConstants.GEO_LOCATION;
import static cn.topiam.employee.common.constant.SettingConstants.SETTING_PATH;
import static cn.topiam.employee.core.setting.GeoIpProviderConstants.IPADDRESS_SETTING_NAME;

/**
 * IP地址库
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/13 22:09
 */
@Validated
@Tag(name = "IP地址库")
@RestController
@AllArgsConstructor
@RequestMapping(value = SETTING_PATH + "/geo_ip", produces = MediaType.APPLICATION_JSON_VALUE)
public class GeoIpLibraryController {
    /**
     * 获取配置
     *
     * @return {@link  GeoIpProviderResult}
     */
    @Operation(summary = "获取地理位置配置")
    @GetMapping("config")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.topiam.employee.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<GeoIpProviderResult> getGeoIpLibrary() {
        return ApiRestResult.<GeoIpProviderResult> builder()
            .result(geoLocationSettingService.getGeoIpLibrary()).build();
    }

    /**
     * 保存配置
     *
     * @param param {@link  GeoIpProviderSaveParam}
     * @return {@link  Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "保存地理位置配置")
    @Audit(type = EventType.SAVE_GEO_LOCATION_SERVICE)
    @PostMapping("save")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.topiam.employee.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> saveGeoIpLibrary(@RequestBody @Validated GeoIpProviderSaveParam param) {
        return ApiRestResult.<Boolean> builder()
            .result(geoLocationSettingService.saveGeoIpLibrary(param)).build();
    }

    /**
     * 禁用地理位置服务
     *
     * @return {@link Boolean}
     */
    @Lock
    @Preview
    @Operation(summary = "禁用地理位置服务")
    @Audit(type = EventType.OFF_GEO_LOCATION_SERVICE)
    @PutMapping("/disable")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.topiam.employee.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<Boolean> disableGeoIpLibrary() {
        Boolean setting = geoLocationSettingService.removeSetting(IPADDRESS_SETTING_NAME);
        // refresh
        ApplicationContextService.refresh(GEO_LOCATION);
        return ApiRestResult.<Boolean> builder().result(setting).build();
    }

    /**
     * 查询IP地址位置信息
     *
     * @param ip {@link  String}
     * @return {@link  ApiRestResult}
     */
    @Operation(summary = "查询IP地址位置信息")
    @GetMapping(value = "/get_location")
    @PreAuthorize(value = "authenticated and @sae.hasAuthority(T(cn.topiam.employee.support.security.userdetails.UserType).ADMIN)")
    public ApiRestResult<GeoLocation> getGeoLocation(@Parameter(description = "IP") String ip) {
        return ApiRestResult.<GeoLocation> builder()
            .result(geoLocationSettingService.getGeoLocation(ip)).build();
    }

    private GeoLocationSettingService geoLocationSettingService;

}
