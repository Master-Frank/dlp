/*
 * eiam-synchronizer - Employee Identity and Access Management
 * Copyright © 2022-Present Charles Network Technology Co., Ltd.
 */
package cn.topiam.employee.synchronizer.task;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.StopWatch;

import cn.topiam.employee.common.enums.TriggerType;
import cn.topiam.employee.identitysource.core.IdentitySource;
import cn.topiam.employee.identitysource.core.IdentitySourceConfig;
import cn.topiam.employee.support.trace.TraceUtils;

import lombok.RequiredArgsConstructor;
import static cn.topiam.employee.support.constant.EiamConstants.CACHE_LOCK_KEY_PREFIX;
import static cn.topiam.employee.support.constant.EiamConstants.COLON;

/**
 * 身份源同步任务
 *
 * @author TopIAM
 * Created by support@topiam.cn on 2022/3/20 21:41
 */
@RequiredArgsConstructor
public class IdentitySourceSyncTask implements Runnable {
    private final AlternativeJdkIdGenerator idGenerator = new AlternativeJdkIdGenerator();
    private static final Logger             logger      = LoggerFactory
        .getLogger(IdentitySourceSyncTask.class);

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        logger.info("[任务触发]-同步身份源[{}]数据开始", name);
        String traceId = idGenerator.generateId().toString();
        TraceUtils.put(traceId);
        RLock lock = redissonClient.getLock(CACHE_LOCK_KEY_PREFIX + COLON + id);
        boolean tryLock = false;
        try {
            tryLock = lock.tryLock(1, TimeUnit.SECONDS);
            if (tryLock) {
                identitySource.sync(TriggerType.JOB);
            }
        } catch (InterruptedException ignored) {

        } finally {
            stopWatch.stop();
            if (tryLock && lock.isLocked()) {
                lock.unlock();
            }
            TraceUtils.remove();
            logger.info("[任务触发]-同步身份源[{}]数据结束, 执行时长: {} ms", name, stopWatch.getTotalTimeMillis());
        }
    }

    /**
     * ID
     */
    private final String                                         id;

    /**
     * 名称
     */
    private final String                                         name;

    /**
     * RedissonClient
     */
    private final RedissonClient                                 redissonClient;

    /**
     * 身份源
     */
    private final IdentitySource<? extends IdentitySourceConfig> identitySource;

}
