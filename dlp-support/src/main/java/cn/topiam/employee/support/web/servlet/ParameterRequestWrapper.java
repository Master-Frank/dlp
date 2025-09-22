package cn.topiam.employee.support.web.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/**
 * 参数请求包装器类
 * 扩展HttpServletRequestWrapper，提供额外的参数处理功能
 */
public class ParameterRequestWrapper extends HttpServletRequestWrapper {
   /**
    * 参数映射
    */
   private final Map<String, String[]> parameterMap;

   /**
    * 获取参数
    *
    * @param name 参数名
    * @return 参数值
    */
   @Override
   public String getParameter(String name) {
      String[] values = this.parameterMap.get(name);
      if (values != null && values.length != 0) {
         return values[0];
      } else {
         return null;
      }
   }

   /**
    * 获取参数名枚举
    *
    * @return 参数名枚举
    */
   @Override
   public Enumeration<String> getParameterNames() {
      return (new Vector(this.parameterMap.keySet())).elements();
   }

   /**
    * 添加参数
    *
    * @param name 参数名
    * @param value 参数值
    */
   public void addParameter(String name, Object value) {
      if (value != null) {
         if (value instanceof String[]) {
            this.parameterMap.put(name, (String[]) value);
            return;
         }

         if (value instanceof String) {
            String[] values = new String[1];
            values[0] = (String) value;
            this.parameterMap.put(name, values);
            return;
         }

         String[] values = new String[1];
         values[0] = String.valueOf(value);
         this.parameterMap.put(name, values);
      }
   }

   /**
    * 获取参数值数组
    *
    * @param name 参数名
    * @return 参数值数组
    */
   @Override
   public String[] getParameterValues(String name) {
      String[] values = this.parameterMap.get(name);
      return values != null && values.length != 0 ? values : null;
   }

   /**
    * 构造函数
    *
    * @param request HTTP请求
    * @param parameters 参数映射
    */
   public ParameterRequestWrapper(HttpServletRequest request, Map<String, Object> parameters) {
      super(request);
      this.addAllParameters(parameters);
   }

   /**
    * 添加所有参数
    *
    * @param parameters 参数映射
    */
   public void addAllParameters(Map<String, Object> parameters) {
      Iterator iterator = parameters.entrySet().iterator();
      while (iterator.hasNext()) {
         Map.Entry entry = (Map.Entry) iterator.next();
         this.addParameter((String) entry.getKey(), entry.getValue());
      }
   }

   /**
    * 构造函数
    *
    * @param request HTTP请求
    */
   public ParameterRequestWrapper(HttpServletRequest request) {
      super(request);
      this.parameterMap = new HashMap<>(16);
      this.parameterMap.putAll(request.getParameterMap());
   }
}