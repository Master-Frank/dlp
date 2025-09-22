package cn.topiam.employee.support.validation;

import cn.topiam.employee.support.util.PhoneUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.springframework.util.CollectionUtils;

/**
 * 验证工具类
 * 提供实体验证和属性验证的工具方法
 */
public class ValidationUtils implements Serializable {
   private static final long serialVersionUID = 5985007275059803332L;
   private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

   /**
    * 验证实体
    *
    * @param entity 实体对象
    * @param groups 验证组
    * @param <T> 泛型
    * @return 验证结果
    */
   public static <T> ValidationResult<T> validateEntity(T entity, Class<?>[] groups) {
      return validate(validator.validate(entity, groups));
   }

   /**
    * 私有构造函数，防止实例化
    */
   private ValidationUtils() {
   }

   /**
    * 验证约束违规集合
    *
    * @param constraintViolations 约束违规集合
    * @param <T> 泛型
    * @return 验证结果
    */
   private static <T> ValidationResult<T> validate(Set<ConstraintViolation<T>> constraintViolations) {
      ValidationResult<T> validationResult = new ValidationResult<>();
      if (!CollectionUtils.isEmpty(constraintViolations)) {
         validationResult.setHasErrors(true);
         HashMap<String, String> errorMsg = new HashMap<>(16);
         Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator();

         while (iterator.hasNext()) {
            ConstraintViolation<T> violation = iterator.next();
            errorMsg.put(violation.getPropertyPath().toString(), violation.getMessage());
         }

         validationResult.setErrorMsg(errorMsg);
      }

      validationResult.setConstraintViolations(constraintViolations);
      return validationResult;
   }

   /**
    * 验证实体
    *
    * @param entity 实体对象
    * @param <T> 泛型
    * @return 验证结果
    */
   public static <T> ValidationResult<T> validateEntity(T entity) {
      Class[] groups = new Class[1];
      groups[0] = Default.class;
      return validate(validator.validate(entity, groups));
   }

   /**
    * 验证属性
    *
    * @param entity 实体对象
    * @param propertyName 属性名
    * @param <T> 泛型
    * @return 验证结果
    */
   public static <T> ValidationResult<T> validateProperty(T entity, String propertyName) {
      Class[] groups = new Class[1];
      groups[0] = Default.class;
      return validate(validator.validateProperty(entity, propertyName, groups));
   }

   /**
    * 验证结果类
    *
    * @param <T> 泛型
    */
   public static class ValidationResult<T> implements Serializable {
      private static final long serialVersionUID = 5938977611874009190L;
      private Set<ConstraintViolation<T>> constraintViolations;
      private Map<String, String> errorMsg;
      private boolean hasErrors;

      /**
       * 设置是否有错误
       *
       * @param hasErrors 是否有错误
       */
      public void setHasErrors(boolean hasErrors) {
         this.hasErrors = hasErrors;
      }

      /**
       * 设置错误消息
       *
       * @param errorMsg 错误消息
       */
      public void setErrorMsg(Map<String, String> errorMsg) {
         this.errorMsg = errorMsg;
      }

      /**
       * 获取约束违规集合
       *
       * @return 约束违规集合
       */
      public Set<ConstraintViolation<T>> getConstraintViolations() {
         return this.constraintViolations;
      }

      /**
       * 设置约束违规集合
       *
       * @param constraintViolations 约束违规集合
       */
      public void setConstraintViolations(Set<ConstraintViolation<T>> constraintViolations) {
         this.constraintViolations = constraintViolations;
      }

      /**
       * 判断是否有错误
       *
       * @return 是否有错误
       */
      public boolean isHasErrors() {
         return this.hasErrors;
      }

      /**
       * 默认构造函数
       */
      public ValidationResult() {
         super();
      }

      /**
       * 获取错误消息
       *
       * @return 错误消息
       */
      public String getMessage() {
         if (this.errorMsg != null && !this.errorMsg.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            this.errorMsg.forEach((key, value) -> {
               sb.append(MessageFormat.format("{0}: {1}", key, value));
            });
            return sb.toString();
         } else {
            return "";
         }
      }

      /**
       * 转换为字符串
       *
       * @return 字符串
       */
      @Override
      public String toString() {
         return "ValidationResult{hasErrors=" + this.hasErrors + ", errorMsg=" + String.valueOf(this.errorMsg) + "}";
      }

      /**
       * 获取错误消息
       *
       * @return 错误消息
       */
      public Map<String, String> getErrorMsg() {
         return this.errorMsg;
      }
   }
}
