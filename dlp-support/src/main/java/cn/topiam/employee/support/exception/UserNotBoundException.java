package cn.topiam.employee.support.exception;

/**
 * 用户未绑定异常
 * 当用户未绑定时抛出此异常
 */
public class UserNotBoundException extends TopIamException {
    /**
     * 构造函数
     *
     * @param message 异常消息
     */
    public UserNotBoundException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param cause 异常原因
     */
    public UserNotBoundException(String message, Throwable cause) {
        super(message, cause);
    }
}