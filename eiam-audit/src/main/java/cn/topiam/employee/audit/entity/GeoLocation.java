/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.entity;

import java.io.Serial;
import java.io.Serializable;

import cn.topiam.employee.support.geo.GeoLocationProvider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地理位置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/11/5 23:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeoLocation implements Serializable {

    @Serial
    private static final long   serialVersionUID           = -1144169992714000310L;

    public static final String  GEO_LOCATION_PROVINCE_CODE = "geo_location.province_code.keyword";

    /**
     * IP
     */
    private String              ip;

    /**
     * continent code
     */
    private String              continentCode;

    /**
     * continent Name
     */
    private String              continentName;

    /**
     * 国家code
     */
    private String              countryCode;

    /**
     * 国家名称
     */
    private String              countryName;

    /**
     * 省份code
     */
    private String              provinceCode;

    /**
     * 省份
     */
    private String              provinceName;

    /**
     * 城市code
     */
    private String              cityCode;

    /**
     * 城市名称
     */
    private String              cityName;

    /**
     * 地理坐标
     */
    private GeoPoint            point;

    /**
     * 提供商
     */
    private GeoLocationProvider provider;

}
