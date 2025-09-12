/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.enums.app.converter;

import java.util.Objects;

import cn.topiam.employee.common.enums.app.JwtIdTokenSubjectType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/22 23:25
 */
@Converter(autoApply = true)
public class JwtIdTokenSubjectTypeConverter implements
                                            AttributeConverter<JwtIdTokenSubjectType, String> {
    @Override
    public String convertToDatabaseColumn(JwtIdTokenSubjectType attribute) {
        if (Objects.isNull(attribute)) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public JwtIdTokenSubjectType convertToEntityAttribute(String dbData) {
        return JwtIdTokenSubjectType.getType(dbData);
    }
}
