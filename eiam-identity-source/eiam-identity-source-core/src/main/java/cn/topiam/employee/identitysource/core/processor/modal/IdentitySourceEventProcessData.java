/*
 * eiam-identity-source-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.core.processor.modal;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import cn.topiam.employee.common.enums.identitysource.IdentitySourceProvider;
import cn.topiam.employee.identitysource.core.enums.IdentitySourceEventReceiveType;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 身份源回调事件数据
 *
 * @author TopIAM
 */
@Data
@AllArgsConstructor
public class IdentitySourceEventProcessData<T> implements Serializable {

    private String                         id;

    /**
     * 数据
     */
    private List<T>                        data;

    /**
     * 身份提供商
     */
    private IdentitySourceProvider         provider;

    /**
     * 时间事件
     */
    private LocalDateTime                  eventTime;

    /**
     * 事件类型
     */
    private IdentitySourceEventReceiveType eventType;

}
