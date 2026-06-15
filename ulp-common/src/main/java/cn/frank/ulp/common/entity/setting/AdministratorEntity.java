/*
 * ulp-common - United Login Platform
 * Copyright (c) 2022-Present Frank Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.frank.ulp.common.entity.setting;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collection;

import org.hibernate.annotations.SoftDelete;
import org.springframework.security.core.GrantedAuthority;

import cn.frank.ulp.common.enums.UserStatus;
import cn.frank.ulp.support.repository.SoftDeleteConverter;
import cn.frank.ulp.support.repository.base.BaseEntity;
import cn.frank.ulp.support.security.userdetails.UserDetails;
import cn.frank.ulp.support.security.userdetails.UserType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import static cn.frank.ulp.support.repository.base.BaseEntity.IS_DELETED_COLUMN;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author Frank Zhang
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity
@Table(name = "ulp_administrator")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class AdministratorEntity extends BaseEntity {

    @Serial
    private static final long  serialVersionUID    = -2619231849746900857L;

    public static final String USERNAME_FIELD_NAME = "username";

    public static final String PHONE_FIELD_NAME    = "phone";

    public static final String EMAIL_FIELD_NAME    = "email";

    /**
     * 用户名
     */
    @Column(name = "username_")
    private String             username;

    /**
     * 姓名
     */
    @Column(name = "full_name")
    private String             fullName;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String             nickName;

    /**
     * 密码
     */
    @Column(name = "password_")
    private String             password;

    /**
     * 需要修改密码
     */
    @Column(name = "need_change_password")
    private Boolean            needChangePassword;

    /**
     * 邮箱
     */
    @Column(name = "email_")
    private String             email;

    /**
     * 手机号
     */
    @Column(name = "phone_")
    private String             phone;

    /**
     * 手机号
     */
    @Column(name = "phone_area_code")
    private String             phoneAreaCode;

    /**
     * 手机有效
     */
    @Column(name = "phone_verified")
    private Boolean            phoneVerified;

    /**
     * 头像URL
     */
    @Column(name = "avatar_")
    private String             avatar;

    /**
     * 状态  ENABLE:启用 DISABLE:禁用 LOCKING:锁定
     */
    @Column(name = "status_")
    private UserStatus         status;

    /**
     * 邮箱验证有效
     */
    @Column(name = "email_verified")
    private Boolean            emailVerified;

    /**
     * 认证次数
     */
    @Column(name = "auth_total")
    private Long               authTotal;
    /**
     * 上次认证IP
     */
    @Column(name = "last_auth_ip")
    private String             lastAuthIp;

    /**
     * 上次认证时间
     */
    @Column(name = "last_auth_time")
    private LocalDateTime      lastAuthTime;

    /**
     * 最后修改密码时间
     */
    @Column(name = "last_update_password_time")
    private LocalDateTime      lastUpdatePasswordTime;

    /**
     * 是否启用 MFA 第二因子
     */
    @Column(name = "mfa_enabled")
    private Boolean            mfaEnabled;

    /**
     * AES-GCM 加密后的 TOTP shared secret (Base64)
     */
    @Column(name = "totp_secret_cipher")
    private String             totpSecretCipher;

    /**
     * 备份码 Argon2id 哈希数组 (JSON)
     */
    @Column(name = "backup_codes_json")
    private String             backupCodesJson;

    /**
     * 扩展参数
     */
    @Column(name = "expand_")
    private String             expand;

    public Boolean isLocked() {
        return UserStatus.LOCKED.equals(this.getStatus())
               || UserStatus.PASSWORD_EXPIRED_LOCKED.equals(this.getStatus())
               || UserStatus.EXPIRED_LOCKED.equals(this.getStatus());
    }

    public Boolean isDisabled() {
        return UserStatus.DISABLED.equals(this.getStatus());
    }

    public UserDetails toUserDetails(Collection<GrantedAuthority> authorities) {
        //@formatter:off
        UserDetails userDetails = new UserDetails(this.getId(), this.getUsername(), this.getPassword(), UserType.ADMIN, !isDisabled(), true, true, !isLocked(), authorities);
        userDetails.setAvatar(this.getAvatar());
        userDetails.setPhone(this.getPhone());
        userDetails.setPhoneAreaCode(this.getPhoneAreaCode());
        userDetails.setPhoneVerified(this.getPhoneVerified());
        userDetails.setEmail(this.getEmail());
        userDetails.setEmailVerified(this.getEmailVerified());
        userDetails.setLastUpdatePasswordTime(this.getLastUpdatePasswordTime());
        userDetails.setFullName(this.getFullName());
        userDetails.setUpdateTime(this.getUpdateTime());
        userDetails.setNeedChangePassword(this.getNeedChangePassword());
        //@formatter:on
        return userDetails;
    }
}
