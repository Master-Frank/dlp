/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.geo;

import java.io.Serial;
import java.io.Serializable;

import cn.topiam.employee.support.geo.GeoLocationProvider;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * MaxmindProviderSetting
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/10/1 21:10
 */
@Data
public class GeoLocationProviderConfig {

    public GeoLocationProviderConfig() {
    }

    public GeoLocationProviderConfig(GeoLocationProvider provider, GeoLocationConfig config) {
        this.provider = provider;
        this.config = config;
    }

    /**
     * 平台
     */
    @NotNull(message = "平台不能为空")
    private GeoLocationProvider provider;

    /**
     * 配置
     */
    private GeoLocationConfig   config;

    @Data
    public static class GeoLocationConfig implements Serializable {
        @Serial
        private static final long serialVersionUID = 5611656522133230183L;
    }

}
