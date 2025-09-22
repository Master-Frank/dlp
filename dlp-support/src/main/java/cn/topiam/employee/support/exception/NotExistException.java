package cn.topiam.employee.support.exception;

/**
 * 不存在异常
 * 当访问的资源不存在时抛出此异常
 */
public class NotExistException extends TopIamException {
    private static final long serialVersionUID = 2175948631919084666L;

    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param cause 异常原因
     */
    public NotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     */
    public NotExistException(String message) {
        super(message);
    }
}