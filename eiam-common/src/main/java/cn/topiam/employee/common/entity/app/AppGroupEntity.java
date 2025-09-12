/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.entity.app;

import org.hibernate.annotations.SoftDelete;

import cn.topiam.employee.common.enums.app.AppGroupType;
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
 * 分组
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2023/8/31 11:25
 */
@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
@Table(name = "eiam_app_group")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class AppGroupEntity extends BaseEntity {

    /**
     * 分组名称
     */
    @Column(name = "name_")
    private String       name;

    /**
     * 分组编码
     */
    @Column(name = "code_")
    private String       code;

    /**
     * 分组类型
     */
    @Column(name = "type_")
    private AppGroupType type;
}
