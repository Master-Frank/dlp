/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.enums.app.converter;

import java.util.Objects;

import cn.topiam.employee.common.enums.app.JwtBindingType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2022/5/22 23:25
 */
@Converter(autoApply = true)
public class JwtBindingTypeConverter implements AttributeConverter<JwtBindingType, String> {
    @Override
    public String convertToDatabaseColumn(JwtBindingType attribute) {
        if (Objects.isNull(attribute)) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public JwtBindingType convertToEntityAttribute(String dbData) {
        return JwtBindingType.getType(dbData);
    }
}
