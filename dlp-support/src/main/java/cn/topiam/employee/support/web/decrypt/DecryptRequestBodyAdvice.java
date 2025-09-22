package cn.topiam.employee.support.web.decrypt;

import cn.topiam.employee.support.context.ServletContextService;
import cn.topiam.employee.support.enums.SecretType;
import cn.topiam.employee.support.util.AesUtils;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

/**
 * 解密请求体通知类
 * 用于处理需要解密的请求参数
 */
@Component
@ControllerAdvice
public class DecryptRequestBodyAdvice extends RequestBodyAdviceAdapter {
   
   /**
    * 解密请求体
    *
    * @param inputStream 输入流
    * @return 解密后的字符串
    * @throws IOException IO异常
    */
   public static String decryptBody(InputStream inputStream) throws IOException {
      String encryptedData;
      if (StringUtils.isBlank(encryptedData = JSONObject.parseObject(IOUtils.toString(inputStream, StandardCharsets.UTF_8)).getString(SecretType.ENCRYPT.getCode()))) {
         throw new EncryptSecretNotExistException();
      } else {
         HttpSession session = ServletContextService.getSession();

         try {
            String secretKey;
            if (StringUtils.isBlank(secretKey = (String) session.getAttribute(SecretType.ENCRYPT.getKey()))) {
               throw new DecryptException("解密密钥不存在", HttpStatus.BAD_REQUEST);
            }

            encryptedData = AesUtils.decrypt(encryptedData, secretKey);
         } catch (Throwable throwable) {
            session.removeAttribute(SecretType.ENCRYPT.getKey());
            throw throwable;
         }

         session.removeAttribute(SecretType.ENCRYPT.getKey());
         return encryptedData;
      }
   }

   /**
    * 在读取请求体之前处理
    *
    * @param inputMessage 输入消息
    * @param parameter 方法参数
    * @param targetType 目标类型
    * @param converterType 转换器类型
    * @return HttpInputMessage
    * @throws IOException IO异常
    */
   @NonNull
   @Override
   public HttpInputMessage beforeBodyRead(@NonNull final HttpInputMessage inputMessage, @NonNull MethodParameter parameter, @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
      HttpInputMessage result;
      return (result = (HttpInputMessage) decryptBody(inputMessage.getBody(), (decryptedBody) -> {
         final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptedBody.getBytes(StandardCharsets.UTF_8));
         return new HttpInputMessage() {
            @Override
            @NonNull
            public InputStream getBody() {
               return byteArrayInputStream;
            }

            @Override
            @NonNull
            public HttpHeaders getHeaders() {
               return inputMessage.getHeaders();
            }
         };
      })) != null ? result : super.beforeBodyRead(inputMessage, parameter, targetType, converterType);
   }

   /**
    * 解密请求体
    *
    * @param inputStream 输入流
    * @param function 函数
    * @param <T> 泛型
    * @return 解密结果
    * @throws IOException IO异常
    */
   @Nullable
   public static <T> T decryptBody(InputStream inputStream, Function<String, T> function) throws IOException {
      return function.apply(decryptBody(inputStream));
   }

   /**
    * 是否支持该参数
    *
    * @param methodParameter 方法参数
    * @param targetType 目标类型
    * @param converterType 转换器类型
    * @return 是否支持
    */
   @Override
   public boolean supports(MethodParameter methodParameter, @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
      if (!methodParameter.hasMethodAnnotation(DecryptRequestBody.class) && !methodParameter.hasParameterAnnotation(DecryptRequestBody.class)) {
         return false;
      } else {
         return true;
      }
   }
}
