/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.event;

import java.util.List;

import cn.topiam.employee.support.security.userdetails.UserType;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 类型
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/11/24 23:06
 */
@Data
@AllArgsConstructor
public class Type {
    /**
     * 编码
     */
    private String         code;
    /**
     * 名称
     */
    private String         name;
    /**
     * 资源
     */
    private Resource       resource;
    /**
     * 用户类型
     */
    private List<UserType> userTypes;

    @Override
    public String toString() {
        return String.format("[%s](%s) %s", name, code, resource);
    }

}
