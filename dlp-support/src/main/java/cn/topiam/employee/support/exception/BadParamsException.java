package cn.topiam.employee.support.exception;

/**
 * 参数错误异常
 * 当传递的参数不符合要求时抛出此异常
 */
public class BadParamsException extends TopIamException {
    /**
     * 构造函数
     *
     * @param message 异常消息
     */
    public BadParamsException(String message) {
        super(message);
    }

    /**
     * 默认构造函数
     */
    public BadParamsException() {
        super("参数错误");
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param cause 异常原因
     */
    public BadParamsException(String message, Throwable cause) {
        super(message, cause);
    }
}