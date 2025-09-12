/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.entity.account.po;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 组织 PO
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/30 21:27
 */
@Getter
@Setter
public class OrganizationMemberPO implements Serializable {
    @Serial
    private static final long serialVersionUID = -150631305460653395L;

    /**
     * 主键ID
     */
    private String            id;

    /**
     * 用户ID
     */
    private String            userId;

    /**
     * 组织ID
     */
    private String            orgId;

    /**
     * 组织名称
     */
    private String            orgName;

    /**
     * 显示路径
     */
    private String            displayPath;

    public OrganizationMemberPO(String id, String userId, String orgId, String orgName,
                                String displayPath) {
        this.id = String.valueOf(id);
        this.userId = String.valueOf(userId);
        this.orgId = orgId;
        this.orgName = orgName;
        this.displayPath = displayPath;
    }
}
