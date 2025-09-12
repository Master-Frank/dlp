/*
 * eiam-identity-source-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.core.processor;

import cn.topiam.employee.identitysource.core.processor.modal.IdentitySourceEventProcessData;

/**
 * 身份源数据 event 处理器
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/1 22:04
 */
public interface IdentitySourceEventPostProcessor {
    /**
     * 处理数据
     *
     * @param data  {@link  IdentitySourceEventProcessData}
     */
    void process(IdentitySourceEventProcessData<?> data);
}
