package cn.topiam.employee.support.repository.base;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

/**
 * ID实体类
 * 包含主键ID字段
 */
@Access(AccessType.FIELD)
@MappedSuperclass
public class IdEntity implements Serializable {

   /**
    * 主键ID
    */
   private String id;

   private static final long serialVersionUID = -7087131058152893045L;

   /**
    * 获取ID
    *
    * @return ID
    */
   @Id
   @UuidGenerator(style = Style.TIME)
   @Access(AccessType.PROPERTY)
   @Column(name = "id_")
   public String getId() {
      return this.id;
   }

   @Override
   public String toString() {
      return "IdEntity(id=" + this.getId() + ")";
   }

   /**
    * 设置ID
    *
    * @param id ID
    */
   public void setId(String id) {
      this.id = id;
   }
}