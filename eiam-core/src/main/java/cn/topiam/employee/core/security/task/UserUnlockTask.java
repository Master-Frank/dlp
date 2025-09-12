/*
 * eiam-core - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.core.security.task;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import cn.topiam.employee.common.entity.account.UserEntity;
import cn.topiam.employee.common.enums.UserStatus;
import cn.topiam.employee.common.repository.account.UserRepository;
import cn.topiam.employee.support.lock.Lock;
import cn.topiam.employee.support.trace.Trace;

import lombok.RequiredArgsConstructor;
import static cn.topiam.employee.core.context.ContextService.getAutoUnlockTime;

/**
 * 用户锁定自动解锁任务
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/4/17 21:22
 */
@RequiredArgsConstructor
public class UserUnlockTask {

    private final Logger logger = LoggerFactory.getLogger(UserUnlockTask.class);

    /**
     * 每分钟扫描已锁定的用户解锁
     */
    @Trace
    @Lock(throwException = false)
    @Scheduled(cron = "0 */1 * * * ?")
    public void execute() {
        logger.info("用户自动解锁任务开始");
        List<UserEntity> list = userRepository.findAllByStatus(UserStatus.LOCKED);
        logger.info("待解锁用户数量: [{}] ", list.size());
        for (UserEntity entity : list) {
            logger.info("开始解锁用户: {}", entity.getUsername());
            LocalDateTime updateTime = entity.getUpdateTime();
            Integer unlockTime = getAutoUnlockTime();
            if (updateTime.plusMinutes(unlockTime).isBefore(LocalDateTime.now())) {
                entity.setStatus(UserStatus.ENABLED);
                userRepository.save(entity);
                logger.info("成功解锁用户: {}", entity.getUsername());
            }
        }
        logger.info("用户自动解锁任务结束");
    }

    /**
     * UserRepository
     *
     */
    private final UserRepository userRepository;
}
