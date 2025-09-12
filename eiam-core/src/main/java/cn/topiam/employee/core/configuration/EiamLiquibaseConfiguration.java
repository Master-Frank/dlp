/*
 * eiam-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.core.configuration;

import java.io.Serializable;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;

import liquibase.integration.spring.SpringLiquibase;

/**
 * liquibase配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/7/28 21:52
 */
@Configuration
public class EiamLiquibaseConfiguration implements Serializable {

    /**
     * TopIAM Liquibase
     *
     * @param dataSource {@link DataSource}
     * @return {@link SpringLiquibase}
     */
    @Bean
    public SpringLiquibase topIamLiquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath*:db/eiam-changelog-master.xml");
        liquibase.setDataSource(dataSource);
        liquibase.setShouldRun(true);
        liquibase.setResourceLoader(new DefaultResourceLoader());
        liquibase.setDatabaseChangeLogTable("eiam_changelog_table");
        liquibase.setDatabaseChangeLogLockTable("eiam_changelog_lock_table");
        return liquibase;
    }
}
