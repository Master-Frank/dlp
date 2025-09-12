/*
 * eiam-identity-source-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.core.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 用户模型
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/28 23:03
 */
@Data
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 8567794739261358220L;
    /**
     * 用户ID
     */
    private String            userId;

    /**
     * 头像地址
     */
    private String            avatar;

    /**
     * 国际电话区号 86
     */
    private String            phoneAreaCode;

    /**
     * 手机号
     */
    private String            phone;

    /**
     * 邮箱
     */
    private String            email;

    /**
     * 公司邮箱
     */
    private String            orgEmail;

    /**
     * 所属部门ID列表
     */
    private List<String>      deptIdList;

    /**
     * 状态  true:启用 false:未启用
     */
    private Boolean           active;

    /**
     * 用户详情
     */
    private UserDetail        userDetail;

}
