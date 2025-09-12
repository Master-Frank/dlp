/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.jackjson.encrypt;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2022/12/22 21:53
 */
public class EncryptedJsonDeserializer extends JsonDeserializer<Object> {

    private final JsonEncryptType deserializerJsonEncryptType;

    public EncryptedJsonDeserializer(JsonEncryptType deserializer) {
        this.deserializerJsonEncryptType = deserializer;
    }

    @Override
    public Object deserialize(final JsonParser parser,
                              final DeserializationContext context) throws IOException {
        String value = parser.getValueAsString();
        if (StringUtils.isBlank(value)) {
            return null;
        }
        if (JsonEncryptType.ENCRYPT == deserializerJsonEncryptType) {
            return EncryptContextHelp.encrypt(value);
        } else if (JsonEncryptType.DECRYPT == deserializerJsonEncryptType) {
            return EncryptContextHelp.decrypt(value);
        }
        return value;
    }
}
