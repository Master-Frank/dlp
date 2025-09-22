package cn.topiam.employee.support.error;

import cn.topiam.employee.support.error.ErrorAttributesHandler;
import cn.topiam.employee.support.exception.TopIamException;
import cn.topiam.employee.support.exception.enums.ExceptionStatus;
import cn.topiam.employee.support.util.PhoneUtils;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Maps;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 * 自定义错误属性处理器
 * 扩展默认错误属性，提供更详细的错误信息
 */
public class CustomErrorAttributes extends DefaultErrorAttributes {
   public static final String ERRORS = "errors";
   public static final String REQUEST_ID = "requestId";
   public static final String SUCCESS = "success";
   public static final String MESSAGE = "message";
   public static final String STATUS = "status";
   public static final String EXCEPTION = "exception";
   
   /**
    * 错误属性处理器
    */
   private ErrorAttributesHandler errorAttributesHandler;
   
   public static final String TRACE = "trace";
   public static final String ERROR = "error";
   public static final String PATH = "path";

   /**
    * 默认构造函数
    */
   public CustomErrorAttributes() {
      this.errorAttributesHandler = (requestAttributes) -> Maps.newHashMap();
   }

   /**
    * 构造函数
    * 
    * @param errorAttributesHandler 错误属性处理器
    */
   public CustomErrorAttributes(ErrorAttributesHandler errorAttributesHandler) {
      this.errorAttributesHandler = errorAttributesHandler;
   }

   /**
    * 获取错误属性
    * 
    * @param webRequest Web请求
    * @param options 错误属性选项
    * @return 错误属性映射
    */
   public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
      Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
      RequestAttributes requestAttributes = (RequestAttributes) webRequest;
      Map<String, Object> customErrorAttributes = this.errorAttributesHandler.getCustomErrorAttributes(requestAttributes);
      errorAttributes.putAll(customErrorAttributes);
      errorAttributes.put("success", Boolean.FALSE);
      errorAttributes.put("requestId", requestAttributes.getAttribute("REQUEST_ID", 0));
      Object attribute = requestAttributes.getAttribute("jakarta.servlet.error.exception", 0);
      if (attribute instanceof TopIamException) {
         TopIamException topIamException = (TopIamException) attribute;
         errorAttributes.put("message", topIamException.getMessage());
         errorAttributes.put("status", topIamException.getStatus());
      }

      if (attribute instanceof ConstraintViolationException) {
         ConstraintViolationException constraintViolationException = (ConstraintViolationException) attribute;
         Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();
         if (!violations.isEmpty()) {
            StringBuilder message = new StringBuilder();
            Iterator<ConstraintViolation<?>> iterator = violations.iterator();

            while (iterator.hasNext()) {
               ConstraintViolation<?> violation = iterator.next();
               message.append(violation.getMessage()).append(";");
            }

            errorAttributes.put("message", message.substring(0, message.length() - 1));
         }
      }

      if (attribute instanceof BindException) {
         BindException bindException = (BindException) attribute;
         BindingResult bindingResult = bindException.getBindingResult();
         if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
               errorAttributes.put("message", fieldError.getDefaultMessage());
            }
         }
      }

      if (attribute instanceof DataAccessException) {
         errorAttributes.put("message", PhoneUtils.decryptString("\u0018\u000f\u0001\u0001\u001b\u001d\u0018\u001a\u0004\u0011\u0015\u0010\u001e\n\u0012\f\u001a\u001a\u0017\u001d\u000f\u0010\t\u001d\u001a\u001c\u0017\u0017\u0018\u0019\u0017"));
      }

      ExceptionStatus status = (ExceptionStatus) errorAttributes.get("status");
      if (status != null) {
         errorAttributes.put("status", status.getCode());
      }

      if (options.isIncluded(ErrorAttributeOptions.Include.MESSAGE) && errorAttributes.get("message") == null) {
         errorAttributes.put("message", errorAttributes.get("error"));
      }

      return errorAttributes;
   }
}