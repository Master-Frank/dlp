/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.entity.setting;

import java.io.Serial;

import org.hibernate.annotations.SoftDelete;

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
 * 设置表
 * </p>
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020-10-01
 */
@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
@Table(name = "eiam_setting")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class SettingEntity extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 5983857137670090984L;
    /**
     * name
     */
    @Column(name = "name_")
    private String            name;
    /**
     * value
     */
    @Column(name = "value_")
    private String            value;
    /**
     * desc
     */
    @Column(name = "desc_")
    private String            desc;

}
