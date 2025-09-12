/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.annotation;

import java.lang.annotation.*;

import cn.topiam.employee.audit.event.type.EventType;

/**
 * Audit
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/28 21:56
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Audit {

    /**
     * 类型
     *
     * @return {@link Class}
     */
    EventType type();

    /**
     * 审计内容 支持SPEL表达式
     *
     * @return {@link String}
     */
    String content() default "";
}
