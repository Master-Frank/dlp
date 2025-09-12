/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cn.topiam.employee.audit.entity.AuditEntity;
import cn.topiam.employee.audit.event.type.EventType;

/**
 * 行为审计repository
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/9/11 22:32
 */
@Repository
public interface AuditRepository extends JpaRepository<AuditEntity, String>,
                                 AuditCustomizedRepository, JpaSpecificationExecutor<AuditEntity> {

    @Query(value = "SELECT COUNT(*) FROM AuditEntity WHERE eventType = :type AND eventTime BETWEEN :startTime AND :endTime")
    Long countByTypeAndTime(@Param("type") EventType type,
                            @Param("startTime") LocalDateTime startTime,
                            @Param("endTime") LocalDateTime endTime);
}
