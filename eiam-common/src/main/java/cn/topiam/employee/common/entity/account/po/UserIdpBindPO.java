/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.entity.account.po;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * 用户身份提供商绑定 PO
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/10 22:46
 */
@Data
public class UserIdpBindPO implements Serializable {
    /**
     * id
     */
    private String        id;

    /**
     * 用户ID
     */
    private String        userId;

    /**
     * 用户名称
     */
    private String        userName;

    /**
     * 昵称
     */
    private String        nickName;

    /**
     * 个人邮箱
     */
    private String        email;

    /**
     * 手机号对应的国家号
     */
    private String        stateCode;

    /**
     * 手机号
     */
    private String        mobile;

    /**
     * 头像url
     */
    private String        avatarUrl;

    /**
     * OpenId
     */
    private String        openId;

    /**
     * unionId
     */
    public String         unionId;

    /**
     * 身份提供商 ID
     */
    private String        idpId;

    /**
     * 身份认证提供商名称
     */
    private String        idpName;

    /**
     * 身份提供商 类型
     */
    private String        idpType;

    /**
     * 绑定时间
     */
    private LocalDateTime bindTime;

    public UserIdpBindPO(String id, String userId, String userName, String nickName, String email,
                         String mobile, String stateCode, String avatarUrl, String openId,
                         String unionId, String idpId, String idpName, String idpType,
                         LocalDateTime bindTime) {
        this.id = String.valueOf(id);
        this.userId = String.valueOf(userId);
        this.userName = userName;
        this.nickName = nickName;
        this.email = email;
        this.mobile = mobile;
        this.stateCode = stateCode;
        this.avatarUrl = avatarUrl;
        this.openId = openId;
        this.unionId = unionId;
        this.idpId = String.valueOf(idpId);
        this.idpName = idpName;
        this.idpType = idpType;
        this.bindTime = bindTime;
    }
}
