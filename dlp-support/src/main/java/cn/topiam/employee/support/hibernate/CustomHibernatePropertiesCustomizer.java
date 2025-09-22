package cn.topiam.employee.support.hibernate;

import cn.topiam.employee.support.repository.page.domain.Page;
import cn.topiam.employee.support.security.userdetails.Group;
import java.util.Map;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;

/**
 * 自定义Hibernate属性定制器
 * 用于设置Hibernate相关的默认属性
 */
public class CustomHibernatePropertiesCustomizer implements HibernatePropertiesCustomizer {
   
   /**
    * 定制Hibernate属性
    * 
    * @param hibernateProperties Hibernate属性映射
    */
   public void customize(Map<String, Object> hibernateProperties) {
      // 设置Hibernate格式化SQL属性
      if (!hibernateProperties.containsKey(Page.decode("6C<O,D?^;\u0004-B1]\u0001Y/F"))) {
         hibernateProperties.put(
                 Group.decode("@\u000eJ\u0002Z\tI\u0013MI[\u000fG\u0010w\u0014Y\u000b"), 
                 Boolean.FALSE);
      }

      // 设置Hibernate显示SQL属性
      if (!hibernateProperties.containsKey(Page.decode("6C<O,D?^;\u00048E,G?^\u0001Y/F"))) {
         hibernateProperties.put(
                 Group.decode("@\u000eJ\u0002Z\tI\u0013MIN\bZ\nI\u0013w\u0014Y\u000b"), 
                 Boolean.FALSE);
      }

      // 设置Hibernate使用SQL注释属性
      if (!hibernateProperties.containsKey(Page.decode("6C<O,D?^;\u0004+Y;u-[2u=E3G;D*Y"))) {
         hibernateProperties.put(
                 Group.decode("@\u000eJ\u0002Z\tI\u0013MI]\u0014M8[\u0016D8K\bE\nM\t\\\u0014"), 
                 Boolean.FALSE);
      }

      // 设置Hibernate查询替换属性
      if (!hibernateProperties.containsKey(Page.decode("B7H;X0K*OpE,N;X\u0001C0Y;X*Y"))) {
         hibernateProperties.put(
                 Group.decode("\u000fA\u0005M\u0015F\u0006\\\u0002\u0006\bZ\u0003M\u0015w\u000eF\u0014M\u0015\\\u0014"), 
                 Boolean.TRUE);
      }

      // 设置Hibernate查询替换属性
      if (!hibernateProperties.containsKey(Page.decode("B7H;X0K*OpE,N;X\u0001_.N?^;Y"))) {
         hibernateProperties.put(
                 Group.decode("\u000fA\u0005M\u0015F\u0006\\\u0002\u0006\bZ\u0003M\u0015w\u0012X\u0003I\u0013M\u0014"), 
                 "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");
      }
   }
}