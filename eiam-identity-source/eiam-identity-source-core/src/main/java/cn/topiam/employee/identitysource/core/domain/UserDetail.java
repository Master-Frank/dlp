/*
 * eiam-identity-source-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.core.domain;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户详情
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/28 23:03
 */
@Data
@NoArgsConstructor
public class UserDetail implements Serializable {

    /**
     * 姓名
     */
    private String name;

    /**
     * 昵称
     */
    private String nickName;
}
