/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.jackjson.encrypt;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;

/**
 * @author TopIAM
 * Created by support@topiam.cn on 2022/12/22 21:53
 */
public class EncryptedDeserializerModifier extends BeanDeserializerModifier {

    private final JsonEncryptType jsonEncryptType;

    public EncryptedDeserializerModifier() {
        this.jsonEncryptType = null;
    }

    public EncryptedDeserializerModifier(JsonEncryptType jsonEncryptType) {
        this.jsonEncryptType = jsonEncryptType;
    }

    @Override
    public BeanDeserializerBuilder updateBuilder(DeserializationConfig config,
                                                 BeanDescription beanDesc,
                                                 BeanDeserializerBuilder builder) {
        var properties = builder.getProperties();
        while (properties.hasNext()) {
            var property = properties.next();
            JsonPropertyEncrypt annotation = property.getAnnotation(JsonPropertyEncrypt.class);
            if (annotation != null) {
                JsonEncryptType deserializer = jsonEncryptType;
                if (jsonEncryptType == null) {
                    deserializer = annotation.deserializer();
                }
                builder.addOrReplaceProperty(
                    property.withValueDeserializer(new EncryptedJsonDeserializer(deserializer)),
                    true);
            }
        }
        return builder;
    }
}
