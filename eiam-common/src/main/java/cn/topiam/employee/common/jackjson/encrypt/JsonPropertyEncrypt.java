/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.jackjson.encrypt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JacksonAnnotation;

/**
 * Encrypt
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/12/22 21:53
 */
@JacksonAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
public @interface JsonPropertyEncrypt {
    JsonEncryptType serializer() default JsonEncryptType.ENCRYPT;

    JsonEncryptType deserializer() default JsonEncryptType.DECRYPT;
}
