/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.geo.maxmind;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.topiam.employee.support.geo.GeoLocationParser;

import lombok.RequiredArgsConstructor;

/**
 * 更新 maxmind 数据库
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/12/7 22:25
 */
@Component
@RequiredArgsConstructor
public class UpdateMaxmindTaskConfiguration {
    private final Logger logger = LoggerFactory.getLogger(UpdateMaxmindTaskConfiguration.class);

    /**
     * 每天一点执行ip库文件更新
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void update() {
        try {
            if (geoLocationParser instanceof MaxmindGeoLocationParserImpl maxmindGeoLocation) {
                logger.info("执行IP库文件更新定时任务开始");
                if (maxmindGeoLocation.checkDbFileIsUpdate()) {
                    maxmindGeoLocation.download();
                }
                logger.info("执行IP库文件更新定时任务结束");
            }
        } catch (Exception e) {
            logger.error("执行IP库文件更新定时发生异常：{}", e.getMessage());
        }
    }

    private final GeoLocationParser geoLocationParser;

}
