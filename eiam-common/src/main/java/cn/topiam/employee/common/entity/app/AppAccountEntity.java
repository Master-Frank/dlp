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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import static cn.topiam.employee.support.repository.base.BaseEntity.IS_DELETED_COLUMN;

/**
 * 应用账户
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/31 21:51
 */
@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
@Table(name = "eiam_app_account")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class AppAccountEntity extends BaseEntity {
    /**
     * 应用ID
     */
    @Column(name = "app_id")
    private String  appId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private String  userId;

    /**
     * 账户名称
     */
    @Column(name = "account_")
    private String  account;

    /**
     * 账户密码
     */
    @Column(name = "password_")
    private String  password;

    /**
     * 默认的
     */
    @Column(name = "default_")
    private Boolean defaulted;
}
