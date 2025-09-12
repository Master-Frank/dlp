/*
 * eiam-identity-source-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.core.client;

import java.util.ArrayList;
import java.util.List;

import cn.topiam.employee.identitysource.core.domain.Dept;
import cn.topiam.employee.identitysource.core.domain.User;

/**
 * IdentityProviderClient
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/28 03:39
 */
public interface IdentitySourceClient {

    /**
     * 递归获取用户
     * @param dept {@link Dept}
     * @return {@link List}
     */
    default List<User> getUserList(Dept dept) {
        List<User> list = new ArrayList<>(getUserList(dept.getDeptId()));
        List<Dept> children = dept.getChildren();
        if (!dept.isLeaf()) {
            children.forEach(item -> list.addAll(getUserList(item)));
        }
        return list;
    }

    /**
     * 根据部门ID获取部门数据
     *
     * @param id {@link List} 部门ID
     * @return {@link List} 部门数据
     */
    Dept getDept(String id);

    /**
     * 根据部门ID获取指定部门及其下的子部门（以及子部门的子部门等等，递归）
     *
     * @param id {@link List} 部门ID
     * @return {@link List} 部门数据
     */
    List<Dept> getDeptList(String id);

    /**
     * 获取所有部门数据
     *
     * @return {@link List}
     */
    List<Dept> getDeptList();

    /**
     * 根据用户ID获取用户信息
     *
     * @param id {@link String} 用户ID
     * @return {@link List} 用户信息
     */
    User getUser(String id);

    /**
     * 根据部门ID获取用户列表
     *
     * @param deptId {@link String} 科室ID
     * @return {@link List} 用户列表
     */
    List<User> getUserList(String deptId);

    /**
     * 根据全部用户列表
     *
     * @return {@link List}
     */
    List<User> getUserList();
}
