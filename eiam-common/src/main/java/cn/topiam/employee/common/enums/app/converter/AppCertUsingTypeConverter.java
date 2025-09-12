/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.enums.app.converter;

import java.util.Objects;

import cn.topiam.employee.common.enums.app.AppCertUsingType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * 应用证书使用类型转换器
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/12/11 23:48
 */
@Converter(autoApply = true)
public class AppCertUsingTypeConverter implements AttributeConverter<AppCertUsingType, String> {

    /**
     * Converts the value stored in the entity attribute into the
     * data representation to be stored in the database.
     *
     * @param attribute the entity attribute value to be converted
     * @return the converted data to be stored in the database
     * column
     */
    @Override
    public String convertToDatabaseColumn(AppCertUsingType attribute) {
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
    public AppCertUsingType convertToEntityAttribute(String dbData) {
        return AppCertUsingType.getType(dbData);
    }
}
