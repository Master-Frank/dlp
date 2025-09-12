/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.entity;

import java.io.Serial;
import java.io.Serializable;

import cn.topiam.employee.audit.enums.TargetType;

import lombok.*;

/**
 * Target
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/11/5 23:34
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Target implements Serializable {

    @Serial
    private static final long  serialVersionUID  = -1144169992714000310L;

    public static final String TARGET_ID_KEYWORD = "target.id.keyword";

    /**
     * 目标 ID
     */
    @NonNull
    private String             id;

    /**
     * 目标名称
     */
    @NonNull
    private String             name;
    /**
     *
     * 目标类型
     */
    @NonNull
    private TargetType         type;

    /**
     * 目标类型名称
     */
    private String             typeName;

    public String getTypeName() {
        return type.getDesc();
    }
}
