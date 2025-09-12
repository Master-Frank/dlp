/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.entity.app;

import org.hibernate.annotations.SoftDelete;

import cn.topiam.employee.common.enums.app.AppPolicySubjectType;
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
 * 应用授权策略
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/6/4 21:29
 */
@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
@Table(name = "eiam_app_access_policy")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class AppAccessPolicyEntity extends BaseEntity {
    /**
     * 应用ID
     */
    @Column(name = "app_id")
    private String               appId;

    /**
     * 主体ID（用户、分组、组织机构）
     */
    @Column(name = "subject_id")
    private String               subjectId;

    /**
     * 主体类型（用户、分组、组织机构）
     */
    @Column(name = "subject_type")
    private AppPolicySubjectType subjectType;

    /**
     * 是否启用
     */
    @Column(name = "is_enabled")
    private Boolean              enabled;
}
