package cn.topiam.employee.support.repository.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Generated;

/**
 * 基础实体类
 * 包含创建时间和更新时间等公共字段
 */
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class BaseEntity extends IdEntity {
    
   /** 序列化版本ID */
   private static final long serialVersionUID = 2455905713747945465L;
   
   /** 字段名称常量：最后修改人 */
   public static final String LAST_MODIFIED_BY = "updateBy";
   
   /** 字段名称常量：是否删除字段 */
   public static final String IS_DELETED_COLUMN = "is_deleted";
   
   /** 字段名称常量：创建时间 */
   public static final String CREATE_TIME = "createTime";
   
   /** 字段名称常量：创建人 */
   public static final String CREATE_BY = "createBy";
   
   /** 字段名称常量：最后修改时间 */
   public static final String LAST_MODIFIED_TIME = "updateTime";
   
   /**
    * 创建人
    */
   private String createBy;
   
   /**
    * 创建时间
    */
   private LocalDateTime createTime;
   
   /**
    * 更新人
    */
   private String updateBy;
   
   /**
    * 更新时间
    */
   private LocalDateTime updateTime;
   
   /**
    * 备注信息
    */
   private String remark;
   

   /**
    * 获取创建时间
    *
    * @return 创建时间
    */
   @CreatedDate
   @JsonFormat
   @Access(AccessType.PROPERTY)
   @Column(name = "create_time")
   public LocalDateTime getCreateTime() {
      return this.createTime;
   }

   /**
    * 获取更新时间
    *
    * @return 更新时间
    */
   @LastModifiedDate
   @JsonFormat
   @Access(AccessType.PROPERTY)
   @Column(name = "update_time")
   public LocalDateTime getUpdateTime() {
      return this.updateTime;
   }

   /**
    * 设置创建人
    *
    * @param createBy 创建人
    */
   public void setCreateBy(String createBy) {
      this.createBy = createBy;
   }

   /**
    * 获取创建人
    *
    * @return 创建人
    */
   @CreatedBy
   @Access(AccessType.PROPERTY)
   @Column(name = "create_by")
   public String getCreateBy() {
      return this.createBy;
   }

   /**
    * 设置备注
    *
    * @param remark 备注信息
    */
   public void setRemark(String remark) {
      this.remark = remark;
   }

   /**
    * 设置更新时间
    *
    * @param updateTime 更新时间
    */
   public void setUpdateTime(LocalDateTime updateTime) {
      this.updateTime = updateTime;
   }

   /**
    * 设置更新人
    *
    * @param updateBy 更新人
    */
   public void setUpdateBy(String updateBy) {
      this.updateBy = updateBy;
   }

   /**
    * 返回对象的字符串表示
    *
    * @return 对象字符串表示
    */
   @Override
   public String toString() {
      return "BaseEntity(createBy=" + this.getCreateBy() + 
             ", createTime=" + String.valueOf(this.getCreateTime()) + 
             ", updateBy=" + this.getUpdateBy() + 
             ", updateTime=" + String.valueOf(this.getUpdateTime()) + 
             ", remark=" + this.getRemark() + ")";
   }

   /**
    * 设置创建时间
    *
    * @param createTime 创建时间
    */
   public void setCreateTime(LocalDateTime createTime) {
      this.createTime = createTime;
   }

   /**
    * 获取备注信息
    *
    * @return 备注信息
    */
   @Access(AccessType.PROPERTY)
   @Column(name = "remark_")
   public String getRemark() {
      return this.remark;
   }

   /**
    * 获取更新人
    *
    * @return 更新人
    */
   @LastModifiedBy
   @Access(AccessType.PROPERTY)
   @Column(name = "update_by")
   public String getUpdateBy() {
      return this.updateBy;
   }
}