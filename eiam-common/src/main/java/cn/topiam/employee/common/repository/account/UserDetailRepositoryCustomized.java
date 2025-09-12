/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.account;

import java.util.ArrayList;
import java.util.List;

import cn.topiam.employee.common.entity.account.UserDetailEntity;

/**
 * User Detail Repository Customized
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/12/29 21:27
 */
public interface UserDetailRepositoryCustomized {
    /**
     * 批量新增
     *
     * @param list {@link List}
     */
    void batchSave(List<UserDetailEntity> list);

    /**
     * 批量更新
     *
     * @param list {@link List}
     */
    void batchUpdate(ArrayList<UserDetailEntity> list);
}
