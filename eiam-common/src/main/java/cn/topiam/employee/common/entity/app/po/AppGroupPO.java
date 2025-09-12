/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.entity.app.po;

import java.time.LocalDateTime;

import cn.topiam.employee.common.entity.app.AppGroupEntity;
import cn.topiam.employee.common.enums.app.AppGroupType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/12/13 23:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AppGroupPO extends AppGroupEntity {

    /**
     * 应用数量
     */
    private String appCount;

    public AppGroupPO(String id, String name, String code, AppGroupType type,
                      LocalDateTime createTime, String remark, Long appCount) {
        super.setId(id);
        super.setName(name);
        super.setCode(code);
        super.setType(type);
        super.setCreateTime(createTime);
        super.setRemark(remark);
        this.appCount = String.valueOf(appCount);
    }

}
