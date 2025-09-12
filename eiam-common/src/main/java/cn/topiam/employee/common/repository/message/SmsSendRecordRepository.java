/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.topiam.employee.common.entity.message.SmsSendRecordEntity;

/**
 * @author TopIAM
 */
@Repository
public interface SmsSendRecordRepository extends JpaRepository<SmsSendRecordEntity, String> {

}
