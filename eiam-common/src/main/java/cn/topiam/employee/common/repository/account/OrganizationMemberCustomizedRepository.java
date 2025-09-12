/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.account;

import java.util.List;

import cn.topiam.employee.common.entity.account.OrganizationMemberEntity;

/**
 * 组织成员
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/10/2 02:53
 */
public interface OrganizationMemberCustomizedRepository {

    /**
     * 批量保存
     *
     * @param list {@link String}
     */
    void batchSave(List<OrganizationMemberEntity> list);
}
