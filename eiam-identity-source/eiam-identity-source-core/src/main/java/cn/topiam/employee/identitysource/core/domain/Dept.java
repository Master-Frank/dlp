/*
 * eiam-identity-source-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.identitysource.core.domain;

import java.io.Serializable;
import java.util.List;

import org.springframework.util.CollectionUtils;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部门
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/2/28 23:03
 */
@Data
@NoArgsConstructor
public class Dept implements Serializable {

    /**
     * 部门id
     */
    private String     deptId;

    /**
     * 部门父id
     */
    private String     parentId;

    /**
     * 部门名称
     */
    private String     name;

    /**
     * 部门排序
     */
    private Long       order;

    /**
     * 子节点
     */
    private List<Dept> children;

    public boolean isLeaf() {
        return CollectionUtils.isEmpty(children);
    }
}
