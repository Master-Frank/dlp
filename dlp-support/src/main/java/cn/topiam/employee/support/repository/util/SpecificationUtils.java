package cn.topiam.employee.support.repository.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

/**
 * 规范工具类
 * 提供常用的JPA Specification构建方法
 */
public class SpecificationUtils {
   
   /**
    * 构建IN条件规范
    *
    * @param columnName 列名
    * @param values 值集合
    * @param <T> 实体类型
    * @return Specification规范
    */
   public static <T> Specification<T> columnIn(String columnName, Collection<?> values) {
      return (root, query, criteriaBuilder) -> {
         return CollectionUtils.isEmpty(values) ? criteriaBuilder.conjunction() : root.get(columnName).in(values);
      };
   }

   /**
    * 构建时间之前条件规范
    *
    * @param columnName 列名
    * @param time 时间
    * @param <T> 实体类型
    * @return Specification规范
    */
   public static <T> Specification<T> columnBefore(String columnName, Date time) {
      if (time == null) {
         throw new NullPointerException("时间参数不能为空");
      } else {
         return (root, query, criteriaBuilder) -> {
            return Objects.isNull(time) ? criteriaBuilder.conjunction() : criteriaBuilder.lessThan(root.get(columnName), time);
         };
      }
   }

   /**
    * 构建LIKE条件规范
    *
    * @param columnName 列名
    * @param likeString LIKE字符串
    * @param <T> 实体类型
    * @return Specification规范
    */
   public static <T> Specification<T> columnLike(String columnName, String likeString) {
      if (likeString == null) {
         throw new NullPointerException("LIKE字符串参数不能为空");
      } else {
         return (root, query, criteriaBuilder) -> {
            return StringUtils.isBlank(likeString) ? criteriaBuilder.conjunction() : criteriaBuilder.like(root.get(columnName), "%" + cn.topiam.employee.support.util.StringUtils.escapeLike(likeString) + "%");
         };
      }
   }

   /**
    * 构建相等条件规范
    *
    * @param columnName 列名
    * @param columnValue 列值
    * @param <T> 实体类型
    * @return Specification规范
    */
   public static <T> Specification<T> columnEqual(String columnName, Object columnValue) {
      if (columnValue == null) {
         throw new NullPointerException("列值参数不能为空");
      } else {
         return (root, query, criteriaBuilder) -> {
            return Objects.isNull(columnValue) ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get(columnName), columnValue);
         };
      }
   }

   /**
    * 构建时间之后条件规范
    *
    * @param columnName 列名
    * @param time 时间
    * @param <T> 实体类型
    * @return Specification规范
    */
   public static <T> Specification<T> columnLate(String columnName, Date time) {
      if (time == null) {
         throw new NullPointerException("时间参数不能为空");
      } else {
         return (root, query, criteriaBuilder) -> {
            return Objects.isNull(time) ? criteriaBuilder.conjunction() : criteriaBuilder.greaterThan(root.get(columnName), time);
         };
      }
   }
}
