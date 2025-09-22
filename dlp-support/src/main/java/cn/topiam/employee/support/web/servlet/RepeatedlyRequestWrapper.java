package cn.topiam.employee.support.web.servlet;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import org.springframework.util.StreamUtils;

/**
 * 可重复请求包装器类
 * 扩展HttpServletRequestWrapper，允许请求体被多次读取
 */
public class RepeatedlyRequestWrapper extends HttpServletRequestWrapper {
   /**
    * 请求体字节数组
    */
   private final byte[] body;

   /**
    * 获取读取器
    *
    * @return 读取器
    * @throws IOException IO异常
    */
   @Override
   public BufferedReader getReader() throws IOException {
      return new BufferedReader(new InputStreamReader(this.getInputStream()));
   }

   /**
    * 构造函数
    *
    * @param request HTTP请求
    * @param response Servlet响应
    * @throws IOException IO异常
    */
   public RepeatedlyRequestWrapper(HttpServletRequest request, ServletResponse response) throws IOException {
      super(request);
      request.setCharacterEncoding("UTF-8");
      this.setCharacterEncoding("UTF-8");
      this.body = StreamUtils.copyToByteArray(request.getInputStream());
   }

   /**
    * 获取输入流
    *
    * @return Servlet输入流
    */
   @Override
   public ServletInputStream getInputStream() {
      final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.body);
      return new ServletInputStream() {
         @Override
         public int available() {
            return RepeatedlyRequestWrapper.this.body.length;
         }

         @Override
         public int read() {
            return byteArrayInputStream.read();
         }

         @Override
         public void setReadListener(ReadListener readListener) {
         }

         @Override
         public boolean isFinished() {
            return false;
         }

         @Override
         public boolean isReady() {
            return false;
         }
      };
   }

   /**
    * 获取参数映射
    *
    * @return 参数映射
    */
   @Override
   public Map<String, String[]> getParameterMap() {
      return super.getParameterMap();
   }
}