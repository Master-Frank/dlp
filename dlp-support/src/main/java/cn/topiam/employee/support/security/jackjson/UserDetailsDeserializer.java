package cn.topiam.employee.support.security.jackjson;

import cn.topiam.employee.support.security.userdetails.UserDetails;
import cn.topiam.employee.support.security.userdetails.UserType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户详情反序列化器
 * 用于将JSON数据反序列化为用户详情对象
 */
public class UserDetailsDeserializer extends JsonDeserializer<UserDetails> {
   
   /**
    * 反序列化用户详情
    * 
    * @param jp JSON解析器
    * @param ctxt 反序列化上下文
    * @return 用户详情
    * @throws IOException IO异常
    */
   @Override
   public UserDetails deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
      JsonNode node = jp.getCodec().readTree(jp);
      String username = node.get("username").asText();
      String password = node.get("password").asText();
      boolean enabled = node.get("enabled").asBoolean();
      UserType userType = UserType.valueOf(node.get("userType").asText());
      
      List<String> authorities = new ArrayList<>();
      node.get("authorities").forEach(authNode -> authorities.add(authNode.asText()));
      
      return new UserDetails(username, password, enabled, userType, authorities);
   }
}