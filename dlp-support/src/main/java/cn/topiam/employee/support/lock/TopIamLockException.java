package cn.topiam.employee.support.lock;

import cn.topiam.employee.support.util.AesUtils;

/**
 * 分布式锁异常类
 * 当分布式锁操作出现异常时抛出此异常
 */
public class TopIamLockException extends RuntimeException {
    
    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param cause 异常原因
     */
    public TopIamLockException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 默认构造函数
     */
    public TopIamLockException() {
        super("获取分布式锁失败");
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param cause 异常原因
     * @param enableSuppression 是否启用抑制
     * @param writableStackTrace 堆栈跟踪是否可写
     */
    public TopIamLockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     */
    public TopIamLockException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param cause 异常原因
     */
    public TopIamLockException(Throwable cause) {
        super(cause);
    }
}