/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.geo.ip2region;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;

import cn.topiam.employee.common.enums.Country;
import cn.topiam.employee.support.geo.GeoLocation;
import cn.topiam.employee.support.geo.GeoLocationParser;
import cn.topiam.employee.support.geo.GeoLocationProvider;

import lombok.extern.slf4j.Slf4j;
import static cn.topiam.employee.common.geo.District.CITY_DISTRICT;
import static cn.topiam.employee.common.geo.District.PROVINCE_DISTRICT;

/**
 * Ip2regionGeo
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/10/30 19:11
 */
@Slf4j
public class Ip2regionGeoLocationParserImpl implements GeoLocationParser, DisposableBean {

    public static GeoLocationProvider IP2REGION        = new GeoLocationProvider("default",
        "ip2region");

    private static final String       DEFAULT_XDB_PATH = "/ip2region/ip2region.xdb";

    private final Searcher            searcher;

    public Ip2regionGeoLocationParserImpl() throws IOException {
        // 加载整个xdb到内存。
        try (InputStream in = this.getClass().getResourceAsStream(DEFAULT_XDB_PATH)) {
            Assert.notNull(in, "XDB must not be null");
            byte[] cBuff = in.readAllBytes();
            log.info("create content cached searcher");
            // 2、使用上述的 cBuff 创建一个完全基于内存的查询对象。
            searcher = Searcher.newWithBuffer(cBuff);
        }
    }

    @Override
    public GeoLocation getGeoLocation(String remote) {
        try {
            String region = searcher.search(remote);
            return buildGeoLocation(remote, region);
        } catch (Exception e) {
            log.error("failed to search({})", remote, e);
        }
        return GeoLocation.builder().provider(IP2REGION).ip(remote).build();
    }

    private GeoLocation buildGeoLocation(String remote, String region) {
        String[] geo = region.split("\\|");
        String countryName = geo[0];
        String provinceName = geo[2];
        String cityName = geo[3];
        Country country = Country.getByCountryName(countryName);
        return GeoLocation.builder().provider(IP2REGION).ip(remote)
            .countryCode(Objects.nonNull(country) ? country.getCode() : null)
            .countryName(countryName)
            .provinceCode(Objects.toString(PROVINCE_DISTRICT.get(provinceName)))
            .provinceName(provinceName).cityCode(Objects.toString(CITY_DISTRICT.get(cityName)))
            .cityName(cityName).build();
    }

    @Override
    public void destroy() throws Exception {
        if (Objects.nonNull(searcher)) {
            searcher.close();
        }
    }
}
