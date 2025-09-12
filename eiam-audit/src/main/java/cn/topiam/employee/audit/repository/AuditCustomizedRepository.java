/*
 * eiam-audit - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.audit.repository;

import java.time.LocalDateTime;
import java.util.List;

import cn.topiam.employee.audit.event.type.EventType;
import cn.topiam.employee.audit.repository.result.AuditStatisticsResult;
import cn.topiam.employee.audit.repository.result.AuthnQuantityResult;

/**
 * 组织成员
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/10/2 02:53
 */
public interface AuditCustomizedRepository {

    List<AuditStatisticsResult> authnHotProvider(List<EventType> types, LocalDateTime startTime,
                                                 LocalDateTime endTime);

    List<AuthnQuantityResult> authnQuantity(List<EventType> types, LocalDateTime startTime,
                                            LocalDateTime endTime, String dateFormat);

    List<AuditStatisticsResult> appVisitRank(EventType type, LocalDateTime startTime,
                                             LocalDateTime endTime);

    List<AuditStatisticsResult> authnZone(List<EventType> types, LocalDateTime startTime,
                                          LocalDateTime endTime);
}
