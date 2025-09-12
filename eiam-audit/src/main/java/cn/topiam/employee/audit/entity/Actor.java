/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.entity;

import java.io.Serial;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * Actor
 * @author TopIAM
 * Created by support@topiam.cn on 2022/11/5 23:30
 */
@Data
@Builder
public class Actor implements Serializable {

    public static final String                                       ACTOR_ID         = "actor.id.keyword";
    public static final String                                       ACTOR_TYPE       = "actor.type.keyword";

    public static final String                                       ACTOR_AUTH_TYPE  = "actor.auth_type.keyword";

    @Serial
    private static final long                                        serialVersionUID = -1144169992714000310L;
    /**
     * 行动者ID
     */
    @NonNull
    private String                                                   id;

    /**
     * 行动者类型
     */
    @NonNull
    private cn.topiam.employee.support.security.userdetails.UserType type;

    /**
     * 身份验证类型
     */
    private String                                                   authType;
}
