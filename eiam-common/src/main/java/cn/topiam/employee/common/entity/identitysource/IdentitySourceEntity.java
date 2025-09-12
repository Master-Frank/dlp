/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.entity.identitysource;

import java.io.Serial;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.type.SqlTypes;

import cn.topiam.employee.common.entity.identitysource.config.JobConfig;
import cn.topiam.employee.common.entity.identitysource.config.StrategyConfig;
import cn.topiam.employee.common.enums.identitysource.IdentitySourceProvider;
import cn.topiam.employee.support.repository.SoftDeleteConverter;
import cn.topiam.employee.support.repository.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import static cn.topiam.employee.support.repository.base.BaseEntity.IS_DELETED_COLUMN;

/**
 * <p>
 * 社交身份认证源配置
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-08-16
 */
@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
@Table(name = "eiam_identity_source")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class IdentitySourceEntity extends BaseEntity {

    @Serial
    private static final long      serialVersionUID = -7936931011805155568L;

    public static final String     NAME_FIELD_NAME  = "name";

    /**
     * 名称
     */
    @Column(name = "name_")
    private String                 name;

    /**
     * 唯一CODE 不可修改
     */
    @Column(name = "code_")
    private String                 code;

    /**
     * 平台
     */
    @Column(name = "provider_")
    private IdentitySourceProvider provider;

    /**
     * 基础配置JSON串
     */
    @Column(name = "basic_config")
    private String                 basicConfig;

    /**
     * 同步策略JSON串
     */
    @Column(name = "strategy_config")
    @JdbcTypeCode(SqlTypes.JSON)
    private StrategyConfig         strategyConfig;

    /**
     * 执行计划JSON串
     */
    @Column(name = "job_config")
    @JdbcTypeCode(SqlTypes.JSON)
    private JobConfig              jobConfig;

    /**
     * 是否启用
     */
    @Column(name = "is_enabled")
    private Boolean                enabled;

    /**
     * 是否已配置
     */
    @Column(name = "is_configured")
    private Boolean                configured;

}
