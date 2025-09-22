package cn.topiam.employee.support.security.jackjson;

import cn.topiam.employee.support.security.savedredirect.SavedRedirect;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 保存的重定向反序列化器
 * 用于将JSON数据反序列化为保存的重定向对象
 */
public class SavedRedirectDeserializer extends JsonDeserializer<SavedRedirect> {
   
   /**
    * 反序列化保存的重定向
    * 
    * @param jp JSON解析器
    * @param ctxt 反序列化上下文
    * @return 保存的重定向
    * @throws IOException IO异常
    */
   @Override
   public SavedRedirect deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
      JsonNode node = jp.getCodec().readTree(jp);
      String redirectUrl = node.get("redirectUrl").asText();
      LocalDateTime expireTime = LocalDateTime.parse(node.get("expireTime").asText());
      return new SavedRedirect(redirectUrl, expireTime);
   }
}