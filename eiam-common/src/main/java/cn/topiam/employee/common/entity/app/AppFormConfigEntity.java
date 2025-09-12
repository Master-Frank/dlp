/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.entity.app;

import java.io.Serializable;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.type.SqlTypes;

import cn.topiam.employee.common.enums.app.FormEncryptType;
import cn.topiam.employee.common.enums.app.FormSubmitType;
import cn.topiam.employee.support.repository.SoftDeleteConverter;
import cn.topiam.employee.support.repository.base.BaseEntity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import static cn.topiam.employee.support.repository.base.BaseEntity.IS_DELETED_COLUMN;

/**
 * APP Form 配置
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/12/13 22:31
 */
@Getter
@Setter
@ToString
@Entity
@Accessors(chain = true)
@Table(name = "eiam_app_form_config")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class AppFormConfigEntity extends BaseEntity {

    /**
     * APP ID
     */
    @Column(name = "app_id")
    private String           appId;

    /**
     * 登录URL
     */
    @Column(name = "login_url")
    private String           loginUrl;

    /**
     * 登录名属性名称
     */
    @Column(name = "username_field")
    private String           usernameField;

    /**
     * 登录密码属性名称
     */
    @Column(name = "password_field")
    private String           passwordField;

    /**
     * 登录密码加密类型
     */
    @Column(name = "password_encrypt_type")
    private FormEncryptType  passwordEncryptType;

    /**
     * 登录密码加密秘钥
     */
    @Column(name = "password_encrypt_key")
    private String           passwordEncryptKey;

    /**
     * 用户名加密类型
     */
    @Column(name = "username_encrypt_type")
    private FormEncryptType  usernameEncryptType;

    /**
     * 用户名加密秘钥
     */
    @Column(name = "username_encrypt_key")
    private String           usernameEncryptKey;

    /**
     * 登录提交方式
     */
    @Column(name = "submit_type")
    private FormSubmitType   submitType;

    /**
     * 登录其他信息
     */
    @Column(name = "other_field")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<OtherField> otherField;

    @Data
    @Schema(description = "表单其他信息")
    public static class OtherField implements Serializable {

        private String fieldName;

        private String fieldValue;
    }
}
