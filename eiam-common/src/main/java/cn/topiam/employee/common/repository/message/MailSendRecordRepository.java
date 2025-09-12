/*
 * eiam-common - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.common.repository.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.topiam.employee.common.entity.message.MailSendRecordEntity;

/**
 * MailSendRecordRepository
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2021/10/3 03:38
 */
@Repository
public interface MailSendRecordRepository extends JpaRepository<MailSendRecordEntity, String> {
}
