package cn.topiam.employee.support.repository.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 * 基础租户实体类
 * 包含租户ID字段
 */
@MappedSuperclass
public abstract class BaseTenantEntity extends BaseEntity {
   private static final long serialVersionUID = 4720107236271252583L;
   public static final String ANONYMOUS_TENANT = "anonymousTenant";
   
   /**
    * 租户ID
    */
   @Column(name = "tenant_id")
   private String tenantId;
   
   public static final String TENANT_COLUMN = "tenant_id";
   public static final String TENANT = "tenantId";

   /**
    * 获取租户ID
    *
    * @return 租户ID
    */
   public String getTenantId() {
      return this.tenantId;
   }

   /**
    * 设置租户ID
    *
    * @param tenantId 租户ID
    */
   public void setTenantId(String tenantId) {
      this.tenantId = tenantId;
   }

   @Override
   public String toString() {
      return "BaseTenantEntity(tenantId=" + this.getTenantId() + ")";
   }
}