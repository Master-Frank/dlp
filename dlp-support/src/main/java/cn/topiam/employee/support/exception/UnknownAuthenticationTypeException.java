package cn.topiam.employee.support.exception;

import org.springframework.http.HttpStatus;

/**
 * 未知认证类型异常
 * 当认证类型未知时抛出此异常
 */
public class UnknownAuthenticationTypeException extends TopIamException {
    /**
     * 构造函数
     *
     * @param cause 异常原因
     * @param errorCode 错误码
     * @param message 异常消息
     * @param httpStatus HTTP状态码
     */
    public UnknownAuthenticationTypeException(Throwable cause, String errorCode, String message, HttpStatus httpStatus) {
        super(cause, errorCode, message, httpStatus);
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param httpStatus HTTP状态码
     */
    public UnknownAuthenticationTypeException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    /**
     * 构造函数
     *
     * @param errorCode 错误码
     * @param message 异常消息
     * @param httpStatus HTTP状态码
     */
    public UnknownAuthenticationTypeException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param cause 异常原因
     */
    public UnknownAuthenticationTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 默认构造函数
     */
    public UnknownAuthenticationTypeException() {
        super("未知认证类型", "未知认证类型", HttpStatus.UNAUTHORIZED);
    }
}