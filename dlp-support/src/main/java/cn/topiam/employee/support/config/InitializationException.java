package cn.topiam.employee.support.config;

import javax.annotation.Nullable;

/**
 * 初始化异常类
 * 用于系统初始化过程中发生的异常
 */
public class InitializationException extends Exception {
    
    /**
     * 序列化版本UID
     */
    private static final long serialVersionUID = 4498093523448059017L;

    /**
     * 构造函数
     */
    public InitializationException() {
    }

    /**
     * 构造函数
     * 
     * @param message 异常信息
     */
    public InitializationException(@Nullable final String message) {
        super(message);
    }

    /**
     * 构造函数
     * 
     * @param message 异常信息
     * @param cause 异常原因
     */
    public InitializationException(@Nullable final String message, @Nullable final Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造函数
     * 
     * @param cause 异常原因
     */
    public InitializationException(@Nullable final Throwable cause) {
        super(cause);
    }
}