/*
 * eiam-identity-source-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.core.event;

import java.io.Serial;
import java.io.Serializable;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
     * 身份源配置事件
     *
     * @author TopIAM
     * Created by support@topiam.cn on 2022/3/20 21:58
     */
@Data
@RequiredArgsConstructor
public class IdentitySourceEvent implements Serializable {
    @Serial
    private static final long             serialVersionUID = 1099740917667842614L;
    /**
     * ID
     */
    private final String                  id;
    /**
     * 事件类型
     */
    private final IdentitySourceEventType identitySourceEventType;
}
