/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.entity.app;

import org.hibernate.annotations.SoftDelete;

import cn.topiam.employee.support.repository.SoftDeleteConverter;
import cn.topiam.employee.support.repository.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.persistence.*;
import static cn.topiam.employee.support.repository.base.BaseEntity.IS_DELETED_COLUMN;

/**
 * 应用组关联
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023年09月06日22:03:21
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity
@Table(name = "eiam_app_group_association")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class AppGroupAssociationEntity extends BaseEntity {

    /**
     * 应用组ID
     */
    @Column(name = "group_id")
    private String    groupId;

    /**
     * 应用
     */
    @ManyToOne
    @JoinColumn(name = "app_id")
    private AppEntity app;
}
