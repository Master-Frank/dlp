/*
 * eiam-protocol-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.protocol.code.http.converter;

import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.util.ClassUtils;

/**
 * Utility methods for {@link HttpMessageConverter}'s.
 *
 * @author Joe Grandja
 * @author luamas
 * @since 5.1
 */
public final class HttpMessageConverters {

    private static final boolean JACKSON2_JSON_PRESENT;
    private static final boolean JACKSON2_XML_PRESENT;

    private static final boolean GSON_PRESENT;

    private static final boolean JSONB_PRESENT;

    static {
        ClassLoader classLoader = HttpMessageConverters.class.getClassLoader();
        JACKSON2_JSON_PRESENT = ClassUtils
            .isPresent("com.fasterxml.jackson.databind.ObjectMapper", classLoader)
                                && ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator",
                                    classLoader);

        JACKSON2_XML_PRESENT = ClassUtils
            .isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", classLoader);

        GSON_PRESENT = ClassUtils.isPresent("com.google.gson.Gson", classLoader);
        JSONB_PRESENT = ClassUtils.isPresent("jakarta.json.bind.Jsonb", classLoader);
    }

    private HttpMessageConverters() {
    }

    public static GenericHttpMessageConverter<Object> getJsonMessageConverter() {
        if (JACKSON2_JSON_PRESENT) {
            return new MappingJackson2HttpMessageConverter();
        }
        if (GSON_PRESENT) {
            return new GsonHttpMessageConverter();
        }
        if (JSONB_PRESENT) {
            return new JsonbHttpMessageConverter();
        }
        return null;
    }

    public static GenericHttpMessageConverter<Object> getXmlMessageConverter() {
        if (JACKSON2_XML_PRESENT) {
            return new MappingJackson2XmlHttpMessageConverter();
        }
        return null;
    }

}
