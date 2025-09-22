package cn.topiam.employee.support.security.jackjson;

import cn.topiam.employee.support.security.core.GrantedAuthority;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;

/**
 * 授权权限反序列化器
 * 用于将JSON数据反序列化为授权权限对象
 */
public class GrantedAuthorityDeserializer extends JsonDeserializer<GrantedAuthority> {
   
   /**
    * 反序列化授权权限
    * 
    * @param jp JSON解析器
    * @param ctxt 反序列化上下文
    * @return 授权权限
    * @throws IOException IO异常
    */
   @Override
   public GrantedAuthority deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
      JsonNode node = jp.getCodec().readTree(jp);
      String authority = node.get("authority").asText();
      return new GrantedAuthority(authority);
   }
}