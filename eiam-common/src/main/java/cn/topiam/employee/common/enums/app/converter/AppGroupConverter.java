/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.enums.app.converter;

import java.util.Objects;

import cn.topiam.employee.common.enums.app.AppDefaultGroup;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2023/9/9 16:23
 */
@Converter(autoApply = true)
public class AppGroupConverter implements AttributeConverter<AppDefaultGroup, String> {

    /**
     * Converts the value stored in the entity attribute into the
     * data representation to be stored in the database.
     *
     * @param attribute the entity attribute value to be converted
     * @return the converted data to be stored in the database
     * column
     */
    @Override
    public String convertToDatabaseColumn(AppDefaultGroup attribute) {
        if (Objects.isNull(attribute)) {
            return null;
        }
        return attribute.getCode();
    }

    /**
     * Converts the data stored in the database column into the
     * value to be stored in the entity attribute.
     * Note that it is the responsibility of the converter writer to
     * specify the correct <code>dbData</code> type for the corresponding
     * column for use by the JDBC driver: i.e., persistence providers are
     * not expected to do such type conversion.
     *
     * @param dbData the data from the database column to be
     *               converted
     * @return the converted value to be stored in the entity
     * attribute
     */
    @Override
    public AppDefaultGroup convertToEntityAttribute(String dbData) {
        return AppDefaultGroup.getType(dbData);
    }
}
