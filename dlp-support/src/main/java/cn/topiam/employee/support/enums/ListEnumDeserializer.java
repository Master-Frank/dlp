package cn.topiam.employee.support.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 列表枚举反序列化器
 *
 * @author TopIAM
 * Created by support on 2020/8/18 21:35
 */
public class ListEnumDeserializer extends StdDeserializer<List<? extends BaseEnum>> implements ContextualDeserializer {
    private static final Logger log = LoggerFactory.getLogger(ListEnumDeserializer.class);

    private Class<? extends BaseEnum> clazz;

    public ListEnumDeserializer() {
        super((Class<?>) null);
    }

    public ListEnumDeserializer(Class<? extends BaseEnum> clazz) {
        super((Class<?>) null);
        this.clazz = clazz;
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty property) throws JsonMappingException {
        JavaType type = context.getContextualType();
        if (type != null) {
            JavaType contentType = type.getContentType();
            if (contentType != null) {
                Class<?> rawClass = contentType.getRawClass();
                if (BaseEnum.class.isAssignableFrom(rawClass)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends BaseEnum> enumClass = (Class<? extends BaseEnum>) rawClass;
                    return new ListEnumDeserializer(enumClass);
                }
            }
        }
        return this;
    }

    @Override
    @SuppressWarnings("unused")
    public List<? extends BaseEnum> deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        List<BaseEnum> list = new ArrayList<>();
        String text = parser.getText();
        if (StringUtils.hasText(text)) {
            String[] codes = text.split(",");
            for (String code : codes) {
                try {
                    BaseEnum[] enumConstants = clazz.getEnumConstants();
                    for (BaseEnum baseEnum : enumConstants) {
                        if (baseEnum.getCode().equals(code)) {
                            list.add(baseEnum);
                            break;
                        }
                    }
                } catch (Exception e) {
                    log.warn("反序列化枚举失败: {}", e.getMessage());
                }
            }
        }

        return list;
    }
}
