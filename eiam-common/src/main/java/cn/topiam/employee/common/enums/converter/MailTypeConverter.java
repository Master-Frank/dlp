/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.enums.converter;

import java.util.Objects;

import cn.topiam.employee.common.enums.MailType;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * MailTemplateTypeReadingConverter
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2020/12/5 21:49
 */
@Converter(autoApply = true)
public class MailTypeConverter implements AttributeConverter<MailType, String> {

    /**
     * Converts the value stored in the entity attribute into the
     * data representation to be stored in the database.
     *
     * @param attribute the entity attribute value to be converted
     * @return the converted data to be stored in the database
     * column
     */
    @Override
    public String convertToDatabaseColumn(MailType attribute) {
        if (!Objects.isNull(attribute)) {
            return attribute.getCode();
        }
        return null;
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
    public MailType convertToEntityAttribute(String dbData) {
        return MailType.getType(dbData);
    }
}
