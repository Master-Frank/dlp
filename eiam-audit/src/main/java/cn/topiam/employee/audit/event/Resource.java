/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.event;

import lombok.Data;

@Data
public abstract class Resource {

    /**
     * 资源编码
     */
    private String code;
    /**
     * 资源名称
     */
    private String name;

}
