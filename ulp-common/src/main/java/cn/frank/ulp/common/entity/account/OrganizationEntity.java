/*
 * ulp-common - United Login Platform
 * Copyright (c) 2022-Present Frank Zhang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.frank.ulp.common.entity.account;

import java.io.Serial;
import java.util.Objects;

import org.hibernate.Hibernate;
import org.hibernate.annotations.SoftDelete;

import cn.frank.ulp.common.enums.account.OrganizationType;
import cn.frank.ulp.support.repository.SoftDeleteConverter;
import cn.frank.ulp.support.repository.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import static cn.frank.ulp.support.repository.base.BaseEntity.IS_DELETED_COLUMN;

/**
 * <p>
 * 组织架构
 * </p>
 *
 * @author Frank Zhang Automatic generated
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "ulp_organization")
@SoftDelete(columnName = IS_DELETED_COLUMN, converter = SoftDeleteConverter.class)
public class OrganizationEntity extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 8143944323232082295L;

    /**
     * 组织机构名称
     */
    @Column(name = "name_")
    private String            name;

    /**
     * 机构编码
     */
    @Column(name = "code_")
    private String            code;

    /**
     * 类型
     */
    @Column(name = "type_")
    private OrganizationType  type;

    /**
     * 上级ID
     */
    @Column(name = "parent_id")
    private String            parentId;

    /**
     * 路径枚举ID
     */
    @Column(name = "path_")
    private String            path;

    /**
     * 路径显示名称
     */
    @Column(name = "display_path")
    private String            displayPath;

    /**
     * 外部ID
     */
    @Column(name = "external_id")
    private String            externalId;

    /**
     * 数据来源
     */
    @Column(name = "data_origin")
    private String            dataOrigin;

    /**
     * 身份源id
     */
    @Column(name = "identity_source_id")
    private String            identitySourceId;

    /**
     * 排序
     */
    @Column(name = "order_")
    private Long              order;

    /**
     * 是否叶子节点 leaf
     */
    @Column(name = "is_leaf")
    private Boolean           leaf;

    /**
     * 是否启用
     */
    @Column(name = "is_enabled")
    private Boolean           enabled;

    /**
     * 是否强制成员启用 MFA (OR 语义, 不沿父链)
     */
    @Column(name = "mfa_enforced")
    private Boolean           mfaEnforced;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        OrganizationEntity entity = (OrganizationEntity) o;
        return getId() != null && Objects.equals(getId(), entity.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
