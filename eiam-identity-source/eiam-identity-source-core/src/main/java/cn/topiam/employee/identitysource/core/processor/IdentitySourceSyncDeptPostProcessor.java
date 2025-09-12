/*
 * eiam-identity-source-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.core.processor;

import java.time.LocalDateTime;
import java.util.List;

import cn.topiam.employee.common.enums.TriggerType;
import cn.topiam.employee.identitysource.core.domain.Dept;

/**
 * 身份源数据 pull post 处理器
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/1 22:04
 */
public interface IdentitySourceSyncDeptPostProcessor {
    /**
     * 处理数据
     *
     * @param batch {@link String}
     * @param identitySourceId {@link String}
     * @param deptList {@link  List}
     * @param startTime {@link  LocalDateTime}
     * @param triggerType {@link  TriggerType}
     */
    void process(String batch, String identitySourceId, List<Dept> deptList,
                 LocalDateTime startTime, TriggerType triggerType);

}
